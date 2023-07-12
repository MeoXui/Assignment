package com.example.assignment;

import static com.example.assignment.Login.FILE_NAME;
import static com.example.assignment.Login.userList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class Register extends AppCompatActivity {

    EditText edtUsername = findViewById(R.id.edt_username_reg),
            edtPassword = findViewById(R.id.edt_password_reg),
            edtRepassword = findViewById(R.id.edt_repassword);
    Button btnRegister = findViewById(R.id.btn_register_reg),
            btnBack = findViewById(R.id.btn_back);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUsername = edtUsername.toString(),
                        strPassword = edtPassword.toString(),
                        strRepassword = edtRepassword.toString();
                if(strUsername.trim().equals(""))
                    Toast.makeText(getApplicationContext(),"Vui lòng nhậm username.",Toast.LENGTH_SHORT).show();
                else if (strPassword.trim().equals(""))
                    Toast.makeText(getApplicationContext(),"Vui lòng nhậm password.",Toast.LENGTH_SHORT).show();
                else if (strRepassword.trim().equals(""))
                    Toast.makeText(getApplicationContext(),"Vui lòng nhậm lại password.",Toast.LENGTH_SHORT).show();
                else if (strRepassword.equals(strPassword))
                    Toast.makeText(getApplicationContext(),"Password nhập lại bị sai.",Toast.LENGTH_SHORT).show();
                else {
                    userList.add(new User(strUsername,strPassword));
                    writeUserListData(FILE_NAME);
                    Toast.makeText(Register.this,"Đăng ký thành công.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Register.this,"Huỷ đăng ký", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void writeUserListData(String fileName) {
        List<User> dataFile =  userList;
        try {
            FileOutputStream fos = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dataFile);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}