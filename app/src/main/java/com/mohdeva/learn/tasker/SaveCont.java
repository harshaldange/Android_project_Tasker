package com.mohdeva.learn.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Text;

public class SaveCont extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private String nameString;
    private String Name, Cont;
    public String number,message,date,time;
    RadioButton call,sms;
    EditText smsContent;
    Button conf;
    TextView name,cont;
    RadioGroup rg1;

    static int hour, min,type=0;

    TextView txtdate, txttime;
    Button btntimepicker, btndatepicker;

    java.sql.Time timeValue;
    SimpleDateFormat format;
    Calendar c;
    int year, month, day,taskid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loc);

        call=(RadioButton)findViewById(R.id.radioCall);
        sms=(RadioButton)findViewById(R.id.radioSms);
        conf=(Button)findViewById(R.id.confirm);
        smsContent=(EditText)findViewById(R.id.smsContent);
        name=(TextView)findViewById(R.id.Name);
        cont=(TextView)findViewById(R.id.Cont);
        rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
        rg1.setOnCheckedChangeListener(this);

        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        txtdate = (TextView) findViewById(R.id.txtdate);
        txttime = (TextView) findViewById(R.id.txttime);

        btndatepicker = (Button) findViewById(R.id.btndatepicker);
        btntimepicker = (Button) findViewById(R.id.btntimepicker);

        smsContent.setEnabled(false);

        //Recieve data from intent
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                nameString= null;
            } else {
                nameString= extras.getString("Data");
            }
        }
        else {
            nameString= (String) savedInstanceState.getSerializable("Data");
        }
        Intent pit=getIntent();
        taskid=pit.getIntExtra("taskid",-1);

        //Splitting name and Number
        String[] data = nameString.split("::");
        Name = data[0];
        Cont = data[1];
        name.setText(Name);
        cont.setText(Cont);

        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(SaveCont.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    date=String.valueOf(year);
                                    String temp1=String.valueOf(monthOfYear);
                                    String temp2=String.valueOf(dayOfMonth);
                                    StringBuilder stringBuilder=new StringBuilder(date);
                                    stringBuilder.append(temp1);
                                    stringBuilder.append(temp2);
                                    date=stringBuilder.toString();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);

                                    txtdate.setText(formatter.format(date).toString());
//                                    formatter = new SimpleDateFormat("dd/MMM/yyyy");
//                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());
//                                    formatter = new SimpleDateFormat("dd-MM-yyyy");
//                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());
//                                    formatter = new SimpleDateFormat("dd.MMM.yyyy");
//                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                                } catch (Exception ex) {

                                }
                            }
                        }, year, month, day);
                dd.show();
            }
        });
        btntimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog td = new TimePickerDialog(SaveCont.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    time=String.valueOf(hourOfDay);
                                    String temp=String.valueOf(minute);
                                    StringBuilder stringBuilder=new StringBuilder(time);
                                    stringBuilder.append(temp);
                                    time=stringBuilder.toString();

                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    format = new SimpleDateFormat("HH:mm");
                                    timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    txttime.setText(String.valueOf(timeValue));
//                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");
//                                    txttime.setText(amPm + "\n" + String.valueOf(timeValue));
                                } catch (Exception ex) {
                                    txttime.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(SaveCont.this)
                );
                td.show();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i)
        {
            case R.id.radioSms:
                actv(true);
                type=1;
                break;

            case R.id.radioCall:
                actv(false);
                smsContent.setText(null);
                break;
        }
    }
    private void actv(final boolean active)
    {
        smsContent.setEnabled(active);
        if (active)
        {
            smsContent.requestFocus();
        }
    }
    public void savenumber(View view)
    {
        DBController controller=new DBController(this);
        String contact_name = name.getText().toString();
        number=cont.getText().toString();
        message=smsContent.getText().toString();
        boolean result=false;
//        if(message==null) {
//            result = controller.insertnumber(taskid, number, contact_name,message, date, time,0);
//        }
//        else
        {
            result = controller.insertnumber(taskid, number, contact_name,message, date, time,type);
        }
        if(result) {
            Toast.makeText(getApplicationContext(), "Call Details Inserted", Toast.LENGTH_SHORT).show();
            finishAffinity();
            Intent tolist=new Intent(SaveCont.this,Todo.class);
            startActivity(tolist);
        }
        else
            Toast.makeText(getApplicationContext(),"Call Details Not Inserted",Toast.LENGTH_SHORT).show();


    }
}
