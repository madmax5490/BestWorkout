package com.example.bestworkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        Button logoutButton = findViewById(R.id.logout_button);
        Button profileButton = findViewById(R.id.profile_button);

        logoutButton.setOnClickListener(this::logoutButton);
        profileButton.setOnClickListener(this::navigateToProfile);
    }

    public void logoutButton(View view) {
        mAuth.signOut();
        Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void navigateToProfile(View view) {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}