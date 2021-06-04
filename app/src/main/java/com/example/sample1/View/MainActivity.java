package com.example.sample1.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample1.EmployeeForm;
import com.example.sample1.R;
import com.example.sample1.RoomDatabase.EmployeeDao;
import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.RoomDatabase.EmployeeTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;

    // variable to display data on list of cardView
    TextView tvDispEmployeeId;
    TextView tvDispName;
    TextView tvDispSalary;
    TextView tvDispRole;
    TextView tvDispCompanyName;

    //Firebase instance;
    private FirebaseAuth mAuth;

    RecyclerView recyclerView;

    List<EmployeeTable> dataList=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    EmployeeDatabase employeeDatabase;
    DataAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab=findViewById(R.id.fab);

        tvDispEmployeeId=findViewById(R.id.tvDisEmployeeId);
        tvDispName=findViewById(R.id.tvDisName);
        tvDispSalary=findViewById(R.id.tvDisSalary);
        tvDispRole=findViewById(R.id.tvDisRole);
        tvDispCompanyName=findViewById(R.id.tvDisCompanyName);

        mAuth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.recylerList);

        // Initialize database
        employeeDatabase=EmployeeDatabase.getDatabase(this);
        // store database values in data list
        dataList=employeeDatabase.employeeDao().getAll();

        // Initialize linear layout manager
        linearLayoutManager=new LinearLayoutManager(this);
        //setLayout
        recyclerView.setLayoutManager(linearLayoutManager);
        // Initialize adapter
        adapter=new DataAdapter(MainActivity.this,dataList);
        //set Adapter
        recyclerView.setAdapter(adapter);






        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,EmployeeForm.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "settings" +
                        "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signOut:
                mAuth.signOut();
                Toast.makeText(this, "LOGOUT", Toast.LENGTH_SHORT).show();
               // this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}