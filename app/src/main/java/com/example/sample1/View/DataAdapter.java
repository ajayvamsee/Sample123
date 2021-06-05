package com.example.sample1.View;

import android.app.Activity;
import android.text.AutoText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sample1.R;
import com.example.sample1.RoomDatabase.EmployeeDatabase;
import com.example.sample1.RoomDatabase.EmployeeTable;



import java.util.List;



public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    // Initilize variables
    private List<EmployeeTable> dataList;
    private Activity context;
    private EmployeeDatabase employeeDatabase;



    // create constructor
    public DataAdapter(Activity context,List<EmployeeTable> dataList){
        this.context=context;
        this.dataList=dataList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize view
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        // Initialize main data
        EmployeeTable employeeTable=dataList.get(position);

        //Initialize database
        employeeDatabase=EmployeeDatabase.getDatabase(context);



        // set text on textview
        holder.tvDisEmployeeId.setText("Employee ID: "+employeeTable.getId());
        holder.tvDisName.setText("Name: "+employeeTable.getName());
        holder.tvDisSalary.setText("Salary: "+employeeTable.getSalary());
        holder.tvDisRole.setText("Role: "+employeeTable.getRole());
        holder.tvDisCompanyName.setText("Company Name: "+employeeTable.getCompanyName());



        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Not implement yet to Update data", Toast.LENGTH_SHORT).show();
                // Initialize main data when any user update data
                // we need navigate to same employee form activity again Setback to Home screen with update data

                // some temporary code we will add this feature later

               /* employeeDatabase.employeeDao().update();
                dataList.clear();
                dataList.addAll(employeeDatabase.employeeDao().getAll());
                notifyDataSetChanged();*/
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Initialize variables
        TextView tvDisEmployeeId;
        TextView tvDisName;
        TextView tvDisSalary;
        TextView tvDisRole;
        TextView tvDisCompanyName;
        ImageView btnUpdate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Assign variables
            tvDisEmployeeId=itemView.findViewById(R.id.tvDisEmployeeId);
            tvDisName=itemView.findViewById(R.id.tvDisName);
            tvDisSalary=itemView.findViewById(R.id.tvDisSalary);
            tvDisRole=itemView.findViewById(R.id.tvDisRole);
            tvDisCompanyName=itemView.findViewById(R.id.tvDisCompanyName);
            btnUpdate=itemView.findViewById(R.id.btnUpdate);
        }
    }
}
