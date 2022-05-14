package com.example.SuperSchedule;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SuperSchedule.entity.User;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity<flag> extends AppCompatActivity implements View.OnClickListener {

    private LoginActivity loginactivity = new LoginActivity();
    private EditText signup_username;
    private EditText signup_phone;
    private EditText signup_email;
    private EditText signup_password;
    private EditText signup_confirm;
    private ImageButton signup_photo;
    private User userinfo = new User();
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        Button signup_return = (Button) findViewById(R.id.signup_return);
        Button signup_enroll = (Button) findViewById(R.id.signup_enroll);
        Button signup_showhide = (Button) findViewById(R.id.signup_showhide);
        signup_photo = (ImageButton) findViewById(R.id.signup_photo);
        signup_username = (EditText) findViewById(R.id.signup_username);
        signup_phone = (EditText) findViewById(R.id.signup_phone);
        signup_email = (EditText) findViewById(R.id.signup_email);
        signup_password = (EditText) findViewById(R.id.signup_password);
        signup_confirm = (EditText) findViewById(R.id.signup_confirm);
        signup_return.setOnClickListener(this);
        signup_enroll.setOnClickListener(this);
        signup_showhide.setOnClickListener(this);
        signup_photo.setOnClickListener(this);
        flag = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_return:
                Intent unsignup_intent = new Intent(this, DashboardActivity.class);
                startActivity(unsignup_intent);
                break;
            case R.id.signup_enroll:
                userinfo.name = signup_username.getText().toString();
                userinfo.phone = signup_phone.getText().toString();
                userinfo.email = signup_email.getText().toString();
                userinfo.password = signup_password.getText().toString();
                String confirm = signup_confirm.getText().toString();
                int type = check(userinfo.password, confirm, userinfo.email,userinfo.phone,userinfo.name);
                switch (type) {
                    case -4:
                        Toast.makeText(this,"input username",Toast.LENGTH_SHORT).show();
                        break;
                    case -3:
                        Toast.makeText(this,"wrong phone format",Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(this,"wrong email format",Toast.LENGTH_SHORT).show();
                        break;
                    case -1:
                        Toast.makeText(this,"wrong passwd format",Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Intent signup_intent = new Intent(this, DashboardActivity.class);
                        startActivity(signup_intent);
                        break;
                    case 1:
                        Toast.makeText(this,"wrong confirm",Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case R.id.signup_photo:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;
            case R.id.signup_showhide:
                if(flag){
                    signup_password.setInputType(0x91);
                    flag = false;
                }
                else{
                    signup_password.setInputType(0x81);
                    flag = true;
                }
                break;
        }
    }

    private int check(String passwd, String confirm, String email, String phone, String name) {
        if(name.length() == 0)
            return -4;
        if(!isphone(phone))
            return -3;
        if(!isEmail(email))
            return -2;
        int num = 0, charnum = 0, dif = 0;
        if (passwd.length() <= 6)
            return -1;
        for (int i = 0; i < passwd.length(); i++) {
            if (passwd.charAt(i) <= '9' && passwd.charAt(i) >= '0')
                num += 1;
            if (passwd.charAt(i) >= 'a' && passwd.charAt(i) <= 'z' || passwd.charAt(i) >= 'A' && passwd.charAt(i) <= 'Z')
                charnum += 1;
        }
        if (num > 0 && charnum > 0) {
            if (passwd.length() != confirm.length())
                return 1;
            for (int i = 0; i < passwd.length(); i++) {
                if (passwd.charAt(i) != confirm.charAt(i))
                    return 1;
            }

            return 0;
        } else return -1;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //将Bitmap设置到imageView
                signup_photo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }

    private boolean isphone(String phone){
        if(phone.length() == 0)
            return true;
        if(phone.length() != 11)
            return false;
        for(int i = 0; i < phone.length(); i++)
            if(phone.charAt(i) > '9' || phone.charAt(i) < '0')
                return false;
        return true;
    }
}

