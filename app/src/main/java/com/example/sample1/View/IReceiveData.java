package com.example.sample1.View;

import com.example.sample1.model.EmployeeTable;

import java.util.List;

public interface IReceiveData {
    void getLatestUser(List<EmployeeTable> tableList);
}
