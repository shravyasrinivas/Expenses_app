package com.example.apptogooglesheets;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

public class ProgressButton {

        CardView cardView;
        ProgressBar progressBar;
        TextView textView;
        RelativeLayout relativeLayout;
        Animation fade_in;
    ProgressButton(Context ct, View view){
        cardView= view.findViewById(R.id.cardView);
         progressBar=view.findViewById(R.id.bar);
         textView=view.findViewById(R.id.submitBtn);
         relativeLayout=view.findViewById(R.id.proglayout);
    }
    void ButtonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Please Wait");
    }
    void Btnfinished(){
        relativeLayout.setBackgroundColor(cardView.getResources().getColor(R.color.lime_green));
        progressBar.setVisibility(View.GONE);
        textView.setText("Done");
    }
}
