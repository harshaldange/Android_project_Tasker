package com.mohdeva.learn.tasker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;

public class Todo extends AppCompatActivity {
    private ImageButton btnSpeak;
    private EditText recSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int PERMISSION_REQUEST_CODE = 600;
    private ListView lv;
    private Button btn;
    private int iterator = 0; //for tasks
    private int confirmFlag=2;

    DBController controller = new DBController(this);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent z = new Intent(Todo.this, ExpenseMgr.class);
                startActivity(z);
                return true;
            case R.id.item2:
                Intent i = new Intent(Todo.this, ChangePassword.class);
                startActivity(i);
                finish();
                return true;
            case R.id.item3:
                Intent x = new Intent(Todo.this, Developers.class);
                startActivity(x);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recSpeak = (EditText) findViewById(R.id.TEXT);
        btnSpeak = (ImageButton) findViewById(R.id.Speech);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        btn = (Button) findViewById(R.id.ADD);
        lv = (ListView) findViewById(R.id.list);

        checkPermission();
        requestPermission();
        // Initializing a new String Array
        //to get from db
        ArrayList<HashMap<String, String>> taskList =  controller.getAllTasks();

        String sum = "";
        for (HashMap<String, String> hash : taskList) {
            for (String current : hash.values()) {
                sum = sum + current + "<#>";
            }
        }
        //Toast.makeText(getApplicationContext(), sum.toString(), Toast.LENGTH_SHORT).show();
        //    \\d+ Regular Expression
        String[] tasks = new String[]{

        };
        if(!sum.isEmpty()){
            tasks = sum.split("<#>\\d+<#>");
        }
        // Create a List from String Array elements

        final List<String> tasks_list = new ArrayList<String>(Arrays.asList(tasks));


        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, tasks_list);
        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new Items to List
                String temp=recSpeak.getText().toString();
                if(temp != null && !temp.isEmpty()) {
//                    tasks_list.add(temp);
                    recSpeak.setText("");

                    HashMap<String, String> queryValues =  new  HashMap<String, String>();
                    queryValues.put("taskName", temp);
                    controller.insertTask(queryValues);
                    arrayAdapter.notifyDataSetChanged();
                    //        Notifies the attached observers that the underlying
                    //        data has been changed and any View reflecting the
                    //        data set should refresh itself.
//                    arrayAdapter.notifyDataSetChanged();
                    int id=controller.getId(temp);
                    Intent selecttype=new Intent(Todo.this,Main.class);
                    selecttype.putExtra("Datacont",temp);
                    selecttype.putExtra("taskid",id);
                    startActivity(selecttype);
                }
                else{
                    Toast.makeText(Todo.this,
                            "No Task !", Toast.LENGTH_LONG).show();
                }
                confirmFlag=2;
            }
        });
        // React to user clicks on item
        // LongClick
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //Confirm Delete
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Todo.this);
                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to invoke YES event
                        String type = "tasks";
                        String item = tasks_list.get(position);
                        int tid=controller.getId(item);
                        Cursor cursor=controller.gettype(tid);
                            if(cursor.moveToFirst())
                            {
                                type = cursor.getString(0);
                            }
//                        Toast.makeText(getApplicationContext()," "+cursor.getCount(),Toast.LENGTH_SHORT).show();
                        controller.deleteTask(tid,type);
                        controller.deleteTask(tid,"tasks");
                        tasks_list.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(Todo.this, "You Deleted : " + item, Toast.LENGTH_SHORT).show();
                    }
                });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //invoke NO event
                        Toast.makeText(getApplicationContext(), "Okay ", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
                return true;
            }
        });
//        short click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String strName =(String) arg0.getItemAtPosition(arg2);
                int taskid=controller.getId(strName);
                Intent i = new Intent(Todo.this,MainAfterSelected.class);
                i.putExtra("Datacont", strName);
                i.putExtra("taskid",taskid);
                startActivity(i);
            }
        });

    }
    //SPEECH STARTS HERE
    // Showing google speech input dialog
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Your Command");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
        }
    }
    // Receiving speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    recSpeak.setText(result.get(0));
                }
                break;
            }

        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED&& result2 == PackageManager.PERMISSION_GRANTED&& result3 == PackageManager.PERMISSION_GRANTED&& result4 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, READ_CONTACTS,ACCESS_COARSE_LOCATION,INTERNET,SEND_SMS,CALL_PHONE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                        //Toast.makeText(Todo.this, "Grant The Next Permission", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, READ_CONTACTS,ACCESS_COARSE_LOCATION,INTERNET,SEND_SMS,CALL_PHONE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Todo.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void onBackPressed()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Todo.this);
        // Setting Dialog Title
        alertDialog.setTitle("Exit");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to Exit?");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                // Write your code here to invoke YES event
                finish();
                System.exit(0);
            }
        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //invoke NO event
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}