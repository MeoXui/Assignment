package com.example.assignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Personnel extends AppCompatActivity {

    public static final String FILE_NAME = "personnelListData";
    Toolbar toolbar = findViewById(R.id.toolbar);
    Button btnAdd = findViewById(R.id.btn_Add);
    ListView lvPersonnel = findViewById(R.id.list_personnel);
    public static List<Person> personnelList;
    Apdater apdater;

    void refresh(){
        apdater= new Apdater(this, personnelList);
        lvPersonnel.setAdapter(apdater);
        writeData();
    }

    private void writeData() {
        try {
            FileOutputStream fos = this.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(personnelList);
            oos.close();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readData() {
        List<Person> dataFile = new ArrayList<>();
        try {
            FileInputStream fis = this.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            dataFile = (List<Person>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        personnelList = dataFile;
    }

    ActivityResultLauncher<Intent> addPerson = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 15) {
                        Intent intent = result.getData();
                        if(intent != null){
                            Bundle bundle = intent.getExtras();
                            String ID = bundle.getString("ID"),
                                    name = bundle.getString("TEN"),
                                    depart = bundle.getString("PHONGBAN");
                            personnelList.add(new Person(ID,name,depart));
                            refresh();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nhân viên");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readData();
        refresh();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Personnel.this, AddUpdate.class);
                Toast.makeText(Personnel.this, "Thêm nhân viên.", Toast.LENGTH_SHORT).show();
                addPerson.launch(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem itemSearch = menu.findItem(R.id.search);
        EditText edtSearch = (EditText)itemSearch.getActionView();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                apdater.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    int indexUpdate;
    ActivityResultLauncher<Intent> updatePerson= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 15) {
                        Intent intent = result.getData();
                        if(intent != null){
                            Bundle bundle = intent.getExtras();
                            String ID = bundle.getString("ID"),
                                    name = bundle.getString("TEN"),
                                    depart = bundle.getString("PHONGBAN");
                            personnelList.set(indexUpdate,new Person(ID,name,depart));
                            refresh();
                        }
                    }
                }
            });

    public void update(int position) {
        this.indexUpdate = position;
        Intent intent = new Intent(Personnel.this, AddUpdate.class);
        Toast.makeText(Personnel.this, "Sửa nhân viên.", Toast.LENGTH_SHORT).show();
        Person updating = personnelList.get(position);
        intent.putExtra("CANSUA", (Serializable) updating);
        updatePerson.launch(intent);
    }

    private void delete(int position) {
        new AlertDialog.Builder(this)
                .setMessage("Bạn có chắc muốn xoá sinh viên này không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        personnelList.remove(position);
                        refresh();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    public static class Apdater extends BaseAdapter implements Filterable {

        private final Context context;
        private List<Person> list;
        private final List<Person> listOld;

        public Apdater(Context context, List<Person> list) {
            this.context = context;
            this.list = list;
            this.listOld = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ((Personnel)context).getLayoutInflater();
            convertView =  inflater.inflate(R.layout.item_list_personnel, parent, false);

            TextView txtID = convertView.findViewById(R.id.txt_id),
                    txtName = convertView.findViewById(R.id.txt_name),
                    txtDepart = convertView.findViewById(R.id.txt_department);
            ImageButton btnUpdate = convertView.findViewById(R.id.imgbtn_update),
                    btnDelete = convertView.findViewById(R.id.imgbtn_delete);

            txtID.setText(list.get(position).getID());
            txtName.setText(list.get(position).getName());
            txtDepart.setText(list.get(position).getDepart());

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Personnel) context).update(position);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Personnel) context).delete(position);
                }
            });

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String sSeach = constraint.toString();
                    if(sSeach.isEmpty()) list = listOld; else{
                        List<Person> listSearch = new ArrayList<>();
                        for (Person p : listOld)
                            if(p.getName().toLowerCase().contains(sSeach.toLowerCase()))
                                listSearch.add(p);
                        list = listSearch;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = list;

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    list = (List<Person>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

}