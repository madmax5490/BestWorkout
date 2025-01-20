package com.example.bestworkout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText repPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        initializeFirebaseAuth();
    }

    private void initializeViews() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repPassword = findViewById(R.id.repeat_password);
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(this::registerUser);
    }

    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(View view) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String repPass = repPassword.getText().toString();

        if (user.isEmpty()) {
            showToast("Write username");
        } else if (pass.isEmpty() || repPass.isEmpty()) {
            showToast("Write password");
        } else if (!pass.equals(repPass)) {
            showToast("Passwords do not match");
        } else if (!isValidPassword(pass)) {
            showToast("Password must be at least 9 characters long and include both letters and numbers");
        } else {
            createUserInFirebase(user, pass);
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 9) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (hasLetter && hasDigit) {
                return true;
            }
        }
        return false;
    }

    private void createUserInFirebase(String user, String pass) {
        mAuth.createUserWithEmailAndPassword(user + "@example.com", pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            navigateToHome();
                        }
                    } else {
                        showToast("Registration failed: " + task.getException().getMessage());
                        Log.d("RegisterActivity.class", task.getException().getMessage());
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHome() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void returnButton(View view) {
        Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}