package com.example.bestworkout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String loggedInUser = sharedPreferences.getString("loggedInUser", "User");

        TextView welcomeText = findViewById(R.id.welcome_text);
        welcomeText.setText("Welcome, " + loggedInUser + "!");
        //  logout process
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            // Clear logged-in user data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("loggedInUser");
            editor.apply();

            // Navigate to welcome screen
            Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}