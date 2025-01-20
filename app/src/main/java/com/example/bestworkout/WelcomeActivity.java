package com.example.bestworkout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import  android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button logInButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToHome();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        logInButton = findViewById(R.id.enter_button);

        setupLogInButton();
    }

    private void setupLogInButton() {
        logInButton.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            mAuth.signInWithEmailAndPassword(user + "@example.com", pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            navigateToHome();
                        } else {
                            Toast.makeText(WelcomeActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void registerButton(View view) {
        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}