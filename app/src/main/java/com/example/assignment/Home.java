package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    Button btnDepartment = findViewById(R.id.btn_department),
            btnPersonnel = findViewById(R.id.btn_personnel),
            btnExit = findViewById(R.id.btn_exit);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Department.class);
                Toast.makeText(Home.this, "Phòng ban.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        btnPersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Personnel.class);
                Toast.makeText(Home.this, "Nhân viên", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hỏi thầy về chức năng thoát ứng dụng
                Toast.makeText(Home.this, "Thoát.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
}