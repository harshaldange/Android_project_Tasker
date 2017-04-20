package com.mohdeva.learn.tasker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends AppCompatActivity implements View.OnClickListener {

    private android.support.v7.widget.AppCompatButton btnDatePicker, btnloc,btncont;
    private String nameString;
    private TextView int_name;
    int taskid;

    DBController controller=new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setTitle("Select an option");


        //Recieve data from intent
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                nameString= null;
                taskid=-1;
            } else {
                nameString= extras.getString("Datacont");
                taskid=extras.getInt("taskid");
            }
        } else {
            nameString= (String) savedInstanceState.getSerializable("Datacont");
        }
        this.setTitle(nameString);
        int_name= (TextView) findViewById(R.id.IntentName);
        int_name.setText("More Details About :\n"+nameString);
        btnDatePicker=(android.support.v7.widget.AppCompatButton)findViewById(R.id.btn_d);
        btnloc=(android.support.v7.widget.AppCompatButton)findViewById(R.id.btn_loc);
        btncont=(android.support.v7.widget.AppCompatButton)findViewById(R.id.btn_c);
        Toast.makeText(getApplicationContext()," "+taskid,Toast.LENGTH_SHORT).show();
        btnDatePicker.setOnClickListener(this);
        btncont.setOnClickListener(this);
        btnloc.setOnClickListener(this);

        //To Know which option is selected to edit
        int Option=0;
        //Get data from db which function was opted
        Option=0;

        if(Option==1){
            //Date was selected
            btnloc.setEnabled(false);
            btncont.setEnabled(false);
        }
        else if(Option==2){
            //Contact was selected
            btnloc.setEnabled(false);
            btnDatePicker.setEnabled(false);
        }
        else if(Option==3){
            //Location was selected
            btnDatePicker.setEnabled(false);
            btncont.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {
            taskid=controller.getId(nameString);
            boolean flag=controller.updatetype(taskid,"time_date");
            if(flag)
                Toast.makeText(getApplicationContext(),"Type Inserted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"Type Isn't Inserted",Toast.LENGTH_SHORT).show();
            Intent date = new Intent(Main.this, DateAndTime.class);
            //pass id
            date.putExtra("taskid",taskid);
            startActivity(date);
        }
        if (v == btncont) {
            taskid=controller.getId(nameString);
            boolean flag=controller.updatetype(taskid,"call_message");
            if(flag)
                Toast.makeText(getApplicationContext(),"Type Inserted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"Type Isn't Inserted",Toast.LENGTH_SHORT).show();
            Intent cont = new Intent(Main.this, Cont.class);
            //pass id
            cont.putExtra("taskid",taskid);
            startActivity(cont);
        }
        if(v==btnloc)
        {
            taskid=controller.getId(nameString);
            boolean flag=controller.updatetype(taskid,"location");
            if(flag)
                Toast.makeText(getApplicationContext(),"Type Inserted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"Type Isn't Inserted",Toast.LENGTH_SHORT).show();
            Intent loc=new Intent(Main.this,location.class);
//            loc.putExtra("Datacont",nameString);
            loc.putExtra("taskid",taskid);
            loc.putExtra("issaved",0);
            startActivity(loc);
        }

    }
    public void onBackPressed()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main.this);
        // Setting Dialog Title
        alertDialog.setTitle("Discard Changes..");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to discard the changes?");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                // Write your code here to invoke YES event
                Toast.makeText(Main.this, "Discarded", Toast.LENGTH_SHORT).show();
                controller.deleteTask(taskid,"tasks");
                Intent main=new Intent(Main.this,Todo.class);
                startActivity(main);
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
    }

}
