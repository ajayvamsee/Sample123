package com.example.sample1.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sample1.View.MainActivity;
import com.example.sample1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText etMail, etPassword;
    Button btnLogin, btnSignUp;

    private FirebaseAuth mAuth;

    ProgressBar progressBar;

    // one boolean variable to check whether all the text fields
    // are filled by the user, properly or not.
   // boolean isAllFieldsChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //creating hooks for UI components
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        progressBar=findViewById(R.id.progressBar);

        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);

        // for login user by clicking the login button in UI
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                loginUser();

            }
        });

        // for user registration by clicking the signUp button in UI
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.GONE);

                // navigate into register page to register user
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

            }
        });


    }

    private void loginUser() {

        String email, password;
        email = etMail.getText().toString();
        password = etPassword.getText().toString();

        // store the returned value of the dedicated function which checks
        // whether the entered data is valid or if any fields are left blank.
       // isAllFieldsChecked = CheckAllFields();

        if(checkUserName(email) || checkPassword(password)){
            // sign in existing user
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                "Login successful!!",
                                Toast.LENGTH_LONG)
                                .show();
                        progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    } else {

                        progressBar.setVisibility(View.GONE);

                        // sign-in failed
                        Toast.makeText(getApplicationContext(),"Login failed!!",Toast.LENGTH_LONG).show();


                    }

                }
            });


        }
        else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "login not correct", Toast.LENGTH_SHORT).show();
        }




        }




    private boolean checkUserName(String userName){
        if(TextUtils.isEmpty(userName)){
            etMail.setError("Enter Email properly");
            return false;
        }
        return false;
    }

    private boolean checkPassword(String password) {

        if(TextUtils.isEmpty(password)){
            etPassword.setError("Enter password");
            return false;
        }
        else if (password.length()<6||password.length()>10){
            etPassword.setError("Passowrd should be between 6 to 10 characters");
            return false;
        }
        return true;
    }
   /* private boolean checkAllFields() {

        String email, password;
        email = etMail.getText().toString();
        password = etPassword.getText().toString();

       if(email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()){
           etMail.setError("Enter valid email ..!");
           return false;
       }
       else {
           etMail.setError(null);

       }
       if(password.isEmpty() || password.length()<4 || password.length()>10){
           etPassword.setText("Required.!");
           return false;
       }
       else{
           etPassword.setText(null);

       }
       return true;

    }*/
}
