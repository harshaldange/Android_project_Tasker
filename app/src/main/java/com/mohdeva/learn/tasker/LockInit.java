package com.mohdeva.learn.tasker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.WindowManager;
import android.widget.TextView;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LockInit extends AppCompatActivity {

    Vibrator vibrator;
    private Button b1, b2, b3, b4, Con;
    private static final String FILENAME = "pat.txt";
    private StringBuilder curPattern = new StringBuilder(100);
    private StringBuilder finalPattern = new StringBuilder(100);
    private int flag=0;
    private Intent todo;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lockinit);

        //displays the content in full screen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setTitle("Initialize your lock screen");

        tv = (TextView)findViewById(R.id.TextView);
        b1 = (Button) findViewById(R.id.one);
        b2 = (Button) findViewById(R.id.two);
        b3 = (Button) findViewById(R.id.three);
        b4 = (Button) findViewById(R.id.four);
        Con= (Button) findViewById(R.id.Lockcontinue);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(flag==0){
                    curPattern.append("1");
                }
                if(flag==1){
                    finalPattern.append("1");
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(flag==0){
                    curPattern.append("2");
                }
                if(flag==1){
                    finalPattern.append("2");
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(flag==0){
                    curPattern.append("3");
                }
                if(flag==1){
                    finalPattern.append("3");
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(flag==0){
                    curPattern.append("4");
                }
                if(flag==1){
                    finalPattern.append("4");
                }
            }
        });

        Con.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(flag==1){
                    int temp=finalPattern.toString().length();
                    if (curPattern.toString().equals(finalPattern.toString())){
                        if(temp==0){
                            //Confirm Delete
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LockInit.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Pattern Empty");
                            // Setting Dialog Message
                            alertDialog.setMessage("Are you sure to keep your pattern empty?");
                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    // Write your code here to invoke YES event
                                    String pattern= finalPattern.toString();
                                    writeToFile(pattern);
                                    todo = new Intent(LockInit.this, Todo.class);
                                    startActivity(todo);
                                    finish();
                                }
                            });
                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //invoke NO event
                                    Toast.makeText(getApplicationContext(), "Set Password again ", Toast.LENGTH_SHORT).show();
                                    flag=0;
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        }
                        else {
                            String pattern = finalPattern.toString();
                            writeToFile(pattern);
                            todo = new Intent(LockInit.this, Todo.class);
                            startActivity(todo);
                            finish();
                        }
                    }
                    else{
                        flag=0;
                        curPattern.setLength(0);
                        finalPattern.setLength(0);
                        vibrator.vibrate(400);
                        tv.setText("Please Assign your pattern");
                        Toast.makeText(LockInit.this, "Password Did not match", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(flag==0){
                    flag=1;
                    tv.setText("Confirm Password once again");
                    Con.setText("Confirm Pattern");
                    Con.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
//            Log.e(TAG, "File write failed: " + e.toString());
        }

    }



}
