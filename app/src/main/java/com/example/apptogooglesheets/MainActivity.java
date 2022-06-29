package com.example.apptogooglesheets;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     private static final String TAG = "MainActivity";
     EditText desp,amt;
     Spinner dropdown2,dropdown3,dropdown1;
     TextView mDisplayDate;
     TextView timeField;
     private DatePickerDialog.OnDateSetListener mDateSetListener;
     ImageButton btnadd;
     Button btnviewitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnadd=(ImageButton)findViewById(R.id.submit);
        btnviewitems = (Button)findViewById(R.id.viewitems);
        desp = (EditText)findViewById(R.id.description);
        amt = (EditText)findViewById(R.id.amount);
        dropdown1=(Spinner) findViewById(R.id.credited);
        mDisplayDate = (TextView) findViewById(R.id.Date);
        dropdown2 = (Spinner)findViewById(R.id.type);
        dropdown3 = (Spinner)findViewById(R.id.modeofpayment);


        String[] items2 = new String[]{"Household", "Rent","Office","Medicine"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_main, items2);
        dropdown2.setAdapter(adapter2);
        adapter2.setDropDownViewResource(R.layout.spinner_list);


        String[] items1 = new String[]{"debited","credited"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_main, items1);
        dropdown1.setAdapter(adapter1);
        adapter1.setDropDownViewResource(R.layout.spinner_list);

        String[] items3 = new String[]{"Cash", "IT","CB343","Citibank credit card","CB2199","SBI,Madhavi","SBI,Shravya","Alandur SBI","Gpay SBI,Madi","Gpay,Shravya","Madhavi Gpay","Metro Card","SBI,Madipakkam","Shravya SBI Debit card","PhonePay(SBI Alandur)","Shravya PayTM"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this,R.layout.spinner_main, items3);
        dropdown3.setAdapter(adapter3);
        adapter3.setDropDownViewResource(R.layout.spinner_list);

        btnadd.setOnClickListener(this);
        btnviewitems.setOnClickListener(this);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
                SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Calendar cal2 = Calendar.getInstance();
                String time=format.format(cal2.getTime());
                timeField =findViewById(R.id.time);
                timeField.setText(time);
            }
        };
        (findViewById(R.id.view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://docs.google.com/spreadsheets/d/1pOJxPkQqYgGgWIonP3GPh7Up1YYBPGeReddTJi3xo1A/edit?usp=sharing";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        btnviewitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    System.out.println("Button Clicked");
                    Intent activity2Intent = new Intent(getApplicationContext(), ListItem.class);
                    startActivity(activity2Intent);
                }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==btnadd){
            AlertDialog dialog;
            final int Dialog_show_time=5000;
            String date=mDisplayDate.getText().toString();
            String time= timeField.getText().toString();
            String descp = desp.getText().toString();
            String amnt = amt.getText().toString();
            String cred=String.valueOf(dropdown1.getSelectedItem());
            String selectedText = String.valueOf(dropdown2.getSelectedItem());
            String selectedText2 = String.valueOf(dropdown3.getSelectedItem());
            @SuppressLint({"NewApi", "LocalSuppress"}) boolean correct = !date.isEmpty() && !descp.isEmpty()  &&!amnt.isEmpty() ;
            if (correct) {
                dialog= new SpotsDialog(this,R.style.Custom);
                dialog.show();
                //final ProgressDialog loading = ProgressDialog.show(this, "Adding Item", "Please wait");
             //   String sheetsURL="https://script.google.com/macros/s/AKfycbwYAiT8ER0SGLjax5wcgpiDgYTI-YA7OY80moTTP76GI11fauDciHiYkLiIEdOge0yg/exec";
                String sheetsURL = "https://script.google.com/macros/s/AKfycbxhvl73RHJjCG8np0gXPpI7D4qqp3CdreSxsoTOyad0SY4crskpBxo9WcfRbM8CS0pM/exec";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, sheetsURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("CREATION", "Successful post" + response);
//                                ProgressBar bar = (ProgressBar) loading.findViewById(android.R.id.progress);
//                                //Getting a DONE(new) drawable from resources
//                                Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_done_24);
//                                Drawable indeterminateDrawable = bar.getIndeterminateDrawable();
//                                //Obtain a bounds of current drawable
//                                Rect bounds = indeterminateDrawable.getBounds();
//                                //Set bounds to DONE(new) drawable
//                                drawable.setBounds(bounds);
//                                //Set a new drawable
//                                bar.setIndeterminateDrawable(drawable);
//                                loading.setTitle("Done.");
//                                loading.setMessage("Successfully added");
                                dialog.dismiss();
                                desp.setText("");
                                amt.setText("");
                                mDisplayDate.setText("");
                                timeField.setText("");
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Successfully Added ", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.lime_green));
                                snackbar.setTextColor(Color.parseColor("#FFFFFF"));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.dismiss();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Failed to add, check internet", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.red));
                                snackbar.setTextColor(Color.parseColor("#FFFFFF"));
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        params.put("action", "add_item");
                        params.put("date", date);
                        params.put("description", descp);
                        params.put("amount", amnt);
                        params.put("credited", cred);
                        params.put("type", selectedText);
                        params.put("modeOfPayment", selectedText2);
                        params.put("time",time);

                        Log.d("CREATION", "Creating params");
                        return params;
                    }
                };

                int socketTimeOut = 40000;

                RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(stringRequest);
            }
            else
            {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please fill in empty fields", Snackbar.LENGTH_LONG);
                snackbar.show();
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.red));
                snackbar.setTextColor(Color.parseColor("#FFFFFF"));

            }
//            if(v==btnviewitems){
////                Intent intent = new Intent(getApplicationContext(),ListItem.class);
////                startActivity(intent);
//                Intent i = new Intent(MainActivity.this,ListItem.class);
//                startActivity(i);
//            }
        }

    }

}