package com.example.apptogooglesheets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Additem extends AppCompatActivity implements View.OnClickListener {


    EditText desp,amt;
    Button buttonAddItem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        desp = (EditText)findViewById(R.id.description);
        amt = (EditText)findViewById(R.id.amount);

        buttonAddItem = (Button)findViewById(R.id.submit);
        buttonAddItem.setOnClickListener(this);


    }

    //This is the part where data is transafeered from Your Android phone to Sheet by using HTTP Rest API calls

    private void  addItemToSheet() {
//        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        Log.d("CREATION", "Hello");
        String descp = desp.getText().toString();
        String amnt = amt.getText().toString();
        Log.d("CREATION", "Value:" + descp);
        Log.d("CREATION", "Value:" + amnt);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwGGTliF2h_VJKwjySqrNoTm5E3MIHV8-dy7mSkEzhjeFWIppQXxBi9w3I-Kdv6F-tS/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(Additem.this,response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                //here we pass params
                params.put("action","addItem");
                params.put("Description",descp);
                params.put("Amount",amnt);

                return params;
            }
        };

//        int socketTimeOut = 4000;// u can change this .. here it is 50 seconds
//
//        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(retryPolicy);
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        queue.add(stringRequest);


    }




    @Override
    public void onClick(View v) {
        Log.d("CREATION", "HIII");
        if(v==buttonAddItem){
            addItemToSheet();

            //Define what to do when button is clicked
        }
    }
}
