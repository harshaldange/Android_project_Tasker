package com.mohdeva.learn.tasker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class Cont extends AppCompatActivity {
    ListView listView;
    int taskid;
    EditText contNum;
    Button Add;
    ListView lv;
    AutoCompleteTextView a1;
    ArrayList<String> nameList = new ArrayList<String>();

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Cursor c;
    ArrayList contacts = new ArrayList();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cont);
        Intent pit=getIntent();
        taskid=pit.getIntExtra("taskid",-1);
        listView = (ListView) findViewById(R.id.idList);

        setTitle("Select Contact");

        //Check Permission
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) { //Name of Method for Calling Message
            showContacts();
        } else { //TODO
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_CONTACTS
            }, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        final AutoCompleteTextView a1 = (AutoCompleteTextView) findViewById(R.id.contactname);
        contNum = (EditText) findViewById(R.id.phoneNum);
        Add = (Button) findViewById(R.id.button1);
        lv = (ListView)findViewById(R.id.idList);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts);
        listView.setAdapter(adapter);

        //AutoComplete
        ArrayAdapter aadapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,contacts);
        a1.setAdapter(aadapter);

        //AutoComplete Selector
        a1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String strName =(String) arg0.getItemAtPosition(arg2);
                //Splitting From nextline
                String[] words=strName.split("\\n");
                //Splitting name
                String[] w1=words[0].split(": ");
                //Splitting Contact
                String[] w2=words[1].split(": ");

                contNum.setText(w2[1].toString());
                a1.setText(w1[1].toString());
            }
        });

        //Add Button
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = a1.getText().toString();
                String Cont = contNum.getText().toString();
                if(Name == null || Name.isEmpty() || Cont == null || Cont.isEmpty()){
                    Toast.makeText(Cont.this,"No Contact Selected", Toast.LENGTH_SHORT).show();
                }
                else{
                    String strName = Name+"::"+Cont;
                    Intent i = new Intent(Cont.this, SaveCont.class);
                    i.putExtra("Data", strName);
                    i.putExtra("taskid",taskid);
                    Toast.makeText(Cont.this,strName, Toast.LENGTH_SHORT).show();
                    startActivity(i);

                }

            }
        });

        //select Content
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String strName =(String) arg0.getItemAtPosition(arg2);
                //Splitting From nextline
                String[] words=strName.split("\\n");
                //Splitting name
                String[] w1=words[0].split(": ");
                //Splitting Contact
                String[] w2=words[1].split(": ");
                //Setting Text
                contNum.setText(w2[1].toString());
                a1.setText(w1[1].toString());
//                Toast.makeText(Cont.this, nameList.get(arg2), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Reading Contacts from Device
    private void showContacts() {
        c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");
        while (c.moveToNext()) {
            String contactName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            nameList.add(contactName);
            contacts.add("Name: " + contactName + "\n" + "PhoneNo: " + phNumber);
        }
        c.close();
    }
}
