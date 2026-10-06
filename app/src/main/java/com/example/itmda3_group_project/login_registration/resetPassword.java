package com.example.itmda3_group_project.login_registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.itmda3_group_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
