package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    public static List<User> userList = new ArrayList<>();
    public static String FILE_NAME = "userListData",
            MEMORY = "memory",
            REMEMBER = "remembered",
            NAME = "name_remembered",
            PASS = "pass_remembered";
    EditText edtUsername = findViewById(R.id.edt_username_log),
            edtPassword = findViewById(R.id.edt_password_log);
    CheckBox chkRemember = findViewById(R.id.chk_remember);
    Button btnLogin = findViewById(R.id.btn_login),
            btnRegister = findViewById(R.id.btn_register_log);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memoryCheck();

        chkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                memo();
            }
        });

        readUserListData(FILE_NAME);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                Toast.makeText(Login.this,"Đăng ký.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUsername = edtUsername.toString(),
                        strPassword = edtPassword.toString();
                boolean check = false;

                if(strUsername.trim().equals(""))
                    Toast.makeText(getApplicationContext(),"Vui lòng nhậm username.",Toast.LENGTH_SHORT).show();
                else if (strPassword.trim().equals(""))
                    Toast.makeText(getApplicationContext(),"Vui lòng nhậm password.",Toast.LENGTH_SHORT).show();
                else if (userList.size() != 0) for(User u : userList){
                    check = strUsername.equals(u.getUsername()) && strPassword.equals(u.getPassword());
                    if(check)break;
                }

                if (check){
                    Intent intent = new Intent(Login.this, Home.class);
                    Toast.makeText(Login.this,"Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } else Toast.makeText(Login.this,"Username hoặc password chưa đúng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readUserListData(String fileName) {
        List<User> dataFile = new ArrayList<>();
        try {
            FileInputStream fis = this.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            dataFile = (List<User>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        userList = dataFile;
    }

    private void memo() {
        SharedPreferences sharedPreferences = getSharedPreferences(MEMORY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME,edtUsername.toString());
        editor.putString(PASS,edtPassword.toString());
        editor.apply();
    }

    private void memoryCheck(){
        SharedPreferences sharedPreferences = getSharedPreferences(MEMORY, MODE_PRIVATE);
        chkRemember.setChecked(sharedPreferences.getBoolean(REMEMBER,false));
        if(chkRemember.isChecked()){
            edtUsername.setText(sharedPreferences.getString(NAME,""));
            edtPassword.setText(sharedPreferences.getString(PASS,""));
        }
    }
}