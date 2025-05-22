package com.example.bestworkout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        ImageButton backButton = findViewById(R.id.return_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(InstructionsActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        // Find all card views
        CardView runningCard = findViewById(R.id.running_card);
        CardView gymCard = findViewById(R.id.gym_card);
        CardView swimmongCard = findViewById(R.id.swimmiung_card);
        CardView cyclingCard = findViewById(R.id.cycling_card);
        CardView tennisCard = findViewById(R.id.tennis_card);
        CardView lifestyleCard = findViewById(R.id.lifestyle_card);

        // Set click listeners
        runningCard.setOnClickListener(v -> openWorkoutAdvice("running"));
        gymCard.setOnClickListener(v -> openWorkoutAdvice("gym"));
        swimmongCard.setOnClickListener(v -> openWorkoutAdvice("swimming"));
        cyclingCard.setOnClickListener(v -> openWorkoutAdvice("cycling"));
        tennisCard.setOnClickListener(v -> openWorkoutAdvice("tennis"));
        lifestyleCard.setOnClickListener(v -> openWorkoutAdvice("lifestyle"));
    }

    private void openWorkoutAdvice(String workoutType) {
        Intent intent = new Intent(this, WorkoutAdviceActivity.class);
        intent.putExtra("workout_type", workoutType);
        startActivity(intent);
    }
}