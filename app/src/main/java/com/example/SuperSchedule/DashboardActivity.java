package com.example.SuperSchedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        Button dashboard_schedule = findViewById(R.id.schedule);
        Button dashboard_location = findViewById(R.id.location);
        dashboard_location.setOnClickListener(this);
        dashboard_schedule.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.schedule:
                //to 4
                break;
            case R.id.location:
                //to 5
                break;
        }
    }
}