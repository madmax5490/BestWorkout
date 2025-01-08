package com.example.bestworkout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private EditText rep_password;
    private Button registerButton;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Map<String, String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rep_password = findViewById(R.id.repeat_password);
        registerButton = findViewById(R.id.register_button);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String usersJson = sharedPreferences.getString("users", "{}");
        gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        users = gson.fromJson(usersJson, type);
    }

    public void registerButton(View view) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String rep_pass = rep_password.getText().toString();

        if (user.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Write username", Toast.LENGTH_SHORT).show();
        } else {
            if (users.containsKey(user)) {
                Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                if (pass.isEmpty() || rep_pass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Write password", Toast.LENGTH_SHORT).show();
                } else {
                    if (!pass.equals(rep_pass)) {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    } else {
                        users.put(user, pass);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("users", gson.toJson(users));
                        editor.apply();
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }

    public void returnButton(View view) {
        Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}