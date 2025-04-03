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
import com.example.moviesapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Login activity for user authentication.
 * This class handles user login using Firebase Authentication.
 */
public class Login extends AppCompatActivity {
    // View binding for accessing UI elements
    ActivityLoginBinding binding;

    // Firebase authentication instance
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Navigate to the registration screen when 'Register Now' is clicked
        binding.registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(getApplicationContext(), Register.class);
                startActivity(intentObj);
                finish();
            }
        });
        // Handle login button click event
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                signIn(email, password);
            }
        });

    }
    // Authenticates the user using Firebase Authentication.
    private void signIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Authentication successful
                            Toast.makeText(Login.this, "Authentication passed", Toast.LENGTH_SHORT).show();
                            Intent intentObj = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intentObj);
                            finish();

                        }else{
                            // Authentication failed, log error
                            Log.d("Tag","createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}