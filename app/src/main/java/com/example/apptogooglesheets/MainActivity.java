package com.example.apptogooglesheets;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     private static final String TAG = "MainActivity";
     EditText desp,amt;
     Spinner dropdown2,dropdown3,dropdown1;
     TextView mDisplayDate;
     private DatePickerDialog.OnDateSetListener mDateSetListener;
     ImageButton btnadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnadd=(ImageButton)findViewById(R.id.submit);
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

        String[] items3 = new String[]{"Cash", "CB343","Citibank credit card","Gpay SBI,Madi","Gpay,Shravya","Madhavi Gpay","Metro Card","SBI,Madipakkam","Shravya SBI Debit card"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this,R.layout.spinner_main, items3);
        dropdown3.setAdapter(adapter3);
        adapter3.setDropDownViewResource(R.layout.spinner_list);

        btnadd.setOnClickListener(this);

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
            }
        };
    }
    @Override
    public void onClick(View v) {
        if(v==btnadd){
            //startActivity(new Intent(getApplicationContext(),CustomProgressButton.class));
            //Intent intent=new Intent(MainActivity.this,CustomProgressButton.class);
            AlertDialog dialog;
            final int Dialog_show_time=5000;
            String date=mDisplayDate.getText().toString();
            String descp = desp.getText().toString();
            String amnt = amt.getText().toString();
            String cred=String.valueOf(dropdown1.getSelectedItem());
            String selectedText = String.valueOf(dropdown2.getSelectedItem());
            String selectedText2 = String.valueOf(dropdown3.getSelectedItem());
            boolean correct = !date.isEmpty() && !descp.isEmpty()  &&!amnt.isEmpty() ;
            if (correct) {
                dialog= new SpotsDialog(this,R.style.Custom);
                dialog.show();
                //final ProgressDialog loading = ProgressDialog.show(this, "Adding Item", "Please wait");
                String sheetsURL = "https://script.google.com/macros/s/AKfycbxfnpEmztQiTRatsc1vw6jSmeK_NvXAN3WIaF4IqxNtaaXl3Nha5O-2cR6lHduZ-Pmr/exec";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, sheetsURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("CREATION", "Successful post" + response);
//                                ProgressButton progressButton=new ProgressButton(MainActivity.this,v);
//                                progressButton.ButtonActivated();
//                                Handler handler=new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        progressButton.Btnfinished();
//                                    }
//                                },3000);
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
        }

    }
}