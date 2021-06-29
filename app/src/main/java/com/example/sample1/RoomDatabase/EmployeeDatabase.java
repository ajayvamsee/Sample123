package com.example.sample1.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sample1.model.EmployeeTable;

@Database(entities = EmployeeTable.class, version = 2, exportSchema = false)
public abstract class EmployeeDatabase extends RoomDatabase {

    public abstract EmployeeDao employeeDao();

    // create database instance
    private static EmployeeDatabase instance;


    public static EmployeeDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (EmployeeDatabase.class) {
                if (instance == null) {
                    //when instance equal to null create database
                    instance = Room.databaseBuilder(context,
                            EmployeeDatabase.class, "EMPLOYEE_DATABASE")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        // return database
        return instance;
    }


}
