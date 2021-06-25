package com.example.sample1.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sample1.model.EmployeeTable;

import java.util.List;

@Dao
public interface EmployeeDao {
    // Insert data
    @Insert
    void insertDetails(EmployeeTable data);

    // get all data
    @Query("SELECT * FROM EMPLOYEEDETAILS")
    List<EmployeeTable> getAll();

    // Delete by specified row or column Query
    @Delete
    void delete(EmployeeTable data);

    // delete All query
    @Delete
    void reset(List<EmployeeTable> dataList);

    // update query
    @Update
    void update(EmployeeTable employeeTable);

    // Query to Employee Names in Ascending order
    @Query("SELECT * FROM EmployeeDetails ORDER BY Name ASC")
    List<EmployeeTable> getPersonsSortByASCName();

    // Query to Employee Names in Descending order
    @Query("SELECT * FROM EmployeeDetails ORDER BY Name DESC")
    List<EmployeeTable> getPersonsSortByDESCName();

    // Query to Employee Salary in Ascending order
    @Query("SELECT * FROM EmployeeDetails ORDER BY Salary ASC")
    List<EmployeeTable> getPersonsSortByASCSalary();

    // Query to Employee salary in Descending order
    @Query("SELECT * FROM EmployeeDetails ORDER BY Salary DESC")
    List<EmployeeTable> getPersonsSortByDESCSalary();

    @Query("delete from EmployeeDetails")
    void deleteAllData();

}
    /*
       @Query("UPDATE EmployeeDetails SET text = :sText WHERE ID = sID")
    void update(int sID,String sText);*/





