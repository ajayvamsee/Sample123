package com.example.sample1.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sample1.Repository.EmployeeRepository;
import com.example.sample1.RoomDatabase.EmployeeTable;

import java.util.List;

public class EmployeeViewModel extends AndroidViewModel {

    private EmployeeRepository repository;
    private LiveData<List<EmployeeTable>> getAllData;

    public EmployeeViewModel(@NonNull Application application) {
        super(application);


        repository = new EmployeeRepository(application);
        getAllData = repository.getAllData();
    }

    public void insert(EmployeeTable data) {
        repository.insertData(data);
    }

    public LiveData<List<EmployeeTable>> getGetAllData() {
        return getAllData;
    }
}
