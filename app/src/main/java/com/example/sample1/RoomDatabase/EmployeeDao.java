package com.example.sample1.RoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmployeeDao {
    @Insert
    void insertDetails(EmployeeTable data);

    // get all data
    @Query("SELECT * FROM EMPLOYEEDETAILS")
    List<EmployeeTable> getAll();

    // Delete Query
    @Delete
    void delete(EmployeeTable data);

    // delete All query
    @Delete
    void reset(List<EmployeeTable> dataList);

    /*
        @Query("select * from EmployeeDetails")
    LiveData<List<EmployeeTable>> getDetails();

       @Query("UPDATE EmployeeDetails SET text = :sText WHERE ID = sID")
    void update(int sID,String sText);*/
/*
    @Query("delete from EmployeeDetails")
    void deleteAllData();*/

}
