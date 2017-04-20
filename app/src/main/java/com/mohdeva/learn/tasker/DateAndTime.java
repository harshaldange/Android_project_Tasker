package com.mohdeva.learn.tasker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DateAndTime extends AppCompatActivity implements View.OnClickListener {

    private Button btnDatePicker, btnTimePicker,btnSave;
    private EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute,taskid,issaved;
    DBController controller=new DBController(this);
    String date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_and_time);
        Intent pit=getIntent();
        taskid=pit.getIntExtra("taskid",-1);

        setTitle("Date And Time");

        btnDatePicker=(Button)findViewById(R.id.btn_d);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        btnSave=(Button)findViewById(R.id.buttonSave);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            date=String.valueOf(year);
                            String temp1=String.valueOf(monthOfYear);
                            String temp2=String.valueOf(dayOfMonth);
                            StringBuilder stringBuilder=new StringBuilder(date);
                            stringBuilder.append(temp1);
                            stringBuilder.append(temp2);
                            date=stringBuilder.toString();
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            time=String.valueOf(hourOfDay);
                            String temp=String.valueOf(minute);
                            StringBuilder stringBuilder=new StringBuilder(time);
                            stringBuilder.append(temp);
                            time=stringBuilder.toString();
//                            time.concat(temp);
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == btnSave){

//            Toast.makeText(getApplicationContext(),date+":"+time,Toast.LENGTH_SHORT).show();
            boolean isdone=controller.insertTime(taskid,date,time);
            if(isdone)
            {
                Toast.makeText(getApplicationContext(),"Date Inserted",Toast.LENGTH_SHORT).show();
                finishAffinity();
                Intent tolist=new Intent(DateAndTime.this,Todo.class);
                startActivity(tolist);
            }
            else
                Toast.makeText(getApplicationContext(),"Date Isn't Inserted", Toast.LENGTH_SHORT).show();
        }
    }
}
