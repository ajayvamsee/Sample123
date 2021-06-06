package com.example.sample1.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sample1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText etUserName, etMobileNum, etPass, etRepass;
    Button btnSignUp;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = findViewById(R.id.etUserName);
        etMobileNum = findViewById(R.id.etMobileNum);
        etPass = findViewById(R.id.etpass);
        etRepass = findViewById(R.id.etRePass);
        btnSignUp = findViewById(R.id.btnSignUp);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {

        String username = etUserName.getText().toString().trim();
        String mobile = etMobileNum.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String rePass = etRepass.getText().toString().trim();

        if(validateName(username) && validatePhoneNo(mobile) && validatePassword(pass) && checkPassAndRepass(pass,rePass)){
            // create new user or register new User
            mAuth.createUserWithEmailAndPassword(username, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Register.this, "Registration failed!!", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }
        else {
            Toast.makeText(this, "check fields Properly", Toast.LENGTH_SHORT).show();
        }

    }
    // temp form validation not using right now we will this later
    // later we have to add the awesome pre-built libary to validate each and components
        private boolean validateForm(){

            if(TextUtils.isEmpty(etUserName.getText().toString().trim())){
                etUserName.setError("Required.!");
                return false;
            }
            else if(TextUtils.isEmpty(etPass.getText().toString().trim())){
                etPass.setText("Required.!");
                return false;
            }
            else if(TextUtils.isEmpty(etMobileNum.getText().toString().trim())){
                etPass.setText("Required.!");
                return false;
            }
            else if(TextUtils.isEmpty(etRepass.getText().toString().trim())){
                etPass.setError("Required");
                String pass = etPass.getText().toString().trim();
                String rePass = etRepass.getText().toString().trim();
                if(!(pass.equals(rePass))){
                    etRepass.setText("Password Not Matched..!");
                    return false;
                }

            }
            else{
                etUserName.setError(null);
                etPass.setText(null);
                etMobileNum.setText(null);
                etRepass.setText(null);
                return true;

            }

           return true;
        }



    // validation of all textFeilds seperated by  methods
    private boolean validateName(String name) {
        if (name.isEmpty()) {
            etUserName.setError("Field cannot be empty");
            return false;
        } else {
            etUserName.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNo(String phnNum) {

        if (phnNum.isEmpty()) { // need to implement moblie number excalty be 10 number  with condition && !(val.length()==10)
            etMobileNum.setError("Field cannot be empty");
            return false;
        }else if (phnNum.length()!=10){
            etMobileNum.setError("Enter 10 Numbers Properly");
            return false;
        }
        else {
            etMobileNum.setError(null);
            return true;
        }
    }

    private boolean checkPassAndRepass(String password,String rePassword) {

        return password.equals(rePassword);
    }

    private boolean validatePassword(String password) {

        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (password.isEmpty()) {
            etPass.setError("Field cannot be empty");
            return false;
        } else if (!password.matches(passwordVal)) {
            etPass.setError("Password is too weak");
            return false;
        } else {
            etPass.setError(null);
            return true;
        }

    }
}
