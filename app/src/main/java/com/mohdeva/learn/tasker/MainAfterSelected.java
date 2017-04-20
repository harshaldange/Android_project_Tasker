package com.mohdeva.learn.tasker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainAfterSelected extends AppCompatActivity {

    String nameString,type;
    int taskid,issaved=1;
    private android.support.v7.widget.AppCompatButton b1,b2;
    DBController controller=new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_after_selected);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if(extras == null) {
                nameString=" ";
                taskid=-1;
            } else {
                nameString=extras.getString("Datacont");
                taskid=extras.getInt("taskid");
            }
        }
        Cursor cursor=controller.gettype(taskid);
        if(cursor.moveToFirst())
        {
            type=cursor.getString(0);
        }
        TextView txt=(TextView)findViewById(R.id.IntentName);
        Toast.makeText(getApplicationContext()," "+taskid,Toast.LENGTH_SHORT).show();
        txt.setText("More Details About "+nameString+":");
        TextView t=(TextView)findViewById(R.id.textView6);
        t.setVisibility(View.INVISIBLE);
        android.support.v7.widget.AppCompatButton b1=(android.support.v7.widget.AppCompatButton)findViewById(R.id.btn_c);
        android.support.v7.widget.AppCompatButton b2=(android.support.v7.widget.AppCompatButton)findViewById(R.id.btn_d);
        android.support.v7.widget.AppCompatButton b3=(android.support.v7.widget.AppCompatButton)findViewById(R.id.btn_l);
        if(type.equals("location")) {
            b1.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.INVISIBLE);
        }
        else if(type.equals("time_date"))
        {
            b1.setVisibility(View.INVISIBLE);
            b3.setVisibility(View.INVISIBLE);
        }
        else if(type.equals("call_message")){
            b2.setVisibility(View.INVISIBLE);
            b3.setVisibility(View.INVISIBLE);
        }

    }
    public void onBackPressed(){
        finishAffinity();
        Intent main=new Intent(MainAfterSelected.this,Todo.class);
        startActivity(main);
    }
    public void showlocation(View view)
    {
//        Toast.makeText(getApplicationContext(),"Aa raha hai "+taskid,Toast.LENGTH_SHORT).show();
        finishAffinity();
        Intent x=new Intent(MainAfterSelected.this,location.class);
        x.putExtra("Datacont",nameString);
        x.putExtra("taskid",taskid);
        x.putExtra("Issaved",issaved);
        startActivity(x);
    }

}
