package com.example.bestworkout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class WorkoutAdviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_advice);

        // Get workout type from intent
        String workoutType = getIntent().getStringExtra("workout_type");
        if (workoutType == null) {
            workoutType = "running"; // Default
        }

        // Set title
        TextView titleView = findViewById(R.id.workout_title);
        titleView.setText(capitalizeFirstLetter(workoutType) + " Tips");

        // Setup ViewPager
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        WorkoutTipAdapter adapter = new WorkoutTipAdapter(this, getTipsForWorkout(workoutType));
        viewPager.setAdapter(adapter);

        // Setup indicator
        CircleIndicator3 indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        // Setup back button
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private List<WorkoutTip> getTipsForWorkout(String type) {
        List<WorkoutTip> tips = new ArrayList<>();

        switch (type) {
            case "running":
                tips.add(new WorkoutTip("Start with a proper warm-up of 5-10 minutes", R.drawable.running_warmup));
                tips.add(new WorkoutTip("Invest in good running shoes that match your foot type", R.drawable.runnig_shoes));
                tips.add(new WorkoutTip("Maintain proper posture: shoulders relaxed, slight forward lean", R.drawable.running_posture));
                tips.add(new WorkoutTip("Follow the 10% rule: don't increase weekly mileage by more than 10%", R.drawable.running_increase));
                tips.add(new WorkoutTip("Cool down properly and stretch after your run", R.drawable.running_stretch));
                break;
            case "gym":
                tips.add(new WorkoutTip("Focus on proper form rather than lifting heavy weights", R.drawable.gym_form));
                tips.add(new WorkoutTip("Follow a structured program that matches your goals", R.drawable.gym_programm));
                tips.add(new WorkoutTip("Give muscle groups 48 hours to recover between workouts", R.drawable.gym_recovery));
                tips.add(new WorkoutTip("Track your progress to stay motivated", R.drawable.gym_progress));
                tips.add(new WorkoutTip("Ensure you're eating enough protein to support muscle growth", R.drawable.gym_protein));
                break;
            case "swimming":
                tips.add(new WorkoutTip("Use proper breathing technique - exhale underwater, inhale above", R.drawable.swimming_breathing));
                tips.add(new WorkoutTip("Keep your body horizontal to reduce drag", R.drawable.swimming_pose));
                tips.add(new WorkoutTip("Start with shorter distances and build endurance gradually", R.drawable.swimming_endurance));
                tips.add(new WorkoutTip("Use swim goggles to protect your eyes", R.drawable.swimming_goggles));
                tips.add(new WorkoutTip("Practice different strokes to work different muscle groups", R.drawable.swimming_strokes));
                break;
            case "cycling":
                tips.add(new WorkoutTip("Ensure your bike is properly fitted to your body", R.drawable.cycling_pose));
                tips.add(new WorkoutTip("Wear a helmet and appropriate visibility gear", R.drawable.cycling_gear));
                tips.add(new WorkoutTip("Maintain a cadence of 80-100 RPM for efficiency", R.drawable.cycling_rpm));
                tips.add(new WorkoutTip("Shift gears before you need to, not during maximum effort", R.drawable.cycling_shiftgear));
                tips.add(new WorkoutTip("Hydrate regularly on longer rides", R.drawable.cycling_hydration));
                break;
            case "tennis":
                tips.add(new WorkoutTip("Focus on footwork - always be ready to move", R.drawable.tennis_move));
                tips.add(new WorkoutTip("Keep your eye on the ball until contact", R.drawable.tennis_ball));
                tips.add(new WorkoutTip("Follow through completely on your strokes", R.drawable.tennis_follow));
                tips.add(new WorkoutTip("Practice serves regularly - it's the only shot you control 100%", R.drawable.teninis_serve));
                tips.add(new WorkoutTip("Learn to read your opponent's body language for anticipation", R.drawable.tennis_bodylanguage));
                break;
            case "lifestyle":
                tips.add(new WorkoutTip("Aim for 7-9 hours of quality sleep each night", R.drawable.lifestyle_sleep));
                tips.add(new WorkoutTip("Stay hydrated by drinking at least 8 glasses of water daily", R.drawable.lifestyle_water));
                tips.add(new WorkoutTip("Include both cardio and strength training in your weekly routine", R.drawable.lifestyle_cardio));
                tips.add(new WorkoutTip("Practice stress-reduction techniques like meditation", R.drawable.lifestyle_meditations));
                tips.add(new WorkoutTip("Make physical activity part of your daily routine", R.drawable.lifestyle_regular));
                break;
        }

        return tips;
    }
}