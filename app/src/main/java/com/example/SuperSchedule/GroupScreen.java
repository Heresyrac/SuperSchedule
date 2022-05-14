package com.example.SuperSchedule;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GroupScreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupscreen);
        Button group_invitation = (Button) findViewById(R.id.group_invitation);
        Button group_return = (Button) findViewById(R.id.group_return);
        Button group_enter = (Button) findViewById(R.id.group_enter);
        Button group_delete = (Button) findViewById(R.id.group_delete);
        Button group_new = (Button) findViewById(R.id.group_new);
        group_invitation.setOnClickListener(this);
        group_delete.setOnClickListener(this);
        group_enter.setOnClickListener(this);
        group_return.setOnClickListener(this);
        group_new.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_invitation:
                new AlertDialog.Builder(this).setTitle("INVITATION UID")//设置对话框标题

                        .setMessage("UID")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {//添加确定按钮

                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {//添加返回按钮

                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件

                    }

                }).show();
                break;
            case R.id.group_return:
                //to 4
                break;
            case R.id.group_enter:
                break;
            case R.id.group_delete:
                break;
            case R.id.group_new:
                break;
        }
    }
}
