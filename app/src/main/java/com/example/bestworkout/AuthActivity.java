// AuthActivity.java
package com.example.bestworkout;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (savedInstanceState == null) {
            // Load WelcomeFragment by default
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new WelcomeFragment())
                    .commit();
        }


    }

    // Method to navigate from welcome to register
    public void navigateToRegister() {
        RegisterFragment registerFragment = new RegisterFragment();

        // Replace the current fragment with RegisterFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, registerFragment)
                .addToBackStack(null)
                .commit();
    }

    // Method to navigate from register to welcome
    public void navigateToWelcome() {
        getSupportFragmentManager().popBackStack();
    }

//    public void navigateToHome() {
//        Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
//        startActivity(intent);
//        finish();
//    }
}