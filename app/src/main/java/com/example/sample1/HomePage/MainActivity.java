package com.example.sample1.HomePage;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sample1.AddData.EmployeeAddForm;
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

public class MainActivity extends AppCompatActivity implements DataAdapter.OnDeleteClickListener {

    private static final int ADD_EMPLOYEE_REQUEST_CODE = 10;
    public EmployeeViewModel employeeViewModel;
    public List<EmployeeTable> dataList;
    public EmployeeDatabase employeeDatabase;
    private TextView tvDispEmployeeId;
    private TextView tvDispName;
    private TextView tvDispSalary;
    private TextView tvDispRole;
    private TextView tvDispCompanyName;
    private ImageView btnSort;
    private FloatingActionButton fab;
    private LinearLayoutManager linearLayoutManager;
    private DataAdapter adapter;
    private RecyclerView recyclerView;
    private EmployeeAddForm employeeAddForm;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        tvDispEmployeeId = findViewById(R.id.tvDisEmployeeId);
        tvDispName = findViewById(R.id.tvDisName);
        tvDispSalary = findViewById(R.id.tvDisSalary);
        tvDispRole = findViewById(R.id.tvDisRole);
        tvDispCompanyName = findViewById(R.id.tvDisCompanyName);
        btnSort = findViewById(R.id.btnSorting);
        recyclerView = findViewById(R.id.recyclerList);
        dataList = new ArrayList<>();

        //veiewModelProviders
        employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        dbSetUp();

        setUpRecyclerView();

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
                // calling activity to fill form in employers data in EmployeeAddForm activity
                Intent intent = new Intent(MainActivity.this, EmployeeAddForm.class);
                startActivityForResult(intent, ADD_EMPLOYEE_REQUEST_CODE);
            }
        });


    }

    private void dbSetUp() {
        //Database reference instance
        mReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Initialize database
        employeeDatabase = EmployeeDatabase.getDatabase(this);

        //store database values in data list
        dataList = employeeDatabase.employeeDao().getAll();
    }

    private void setUpRecyclerView() {
        //Initialize linear layout manager and set adapter
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DataAdapter(MainActivity.this, dataList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EMPLOYEE_REQUEST_CODE && resultCode == RESULT_OK) {

            EmployeeTable table = new EmployeeTable();

            String employeeName = data.getStringExtra("name");
            String employeeSalary = data.getStringExtra("salary");
            String employeeRole = data.getStringExtra("role");
            String employeeCompanyName = data.getStringExtra("companyName");

            table.setName(employeeName);
            table.setSalary(employeeSalary);
            table.setRole(employeeRole);
            table.setCompanyName(employeeCompanyName);

            //dataList.add(table);

            employeeDatabase.employeeDao().insertDetails(table);
            dataList = employeeDatabase.employeeDao().getAll();
            adapter = new DataAdapter(MainActivity.this, dataList, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.notifyDataSetChanged();
    }

    // to sort data
    @SuppressLint("NonConstantResourceId")
    private void sortData() {
        // Initialize pop menu and display various sort operations
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnSort);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
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


    @Override
    public void onDeleteClickListener(EmployeeTable employeeTable, int position) {

        // code to delete
        employeeDatabase.employeeDao().delete(employeeTable);
        dataList = employeeDatabase.employeeDao().getAll();
        adapter = new DataAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

}