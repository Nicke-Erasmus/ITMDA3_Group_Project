package com.example.itmda3_group_project.login_registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itmda3_group_project.R;
import com.google.firebase.auth.FirebaseAuth;

public class resetPassword extends AppCompatActivity {

    Button btnReset, btnBack;
    EditText etForgetEmail;
    FirebaseAuth mAuth;
    String sEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        btnReset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btnBack);

        etForgetEmail = findViewById(R.id.etForgetEmail);

        mAuth = FirebaseAuth.getInstance();

        //Reset button listener
        btnReset.setOnClickListener(v -> {
            String email = etForgetEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter the email you used to create the account", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        //Back button code
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(resetPassword.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    private void ResetPassword(){

    }
}
