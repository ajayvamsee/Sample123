package com.example.sample1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sample1.R;
import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.model.EmployeeTable;

import java.util.List;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    // Initialize variables
    public List<EmployeeTable> dataList;
    public Activity context;
    public OnDeleteClickListener onDeleteClickListener;
    public EmployeeDatabase employeeDatabase;

    public DataAdapter(Activity context, List<EmployeeTable> dataList, OnDeleteClickListener onDeleteClickListener) {
        this.context = context;
        this.dataList = dataList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    // create constructor
    public DataAdapter(Activity context, List<EmployeeTable> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Initialize main data
        EmployeeTable employeeTable = dataList.get(position);
        //Initialize database
        employeeDatabase = EmployeeDatabase.getDatabase(context);


        // set text on textview
        holder.tvDisEmployeeId.setText("Employee ID: " + employeeTable.getId());
        holder.tvDisName.setText("Name: " + employeeTable.getName());
        holder.tvDisSalary.setText("Salary: " + employeeTable.getSalary());
        holder.tvDisRole.setText("Role: " + employeeTable.getRole());
        holder.tvDisCompanyName.setText("Company Name: " + employeeTable.getCompanyName());


        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Not implement yet to Update data", Toast.LENGTH_SHORT).show();
                // Initialize main data when any user update data
                // we need navigate to same employee form activity again Setback to Home screen with update data
               /* employeeDatabase.employeeDao().update();
                dataList.clear();
                dataList.addAll(employeeDatabase.employeeDao().getAll());
                notifyDataSetChanged();*/

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClickListener(dataList.get(position), position);
                }

                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view);
    }

    // interface to send data to EmployeeAddForm activity
    public interface OnDeleteClickListener {
        void onDeleteClickListener(EmployeeTable employeeTable, int position);
    }
    /*// interface to send data to EmployeeUpdateForm activity
    public interface OnUpdateClickListener{
        void onUpdateData(EmployeeTable employeeTable, int position);
    }*/

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Initialize variables
        TextView tvDisEmployeeId;
        TextView tvDisName;
        TextView tvDisSalary;
        TextView tvDisRole;
        TextView tvDisCompanyName;
        ImageView btnUpdate;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Assign variables
            tvDisEmployeeId = itemView.findViewById(R.id.tvDisEmployeeId);
            tvDisName = itemView.findViewById(R.id.tvDisName);
            tvDisSalary = itemView.findViewById(R.id.tvDisSalary);
            tvDisRole = itemView.findViewById(R.id.tvDisRole);
            tvDisCompanyName = itemView.findViewById(R.id.tvDisCompanyName);
            btnUpdate = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }


}
