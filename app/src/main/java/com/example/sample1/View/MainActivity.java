package com.example.sample1.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample1.HomePage.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sample1.LoginRegister.Login;
import com.example.sample1.R;
import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.ViewModel.EmployeeViewModel;
import com.example.sample1.adapter.DataAdapter;
import com.example.sample1.model.EmployeeTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_EMPLOYEE_REQUEST_CODE = 10;

    // Variable to display data on list of cardView
    TextView tvDispEmployeeId;
    TextView tvDispName;
    TextView tvDispSalary;
    TextView tvDispRole;
    TextView tvDispCompanyName;
    ImageView btnSort;
    FloatingActionButton fab;

    EmployeeViewModel employeeViewModel;

    List<EmployeeTable> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    EmployeeDatabase employeeDatabase;
    DataAdapter adapter;
    RecyclerView recyclerView;
    EmployeeForm employeeForm;
    //Firebase instance;
    private FirebaseAuth mAuth;

    //Firebase database reference
    DatabaseReference mReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDispEmployeeId = findViewById(R.id.tvDisEmployeeId);
        tvDispName = findViewById(R.id.tvDisName);
        tvDispSalary = findViewById(R.id.tvDisSalary);
        tvDispRole = findViewById(R.id.tvDisRole);
        tvDispCompanyName = findViewById(R.id.tvDisCompanyName);
        btnSort = findViewById(R.id.btnSorting);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerList);

        //database reference instance
        mReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize database
        employeeDatabase = EmployeeDatabase.getDatabase(this);
        // store database values in data list
        dataList = employeeDatabase.employeeDao().getAll();

        // Initialize linear layout manager and set adapter
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DataAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        //sort the data by clicking sort button
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortData();
            }
        });

        // A floating button used to add employee data to database
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling activity to fill form in employers data in EmployeeForm activity
                Intent intent = new Intent(MainActivity.this, EmployeeForm.class);
                startActivityForResult(intent, EDIT_EMPLOYEE_REQUEST_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_EMPLOYEE_REQUEST_CODE && resultCode == RESULT_OK) {

            EmployeeTable table = new EmployeeTable();

            String employeeName = data.getStringExtra("name");
            String employeeSalary = data.getStringExtra("salary");
            String employeeRole = data.getStringExtra("role");
            String employeeCompanyName = data.getStringExtra("companyName");

            table.setName(employeeName);
            table.setSalary(employeeSalary);
            table.setRole(employeeRole);
            table.setCompanyName(employeeCompanyName);

            dataList.add(table);
            adapter = new DataAdapter(MainActivity.this, dataList);
            recyclerView.setAdapter(adapter);

            //adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.notifyDataSetChanged();
    }

    // to sort data
    private void sortData() {
        // Initialize pop menu and display various sort operations
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnSort);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ascName:
                        // add a feature to sort the data
                        dataList = employeeDatabase.employeeDao().getPersonsSortByASCName();
                        adapter = new DataAdapter(MainActivity.this, dataList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case R.id.dscName:
                        // add a feature to sort the data
                        dataList = employeeDatabase.employeeDao().getPersonsSortByDESCName();
                        adapter = new DataAdapter(MainActivity.this, dataList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case R.id.ascSalary:
                        // add a feature to sort the data
                        dataList = employeeDatabase.employeeDao().getPersonsSortByASCSalary();
                        adapter = new DataAdapter(MainActivity.this, dataList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case R.id.dscSalary:
                        dataList = employeeDatabase.employeeDao().getPersonsSortByDESCSalary();
                        adapter = new DataAdapter(MainActivity.this, dataList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case R.id.reset:
                        dataList = employeeDatabase.employeeDao().getAll();
                        adapter = new DataAdapter(MainActivity.this, dataList);
                        recyclerView.setAdapter(adapter);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signOut:
                mAuth.signOut();
                Toast.makeText(this, "LOGOUT", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}