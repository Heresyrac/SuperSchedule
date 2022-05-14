package com.example.SuperSchedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.multiprocess.IWorkManagerImpl;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.PublicKey;

public class EventEditor extends AppCompatActivity implements View.OnClickListener{
    private Button event_cancel_bot;
    private Button event_confirm_bot;
    private EditText event_edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventeditorscreen);
        event_cancel_bot = (Button) findViewById(R.id.event_cancel_button);
        event_confirm_bot = (Button) findViewById(R.id.event_confirm_button);
        event_edittext = (EditText) findViewById(R.id.event_EditText);
        event_cancel_bot.setOnClickListener(this);
        event_confirm_bot.setOnClickListener(this);
        event_edittext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.event_confirm_button:
                String InputEvent = event_edittext.getText().toString();
                break;
            case R.id.event_cancel_button:
                break;
            default:
                break;
        }
    }
}