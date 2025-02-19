package com.example.bestworkout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView workoutList;
    private WorkoutAdapter workoutAdapter;
    private ArrayList<Workout> workouts;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        workouts = new ArrayList<>();

        workoutList = findViewById(R.id.workout_list);
        workoutList.setLayoutManager(new LinearLayoutManager(this));
        workoutAdapter = new WorkoutAdapter(workouts);
        workoutList.setAdapter(workoutAdapter);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ImageButton openDrawerButton = findViewById(R.id.open_drawer_button);

        openDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            final int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            } else if (itemId == R.id.nav_settings) {
                // Handle settings action
            } else if (itemId == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
                finish();
            }
            drawerLayout.closeDrawer(navigationView);
            return true;
        });

        loadWorkouts();
    }

    public void showAddWorkoutDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_workout, null);
        builder.setView(dialogView);

        EditText workoutType = dialogView.findViewById(R.id.workout_type);
        EditText workoutTime = dialogView.findViewById(R.id.workout_time);
        EditText workoutDays = dialogView.findViewById(R.id.workout_days);
        Button saveButton = dialogView.findViewById(R.id.save_button);

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            String type = workoutType.getText().toString();
            String time = workoutTime.getText().toString();
            String days = workoutDays.getText().toString();
            saveWorkout(type, time, days);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void saveWorkout(String type, String time, String days) {
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> workout = new HashMap<>();
        workout.put("type", type);
        workout.put("time", time);
        workout.put("days", days);

        db.collection("users").document(userId).collection("workouts").add(workout)
                .addOnSuccessListener(documentReference -> {
                    workouts.add(new Workout(type, time, days));
                    workoutAdapter.notifyDataSetChanged();
                    Toast.makeText(HomeActivity.this, "Workout scheduled", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Failed to schedule workout", Toast.LENGTH_SHORT).show());
    }

    private void loadWorkouts() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("workouts").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (var document : queryDocumentSnapshots) {
                        String type = document.getString("type");
                        String time = document.getString("time");
                        String days = document.getString("days");
                        workouts.add(new Workout(type, time, days));
                    }
                    workoutAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Failed to load workouts", Toast.LENGTH_SHORT).show());
    }


//    public void navigateToProfile(View view) {
//        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
//        startActivity(intent);
//        finish();
//    }

//    public void logoutButton(View view) {
//        Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
//        FirebaseUser currentUser = null;
//        startActivity(intent);
//        finish();
//    }
}