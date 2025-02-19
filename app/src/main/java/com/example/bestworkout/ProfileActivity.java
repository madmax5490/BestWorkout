package com.example.bestworkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private EditText email, displayName, displayAge, displayWeight, displayHeight;
    private FirebaseAuth mAuth;
    private ArrayAdapter<CharSequence> adapter;
    private Spinner genderSpinner;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        email = findViewById(R.id.email);
        displayName = findViewById(R.id.display_name);
        displayAge = findViewById(R.id.display_age);
        displayWeight = findViewById(R.id.display_weight);
        displayHeight = findViewById(R.id.display_height);
        Button updateButton = findViewById(R.id.update_button);
        genderSpinner = findViewById(R.id.gender_spinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        if (user != null) {
            email.setText(user.getEmail());
            displayName.setText(user.getDisplayName());
            loadUserData(user.getUid());
        }

        updateButton.setOnClickListener(this::updateProfile);
    }

    private void loadUserData(String uid) {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                displayAge.setText(documentSnapshot.getString("age"));
                displayWeight.setText(documentSnapshot.getString("weight"));
                displayHeight.setText(documentSnapshot.getString("height"));
                String gender = documentSnapshot.getString("gender");
                if (gender != null) {
                    int spinnerPosition = adapter.getPosition(gender);
                    genderSpinner.setSelection(spinnerPosition);
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show());
    }

    private void updateProfile(View view) {
        String newEmail = email.getText().toString();
        String newDisplayName = displayName.getText().toString();
        String newAge = displayAge.getText().toString();
        String newWeight = displayWeight.getText().toString();
        String newHeight = displayHeight.getText().toString();
        String newGender = genderSpinner.getSelectedItem().toString();
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

            Map<String, Object> userData = new HashMap<>();
            userData.put("age", newAge);
            userData.put("weight", newWeight);
            userData.put("height", newHeight);
            userData.put("gender", newGender);

            db.collection("users").document(user.getUid()).set(userData, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "User data updated", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to update user data", Toast.LENGTH_SHORT).show());
        }
    }

    public void returnButton(View view) {
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}