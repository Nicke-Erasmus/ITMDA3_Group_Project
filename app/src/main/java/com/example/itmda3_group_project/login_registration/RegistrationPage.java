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
import com.example.itmda3_group_project.UserHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationPage extends AppCompatActivity {

    Button btnRegister;
    EditText regUsername, regEmail, regUniversityName, regStudyPath, regPassword, regConfirmPassword;
    TextView regBackToLogin;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        //Initializing the database variables
        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        btnRegister = findViewById(R.id.btnRegister);
        regUsername = findViewById(R.id.etUsername);
        regEmail = findViewById(R.id.etEmail);
        regUniversityName = findViewById(R.id.etUniversity);
        regStudyPath = findViewById(R.id.etStudyPath);
        regPassword = findViewById(R.id.etPassword);
        regConfirmPassword = findViewById(R.id.etConfirmPassword);
        regBackToLogin = findViewById(R.id.tvLogin);

        //When button is clicked save entered data into firebase
    btnRegister.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            //Get all the values from the edit texts
            String username = regUsername.getText().toString().trim();
            String email = regEmail.getText().toString().trim();
            String universityName = regUniversityName.getText().toString().trim();
            String studyPath = regStudyPath.getText().toString().trim();
            String password = regPassword.getText().toString().trim();
            String confirmPassword = regConfirmPassword.getText().toString().trim();

            //Check if fields are empty
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegistrationPage.this, "Please fill in all required fields", Toast.LENGTH_LONG).show();
                return;
            }

            // Checks if passwords are the same
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegistrationPage.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                regPassword.setText("");
                regConfirmPassword.setText("");
                return;
            }

            //Create a new user using Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                 if (task.isSuccessful()) {
                    // Get the UID from Firebase Auth
                    String uid = mAuth.getCurrentUser().getUid();

                    UserHelperClass helperClass = new UserHelperClass(username, email, universityName, studyPath);

                    reference.child(uid).setValue(helperClass)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(RegistrationPage.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        startActivity(new Intent(RegistrationPage.this, LoginPage.class));
                        })
                    .addOnFailureListener(e -> {
                        Toast.makeText(RegistrationPage.this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                 } else {
                     //Handles duplicate emails and gives an appropriate response.
                   if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                       Toast.makeText(RegistrationPage.this, "This email is already registered.", Toast.LENGTH_LONG).show();
                       regEmail.setText("");
                    } else {
                        Toast.makeText(RegistrationPage.this, "Auth failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                 }
            });
        }
    });

        regBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }
}
