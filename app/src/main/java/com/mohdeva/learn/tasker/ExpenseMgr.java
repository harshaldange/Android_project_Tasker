package com.mohdeva.learn.tasker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpenseMgr extends AppCompatActivity {
    Button balanceAdd,expenseAdd;
    ListView expenseHistory;
    TextView balanceDb;
    EditText incBal,reason,reasonAmt;
    float amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_mgr);

        balanceAdd = (Button)findViewById(R.id.addBalance);
        expenseAdd = (Button)findViewById(R.id.addExp);
        balanceDb = (TextView)findViewById(R.id.balance);
        expenseHistory=(ListView)findViewById(R.id.histExp);
        incBal = (EditText)findViewById(R.id.incBal);
        reason = (EditText)findViewById(R.id.reasonExp);
        reasonAmt=(EditText)findViewById(R.id.amt);

        //get from db
        amount= (float)100.0;
        balanceDb.setText(String.valueOf(amount));

        //get from db
        String[] exp = new String[] {
                "Veg"+"\n"+"50"
        };

        final List<String> exp_list = new ArrayList<String>(Arrays.asList(exp));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, exp_list);

        // DataBind ListView with items from ArrayAdapter
        expenseHistory.setAdapter(arrayAdapter);

        expenseHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                //Confirm Delete

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExpenseMgr.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");


                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        String item = exp_list.get(position);
                        exp_list.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(ExpenseMgr.this, "You Deleted : " + item, Toast.LENGTH_SHORT).show();
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

        balanceAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(incBal.toString().length()!=0){
                    float temp = Float.valueOf(incBal.getText().toString());
                    amount+=temp;
                    balanceDb.setText(String.valueOf(amount));
                    //update balance in db
                    incBal.setText("");
                }

            }
        });
        expenseAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String Left=reason.getText().toString();
                String Right = reasonAmt.getText().toString();
                float temp1= Float.valueOf(Right);
                amount-=temp1;
                balanceDb.setText(String.valueOf(amount));
                String resultText=Left+"\n"+Right;
                final SpannableString styledResultText = new SpannableString(resultText);
                styledResultText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), Left.length() , Left.length() +Right.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                exp_list.add(styledResultText.toString());
                arrayAdapter.notifyDataSetChanged();
                reason.setText("");
                reasonAmt.setText("");
            }
        });
    }
}
