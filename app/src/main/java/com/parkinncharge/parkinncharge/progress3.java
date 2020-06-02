package com.parkinncharge.parkinncharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.kofigyan.stateprogressbar.StateProgressBar;


public class progress3 extends AppCompatActivity {
    String[] descriptionData = {"Photo", "My details", "Submit"};
    Button btn,btn1;
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress3);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.progress4);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateSize(30f);
        stateProgressBar.setStateNumberTextSize(15f);
        stateProgressBar.setStateLineThickness(5f);
        btn1 = findViewById( R.id.button26 );
        btn = findViewById( R.id.button22 );
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent( getApplicationContext(),Main2Activity.class );

                startActivity( intent );
            }
        } );



    }

}
