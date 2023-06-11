package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddUpdate extends AppCompatActivity {

    EditText edtID = findViewById(R.id.edt_id),
            edtName = findViewById(R.id.edt_name),
            edtDepart = findViewById(R.id.edt_depart);
    Button btnSave = findViewById(R.id.btn_save);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        Person updating = (Person) getIntent().getSerializableExtra("CANSUA");
        if(updating != null){
            edtID.setText(updating.getID());
            edtName.setText(updating.getName());
            edtDepart.setText(updating.getDepart());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = edtID.getText().toString(),
                        name = edtName.getText().toString(),
                        depart = edtDepart.getText().toString();
                if(ID.trim().equals(""))
                    Toast.makeText(AddUpdate.this, "Vui lòng nhập mã nhân viên.", Toast.LENGTH_SHORT).show();
                else if(Personnel.personnelList.size() != 0)
                    for(Person p : Personnel.personnelList)
                        if(ID.equals(p.getID())){
                            Toast.makeText(AddUpdate.this, "Mã nhân viên đã tồn tại.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                else if(name.trim().equals(""))
                    Toast.makeText(AddUpdate.this, "Vui lòng nhập tên nhân viên.", Toast.LENGTH_SHORT).show();
                else if(depart.trim().equals(""))
                    Toast.makeText(AddUpdate.this, "Vui lòng nhập phòng ban.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",ID);
                    bundle.getString("TEN",name);
                    bundle.putString("PHONGBAN",depart);
                    intent.putExtras(bundle);
                    setResult(15,intent);
                    finish();
                }
            }
        });
    }
}