package com.example.SuperSchedule;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GroupScreen extends AppCompatActivity implements View.OnClickListener{

    private String[] starArray = {"Calender1","Calendar2","Calendar3"};
    private Spinner group_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupscreen);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Group");
        }
        Button group_invitation = (Button) findViewById(R.id.group_invitation);
        Button group_return = (Button) findViewById(R.id.group_return);
        Button group_delete = (Button) findViewById(R.id.group_delete);
        Button group_new = (Button) findViewById(R.id.group_new);
        group_spinner = (Spinner) findViewById(R.id.group_spinner);
        group_invitation.setOnClickListener(this);
        group_delete.setOnClickListener(this);
        group_return.setOnClickListener(this);
        group_new.setOnClickListener(this);
        initSpinner();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSpinner(){
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(this,R.layout.item_select,starArray);
        //设置数组适配器的布局样式
        starAdapter.setDropDownViewResource(R.layout.item_dropdown);
        //从布局文件中获取名叫sp_dialog的下拉框
        Spinner sp = findViewById(R.id.group_spinner);
        //设置下拉框的标题，不设置就没有难看的标题了
        sp.setPrompt("select calendar");
        //设置下拉框的数组适配器
        sp.setAdapter(starAdapter);
        //设置下拉框默认的显示第一项
        sp.setSelection(0);
        //给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        sp.setOnItemSelectedListener(new MySelectedListener());
    }

    class MySelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String s = "You select："+ starArray[i];
            Toast.makeText(GroupScreen.this, s ,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_invitation:
                new AlertDialog.Builder(this).setTitle("INVITATION UID")//设置对话框标题

                        .setMessage("4be2da54-a3a3-4b1f-a8a2-3c2600ec90fa")
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
                finish();
                break;
            case R.id.group_delete:
                onDestroy();
                break;
            case R.id.group_new:
                break;
        }
    }
}
