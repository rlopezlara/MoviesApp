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
    FirebaseAuth mAuth;
    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.regEmail.getText().toString();
                String password = binding.regPassword.getText().toString();
                String confirmPassword = binding.regPasswordTwo.getText().toString();

                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Register.this, "Password fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    binding.regPassword.setError("Password must be at least 6 characters");
                    return;
                }
                registerUser(email, password);
            }
        });
    }
    private void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Log.d("tag","createUserWithEmail:Success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "registerUser Pass." + user.getUid(), Toast.LENGTH_SHORT).show();

                            Intent intentObj = new Intent(getApplicationContext(), Login.class);
                            startActivity(intentObj);

                        }else{
                            Log.d("Tag","createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Register failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}