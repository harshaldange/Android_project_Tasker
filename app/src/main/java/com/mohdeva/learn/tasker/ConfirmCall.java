package com.mohdeva.learn.tasker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmCall extends AppCompatActivity {

    TextView nam,con;
    Button conf,rej;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_call);

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

        nam=(TextView)findViewById(R.id.contName);
        con=(TextView)findViewById(R.id.contNum);
        conf=(Button)findViewById(R.id.conf);
        rej=(Button)findViewById(R.id.rej);

        nam.setText(data[0]);
        con.setText(data[1]);

        conf.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_CALL);
                String sNum = con.getText().toString();
                if (sNum.trim().isEmpty()) {
                    Toast.makeText(ConfirmCall.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    i.setData(Uri.parse("tel:" + sNum));
                }
                if (ActivityCompat.checkSelfPermission(ConfirmCall.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ConfirmCall.this, "Please grant the permission to call", Toast.LENGTH_SHORT).show();
                    requestPermission();
                } else {
                    startActivity(i);
                }
            }
        });
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.CALL_PHONE
        }, 1);
    }
}
