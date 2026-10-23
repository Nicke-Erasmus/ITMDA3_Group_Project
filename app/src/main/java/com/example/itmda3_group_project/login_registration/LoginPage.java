package com.example.itmda3_group_project.login_registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itmda3_group_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    Button btnLogin;
    EditText logEmail, logPassword;
    TextView tvResetPassword, tvRegister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        //Initializing firebase authentication
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginPage.this, Dashboard.class);
            startActivity(intent);
            finish();
            return;
        }

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        logEmail = findViewById(R.id.etEmail);
        logPassword = findViewById(R.id.etPassword);
        tvResetPassword = findViewById(R.id.tvResetPassword);

        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, resetPassword.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = logEmail.getText().toString().trim();
                String password = logPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Please enter email and password", Toast.LENGTH_LONG).show();
                    return;
                }

                //Sign into account using firebase authenticator
                mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LoginPage.this, Dashboard.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginPage.this, "Login failed: Incorrect Email or Password", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegistrationPage.class);
                startActivity(intent);
            }
        });
    }
}