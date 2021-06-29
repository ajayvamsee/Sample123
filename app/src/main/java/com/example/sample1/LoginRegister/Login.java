package com.example.sample1.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sample1.R;
import com.example.sample1.HomePage.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText etMail, etPassword;
    Button btnLogin, btnSignUp;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //creating hooks for UI components
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        progressBar = findViewById(R.id.progressBar);

        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //taking instance of FirebaseUser
        user = FirebaseAuth.getInstance().getCurrentUser();


        progressBar.setVisibility(View.GONE);

        // for login user by clicking the login button in UI
        btnLogin.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);

            loginUser();

        });

        // for user registration by clicking the signUp button in UI
        btnSignUp.setOnClickListener(v -> {

            progressBar.setVisibility(View.GONE);

            // navigate into register page to register user
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);

        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        keepUserLogin();
    }

    private void keepUserLogin() {
        progressBar.setVisibility(View.VISIBLE);
        if (user != null) {

            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "UserLogout", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser() {

        String email, password;
        email = etMail.getText().toString();
        password = etPassword.getText().toString();

        if (checkUserName(email) || checkPassword(password)) {
            // sign in existing user
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(Login.this, MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    progressBar.setVisibility(View.GONE);
                    // sign-in failed
                    Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();
                }

            });


        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "login not correct", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean checkUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            etMail.setError("Enter Email properly");
            return false;
        }
        return false;
    }

    private boolean checkPassword(String password) {

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter password");
            return false;
        } else if (password.length() < 6 || password.length() > 10) {
            etPassword.setError("Password should be between 6 to 10 characters");
            return false;
        }
        return true;
    }

}
