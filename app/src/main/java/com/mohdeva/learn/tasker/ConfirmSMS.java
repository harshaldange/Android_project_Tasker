package com.mohdeva.learn.tasker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmSMS extends AppCompatActivity {
    private Button Reject,Confirm;
    private TextView Name,Cont;
    private EditText SMSContent;
    private String cont;
    private String content;
    private String name;
    String phoneNo;
    String message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_sms);
        //Recieve data from intent
        String nameString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                nameString= null;
            } else {
                nameString= extras.getString("Data");
            }
        } else {
            nameString= (String) savedInstanceState.getSerializable("Data");
        }
        String[] data = nameString.split("::");
        name=data[0];
        cont=data[1];
        content=data[2];

        Reject=(Button)findViewById(R.id.Reject_sms);
        Confirm=(Button)findViewById(R.id.Confirm_sms);
        Name=(TextView)findViewById(R.id.Name);
        Cont=(TextView)findViewById(R.id.contact);
        SMSContent=(EditText)findViewById(R.id.data);

        Name.setText(data[0]);
        Cont.setText(data[1]);
        SMSContent.setText(data[2]);

        Confirm.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendSMSMessage();
            }
        });
    }
    protected void sendSMSMessage() {
        phoneNo = Cont.getText().toString();
        message = SMSContent.getText().toString();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
