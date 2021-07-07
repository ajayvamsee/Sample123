package com.example.sample1.AddData;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sample1.R;
import com.example.sample1.Repository.EmployeeRepository;
import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.ViewModel.EmployeeViewModel;
import com.example.sample1.adapter.DataAdapter;
import com.example.sample1.model.EmployeeTable;
import com.example.sample1.utils.ApplicationClass;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class EmployeeUpdateForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputEditText name;
    private TextInputEditText salary;
    private TextInputEditText companyName;
    private Spinner spinner;
    private Button btnUpdate;

    private String employeeName;
    private String employeeSalary;
    private String role;
    private String employeeCompanyName;

    private List<EmployeeTable> dataList;
    private EmployeeTable employeeTable;
    private DataAdapter adapter;

    private EmployeeDatabase employeeDatabase;
    private EmployeeViewModel employeeViewModel;
    private EmployeeRepository employeeRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_update_form);

        name = findViewById(R.id.employeeName);
        salary = findViewById(R.id.employeeSalary);
        companyName = findViewById(R.id.employeeCompanyName);
        spinner = findViewById(R.id.role);
        btnUpdate = findViewById(R.id.save);

        employeeTable = dataList.get(0);


        arrayAdapterSetUp();

        adapterSetUp();

        //dbInitialize();

        // Initialize database
        employeeDatabase = EmployeeDatabase.getDatabase(this);
        dataList = employeeDatabase.employeeDao().getAll();


        btnUpdate.setOnClickListener(v -> {

            employeeName = name.getText().toString().trim();
            employeeSalary = salary.getText().toString().trim();
            employeeCompanyName = companyName.getText().toString().trim();

            updateDataToRoomDB(employeeName, employeeSalary, employeeCompanyName);

            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        });


    }

    private void updateDataToRoomDB(String employeeName, String employeeSalary, String employeeCompanyName) {

    }

    private void arrayAdapterSetUp() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, ApplicationClass.employeeRoles);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Select Role");
        spinner.setOnItemSelectedListener(this);
    }

    private void adapterSetUp() {
        // Initialize adapter
        adapter = new DataAdapter(EmployeeUpdateForm.this, dataList);
    }

    private void dbInitialize() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        role = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        role = null;
    }
}