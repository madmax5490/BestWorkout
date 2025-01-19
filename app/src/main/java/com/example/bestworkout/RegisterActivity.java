package com.example.bestworkout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText repPassword;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Map<String, String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        initializeSharedPreferences();
    }

    private void initializeViews() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repPassword = findViewById(R.id.repeat_password);
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(this::registerUser);
    }

    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String usersJson = sharedPreferences.getString("users", "{}");
        gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        users = gson.fromJson(usersJson, type);
    }

    private void registerUser(View view) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String repPass = repPassword.getText().toString();

        if (user.isEmpty()) {
            showToast("Write username");
        } else if (users.containsKey(user)) {
            showToast("Username already exists");
        } else if (pass.isEmpty() || repPass.isEmpty()) {
            showToast("Write password");
        } else if (!pass.equals(repPass)) {
            showToast("Passwords do not match");
        } else if (!isValidPassword(pass)) {
            showToast("Password must be at least 9 characters long and include both letters and numbers");
        } else {
            saveUser(user, pass);
            navigateToHome();
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

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveUser(String user, String pass) {
        users.put(user, pass);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("users", gson.toJson(users));
        editor.apply();
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