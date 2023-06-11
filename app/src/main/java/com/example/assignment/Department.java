package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Department extends AppCompatActivity {

    Toolbar toolbar = findViewById(R.id.toolbar);
    ListView listDepartment = findViewById(R.id.list_department);
    Apdater apdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Phòng ban");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> list = new ArrayList<>();
        list.add("Nhân sự");
        list.add("Hành chính");
        list.add("Nhân sự");

        apdater = new Apdater(this,list);
        listDepartment.setAdapter(apdater);

        listDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Department.this, list.get(position), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public class Apdater extends BaseAdapter implements Filterable {

        private final Context context;
        private List<String> list;
        private final List<String> listOld;

        public Apdater(Context context, List<String> list) {
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
            LayoutInflater inflater = ((Department)context).getLayoutInflater();
            convertView =  inflater.inflate(R.layout.item_list_department, parent, false);

            TextView txt = convertView.findViewById(R.id.txt_item);

            txt.setText(list.get(position));

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String sSeach = constraint.toString();
                    if(sSeach.isEmpty()) list = listOld; else{
                        List<String> listSearch = new ArrayList<>();
                        for (String str : listOld)
                            if(str.toLowerCase().contains(sSeach.toLowerCase()))
                                listSearch.add(str);
                        list = listSearch;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = list;

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    list = (List<String>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
}