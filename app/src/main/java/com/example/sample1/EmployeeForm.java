package com.example.sample1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.RoomDatabase.EmployeeTable;
import com.example.sample1.View.DataAdapter;
import com.example.sample1.View.MainActivity;
import com.example.sample1.ViewModel.EmployeeViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EmployeeForm extends AppCompatActivity {

    Spinner spinner;

    // add some employee role items into string array
    String[] employeeRoles = {"Select Role", "Jr.Android Developer ", "Android Developer", "Associate Android Developer",
            "Mobile Application Developer", "Android Engineer", "Sr.Android Developer",
            "Staff Software Engineer, Android", "Android Tech Lead", "Android Development Manager"};

    private EmployeeViewModel employeeViewModel;
    private Button btnSave;
    private EmployeeDatabase employeeDatabase;

    // EdiText feilds for employee form
    TextInputEditText name;
    TextInputEditText salary;
    TextInputEditText companyName;


    //public String role=null;

    List<EmployeeTable> dataList=new ArrayList<>();

    DataAdapter adapter;

    //Adding firebase
    FirebaseDatabase mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_form);

        spinner = findViewById(R.id.role);
        btnSave = findViewById(R.id.save);


        name = findViewById(R.id.employeeName);
        salary = findViewById(R.id.employeeSalary);
        companyName = findViewById(R.id.employeeCompanyName);

        // Initialize database
        employeeDatabase = EmployeeDatabase.getDatabase(this);
        dataList=employeeDatabase.employeeDao().getAll();

        // Initialize adapter
        adapter=new DataAdapter(EmployeeForm.this,dataList);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToRoomDB();
            }
        });

        // For Role of an employee to display in spinner using Array Of some Objects
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, employeeRoles);
        spinner.setAdapter(adapter);

       /* spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        role = null;
                        break;
                    case 1:
                        role = employeeRoles[1];
                        break;
                    case 2:
                        role = employeeRoles[2];
                        break;
                    case 3:
                        role = employeeRoles[3];
                        break;
                    case 4:
                        role = employeeRoles[4];
                        break;
                    case 5:
                        role = employeeRoles[5];
                        break;
                    case 6:
                        role = employeeRoles[6];
                        break;
                    case 7:
                        role = employeeRoles[7];
                        break;
                    case 8:
                        role = employeeRoles[8];
                        break;
                    case 9:
                        role = employeeRoles[9];
                        break;


                }
            }
        });*/


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
        } else if (TextUtils.isEmpty(employeeCompanyName)) {
            companyName.setError("Please Enter Your employeeCompanyName");
        } else {

            // passing data to EmployeeTable
            data.setName(employeeName);
            data.setSalary(employeeSalary);
            //  data.setRole(role);
            data.setCompanyName(employeeCompanyName);

            // inserting text to database
            employeeDatabase.employeeDao().insertDetails(data);
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