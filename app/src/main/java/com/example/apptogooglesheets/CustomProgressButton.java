package com.example.apptogooglesheets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomProgressButton extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progress_button);
        ProgressBar progressBar=findViewById(R.id.progressBar);
        TextView textView=findViewById(R.id.submitBtn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Please Wait");
                progressBar.setVisibility(View.VISIBLE);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("Done");
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setTextColor(Color.parseColor("3DDC84"));
                        finish();
                       // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        Intent intent=new Intent(CustomProgressButton.this,MainActivity.class);
                    }
                },4000);
            }
        });
    }
}