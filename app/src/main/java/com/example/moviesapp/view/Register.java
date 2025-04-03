package com.example.moviesapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviesapp.R;
import com.example.moviesapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    // Firebase Authentication instance
    FirebaseAuth mAuth;
    // View binding for activity layout
    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize view binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Set click listener for Register button
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input from EditText fields
                String email = binding.regEmail.getText().toString();
                String password = binding.regPassword.getText().toString();
                String confirmPassword = binding.regPasswordTwo.getText().toString();
                // Validate password fields
                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Register.this, "Password fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check password length requirement
                if (password.length() < 6) {
                    binding.regPassword.setError("Password must be at least 6 characters");
                    return;
                }
                // Call method to register user
                registerUser(email, password);
            }
        });
    }
        // Registers a new user with Firebase Authentication.
    private void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            // Registration successful, get the current user
                            Log.d("tag","createUserWithEmail:Success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "registerUser Pass." + user.getUid(), Toast.LENGTH_SHORT).show();
                            // Redirect user to Login screen
                            Intent intentObj = new Intent(getApplicationContext(), Login.class);
                            startActivity(intentObj);

                        }else{
                            // Registration failed, log the error and notify the user
                            Log.d("Tag","createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Register failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}