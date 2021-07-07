package com.example.sample1.AddData;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.sample1.R;
import com.example.sample1.Repository.EmployeeRepository;
import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.ViewModel.EmployeeViewModel;
import com.example.sample1.adapter.DataAdapter;
import com.example.sample1.model.EmployeeTable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAddForm extends AppCompatActivity {


    TextInputEditText name;
    TextInputEditText salary;
    TextInputEditText companyName;
    Button btnSave;

    public ArrayAdapter arrayAdapter;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autoCompleteTextView;

    String employeeName;
    String employeeSalary;
    String role;
    String employeeCompanyName;

    List<EmployeeTable> dataList;
    DataAdapter adapter;

    EmployeeViewModel employeeViewModel;
    EmployeeDatabase employeeDatabase;
    EmployeeRepository employeeRepository;

    //Adding firebase
    DatabaseReference mReference;
    Long maxId;

    // add some employee role items into string array
    String[] employeeRoles = {" ", "Jr.Android Developer ", "Android Developer", "Associate Android Developer",
            "Mobile Application Developer", "Android Engineer", "Sr.Android Developer",
            "Staff Software Engineer, Android", "Android Tech Lead", "Android Development Manager"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_form);

        name = findViewById(R.id.employeeName);
        salary = findViewById(R.id.employeeSalary);
        companyName = findViewById(R.id.employeeCompanyName);
        btnSave = findViewById(R.id.save);
        textInputLayout = findViewById(R.id.roleMenu);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        dataList = new ArrayList<>();

        employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        fbInstance();

        dbInitialize();

        adapterSetUp();


        String[] rolesItems = getResources().getStringArray(R.array.roles);

        arrayAdapter = new ArrayAdapter(this, R.layout.drop_down_layout, rolesItems);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                role = String.valueOf(parent.getItemAtPosition(position));
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                employeeName = name.getText().toString().trim();
                employeeSalary = salary.getText().toString().trim();
                employeeCompanyName = companyName.getText().toString().trim();

                // saving data to room
                saveDataToRoomDB(employeeName, employeeSalary, employeeCompanyName);
            }
        });


    }

    private void adapterSetUp() {
        // Initialize adapter
        adapter = new DataAdapter(EmployeeAddForm.this, dataList);
    }

    private void dbInitialize() {
        // Initialize database
        employeeDatabase = EmployeeDatabase.getDatabase(this);
        dataList = employeeDatabase.employeeDao().getAll();
    }

    private void fbInstance() {
        // firebase database instance
        mReference = FirebaseDatabase.getInstance().getReference().child("EmployeeTable");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveDataToRoomDB(String employeeName, String employeeSalary, String employeeCompanyName) {

        // instance of Employee Table class
        EmployeeTable employeeTable = new EmployeeTable();
        Intent returnIntent = new Intent();

        // form validation
        if (TextUtils.isEmpty(employeeName)) {
            name.setError("Please Enter Your employeeName");
        } else if (TextUtils.isEmpty(employeeSalary)) {
            salary.setError("Please Enter Your employeeSalary");
        } else if (TextUtils.isEmpty(role)) {
            autoCompleteTextView.setError("Please Select Role");
        } else if (TextUtils.isEmpty(employeeCompanyName)) {
            companyName.setError("Please Enter Your employeeCompanyName");
        } else {

            //sending data to MainActivity to display data
            returnIntent.putExtra("name", employeeName);
            returnIntent.putExtra("salary", employeeSalary);
            returnIntent.putExtra("role", role);
            returnIntent.putExtra("companyName", employeeCompanyName);
            setResult(RESULT_OK, returnIntent);

            // passing data to EmployeeTable
            employeeTable.setName(employeeName);
            employeeTable.setSalary(employeeSalary);
            employeeTable.setRole(role);
            employeeTable.setCompanyName(employeeCompanyName);

            // inserting data to firebase
            mReference.child(String.valueOf(maxId)).setValue(employeeTable);
            Toast.makeText(this, "Successfully Stored", Toast.LENGTH_SHORT).show();


            //clear edit text
            name.setText("");
            salary.setText("");
            companyName.setText("");

            //Notify when data is inserted
            dataList.clear();
            dataList.addAll(employeeDatabase.employeeDao().getAll());
            adapter.notifyDataSetChanged();
        }


    }
}

