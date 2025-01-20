package com.example.bestworkout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {
    private EditText email;
    private EditText displayName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        email = findViewById(R.id.email);
        displayName = findViewById(R.id.display_name);
        Button updateButton = findViewById(R.id.update_button);

        if (user != null) {
            email.setText(user.getEmail());
            displayName.setText(user.getDisplayName());
        }

        updateButton.setOnClickListener(this::updateProfile);
    }

    private void updateProfile(View view) {
        String newEmail = email.getText().toString();
        String newDisplayName = displayName.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Email updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            });

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newDisplayName)
                    .build();

            user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}