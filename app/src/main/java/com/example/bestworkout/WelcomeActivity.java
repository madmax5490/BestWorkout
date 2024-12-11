package com.example.bestworkout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String usersJson = sharedPreferences.getString("users", "{}");
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        Map<String, String> users = gson.fromJson(usersJson, type);

        // Check if the user is already logged in
        String loggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (loggedInUser != null) {
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button logInButton = findViewById(R.id.enter_button);
        Button registerButton = findViewById(R.id.registration_button);

        logInButton.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (users.containsKey(user) && Objects.equals(users.get(user), pass)) {
                // Save the logged-in user
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("loggedInUser", user);
                editor.apply();

                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(WelcomeActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }
}