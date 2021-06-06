package com.example.sample1.View;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sample1.EmployeeForm;
import com.example.sample1.LoginRegister.Login;
import com.example.sample1.R;
import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.RoomDatabase.EmployeeTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;

    int REQUEST_CODE = 89;

    // Variable to display data on list of cardView
    TextView tvDispEmployeeId;
    TextView tvDispName;
    TextView tvDispSalary;
    TextView tvDispRole;
    TextView tvDispCompanyName;
    ImageView btnSort;


    //Firebase instance;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    RecyclerView recyclerView;

    List<EmployeeTable> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    EmployeeDatabase employeeDatabase;
    DataAdapter adapter;

    //Firebase database reference
    DatabaseReference mReference;

    // To calculate to number of employee details try to enter the form
    // we need to set for one user 10 employee has to data entry
    int count = 0;


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

        //database reference instance
        mReference = FirebaseDatabase.getInstance().getReference();//need to update


        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.recylerList);
        // Initialize database
        employeeDatabase = EmployeeDatabase.getDatabase(this);
        // store database values in data list
        dataList = employeeDatabase.employeeDao().getAll();
        // Initialize linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        //setLayout
        recyclerView.setLayoutManager(linearLayoutManager);
        // Initialize adapter
        adapter = new DataAdapter(MainActivity.this, dataList);
        //set Adapter
        recyclerView.setAdapter(adapter);


        //sort the data by clicking sort button
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortData();
            }
        });

        // A floating button used to add employee data to database
        // it will redirect into new Activity to store data
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling activity to fill form in employers data in EmployeeForm activity
                Intent intent = new Intent(MainActivity.this, EmployeeForm.class);
                startActivity(intent);

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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
        // inflate menu
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        // menu item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
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
                        //Toast.makeText(MainActivity.this, "Ascending Sort not implement yet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.dscSalary:
                        dataList = employeeDatabase.employeeDao().getPersonsSortByDESCSalary();
                        adapter = new DataAdapter(MainActivity.this, dataList);
                        recyclerView.setAdapter(adapter);
                        // Toast.makeText(MainActivity.this, "Ascending Sort not implement yet", Toast.LENGTH_SHORT).show();
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
        //we have created,initialized and inflated the  menu now and show it
        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}