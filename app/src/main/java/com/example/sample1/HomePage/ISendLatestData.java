package com.example.sample1.HomePage;

import com.example.sample1.model.EmployeeTable;

import java.util.List;

public interface ISendLatestData {
    void sendLatestUser(List<EmployeeTable> tableList);
}
