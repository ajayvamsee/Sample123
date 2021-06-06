package com.example.sample1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.RoomDatabase.EmployeeTable;
import com.example.sample1.View.DataAdapter;
import com.example.sample1.ViewModel.EmployeeViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    EmployeeViewModel employeeViewModel;
    Button btnSave;
    EmployeeDatabase employeeDatabase;
    // EdiText feilds for employee form
    TextInputEditText name;
    TextInputEditText salary;
    TextInputEditText companyName;
    String role;
    static List<EmployeeTable> dataList = new ArrayList<>();
    IUserRecyclerView mListener;
    DataAdapter adapter;
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

        spinner = findViewById(R.id.role);
        btnSave = findViewById(R.id.save);


        name = findViewById(R.id.employeeName);
        salary = findViewById(R.id.employeeSalary);
        companyName = findViewById(R.id.employeeCompanyName);


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

        // Initialize database
        employeeDatabase = EmployeeDatabase.getDatabase(this);
        dataList = employeeDatabase.employeeDao().getAll();
        // Initialize adapter
        adapter = new DataAdapter(EmployeeForm.this, dataList);
        // For Role of an employee to display in spinner using Array Of some Objects
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, employeeRoles);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Select Role");
        spinner.setOnItemSelectedListener(this);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // saving data to room
                saveDataToRoomDB();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        role = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        role = null;

    }

    private void saveDataToRoomDB() {

        String employeeName = name.getText().toString().trim();
        String employeeSalary = salary.getText().toString().trim();
        String employeeCompanyName = companyName.getText().toString().trim();


        // instance of Employee Table class
        EmployeeTable data = new EmployeeTable();

        // form validation
        if (TextUtils.isEmpty(employeeName)) {
            name.setError("Please Enter Your employeeName");
        } else if (TextUtils.isEmpty(employeeSalary)) {
            salary.setError("Please Enter Your employeeSalary");
        } else if (TextUtils.isEmpty(role)) {
            companyName.setError("Please Select Role");
        } else if (TextUtils.isEmpty(employeeCompanyName)) {
            companyName.setError("Please Enter Your employeeCompanyName");
        } else {

            // passing data to EmployeeTable
            data.setName(employeeName);
            data.setSalary(employeeSalary);
            data.setRole(role);
            data.setCompanyName(employeeCompanyName);

            // inserting text to room database
            employeeDatabase.employeeDao().insertDetails(data);


            // inserting data to firebase
            mReference.child(String.valueOf(maxId)).setValue(data);//need to work

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

    public interface IUserRecyclerView {
        void getLatestUser(List<EmployeeTable> dataList);
    }
}