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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText rep_password = findViewById(R.id.repeat_password);
        Button registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            String rep_pass = rep_password.getText().toString();

            if (pass.equals(rep_pass)) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String usersJson = sharedPreferences.getString("users", "{}");
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<String, String>>() {}.getType();
                Map<String, String> users = gson.fromJson(usersJson, type);

                if (users.containsKey(user)) {
                    Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    users.put(user, pass);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("users", gson.toJson(users));
                    editor.apply();

                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
    }
}