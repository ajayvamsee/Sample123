package com.example.sample1.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.sample1.RoomDatabase.EmployeeDao;
import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.model.EmployeeTable;

import java.util.List;

public class EmployeeRepository {
    private EmployeeDao employeeDao;

    private LiveData<List<EmployeeTable>> allData;

    public EmployeeRepository(Application application) {
        EmployeeDatabase db = EmployeeDatabase.getDatabase(application);
        employeeDao = db.employeeDao();


    }

    public void deleteData() {
        employeeDao.deleteAllData();
    }

    public LiveData<List<EmployeeTable>> getAllData() {
        return allData;
    }

    // to insert data
    public void insertData(EmployeeTable data) {
        new LoginInsertion(employeeDao).execute(data);
    }

    // to delete data
    public void deleteData(EmployeeTable data) {
        new DeleteAsyncTask(employeeDao).execute(data);
    }

    // to update data
    public void updateData(EmployeeTable data) {
        new UpdateAsyncTask(employeeDao).execute(data);
    }

    private static class LoginInsertion extends AsyncTask<EmployeeTable, Void, Void> {

        private EmployeeDao employeeDao;

        private LoginInsertion(EmployeeDao employeeDao) {
            this.employeeDao = employeeDao;
        }

        @Override
        protected Void doInBackground(EmployeeTable... employeeTables) {
            employeeDao.deleteAllData();
            employeeDao.insertDetails(employeeTables[0]);
            return null;
        }

    }

    private class DeleteAsyncTask extends AsyncTask<EmployeeTable, Void, Void> {

        private EmployeeDao employeeDao;

        public DeleteAsyncTask(EmployeeDao employeeDao) {
            this.employeeDao = employeeDao;
        }

        @Override
        protected Void doInBackground(EmployeeTable... employeeTables) {
            employeeDao.delete(employeeTables[0]);
            return null;
        }
    }

    private class UpdateAsyncTask extends AsyncTask<EmployeeTable, Void, Void> {

        private EmployeeDao employeeDao;

        public UpdateAsyncTask(EmployeeDao employeeDao) {
            this.employeeDao = employeeDao;
        }

        @Override
        protected Void doInBackground(EmployeeTable... employeeTables) {
            employeeDao.update(employeeTables[0]);
            return null;
        }
    }
}

