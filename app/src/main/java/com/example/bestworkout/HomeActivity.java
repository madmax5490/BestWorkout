package com.example.bestworkout;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.os.Build;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView workoutList;
    private WorkoutAdapter workoutAdapter;
    private ArrayList<Workout> workouts;
    private DrawerLayout drawerLayout;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        workouts = new ArrayList<>();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //
        // Инициализация RecyclerView
        workoutList = findViewById(R.id.workout_list);
        workoutList.setLayoutManager(new LinearLayoutManager(this));
        workoutAdapter = new WorkoutAdapter(workouts);
        workoutAdapter.setOnDeleteClickListener(position -> {
            showDeleteWorkoutDialog(position);
        });
        workoutList.setAdapter(workoutAdapter);

        // Настройка бокового меню
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ImageButton openDrawerButton = findViewById(R.id.open_drawer_button);

        openDrawerButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(navigationView);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            final int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            } else if (itemId == R.id.nav_settings) {
                // Обработка настроек
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

    private void showDeleteWorkoutDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Workout")
                .setMessage("Are you sure you want to delete this workout?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteWorkout(position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteWorkout(int position) {
        String userId = mAuth.getCurrentUser().getUid();

        // Получаем данные тренировки для поиска в Firestore
        Workout workout = workouts.get(position);
        String type = workout.getType();
        String time = workout.getTime();
        String days = workout.getDays();

        cancelWorkoutAlarm(type, time, days);

        // Запрос на удаление тренировки
        db.collection("users").document(userId).collection("workouts")
                .whereEqualTo("type", type)
                .whereEqualTo("time", time)
                .whereEqualTo("days", days)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        querySnapshot.getDocuments().get(0).getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    workouts.remove(position);
                                    workoutAdapter.notifyItemRemoved(position);
                                    Toast.makeText(HomeActivity.this, "Workout deleted", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(HomeActivity.this, "Failed to delete workout", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(HomeActivity.this, "Failed to find workout", Toast.LENGTH_SHORT).show());
    }
    public void showAddWorkoutDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_workout, null);
        builder.setView(dialogView);

        Spinner workoutTypeSpinner = dialogView.findViewById(R.id.workout_type_spinner);
        Button timePickerButton = dialogView.findViewById(R.id.time_picker_button);
        TextView selectedTimeText = dialogView.findViewById(R.id.selected_time_text);

        CheckBox mondayCheckbox = dialogView.findViewById(R.id.monday);
        CheckBox tuesdayCheckbox = dialogView.findViewById(R.id.tuesday);
        CheckBox wednesdayCheckbox = dialogView.findViewById(R.id.wednesday);
        CheckBox thursdayCheckbox = dialogView.findViewById(R.id.thursday);
        CheckBox fridayCheckbox = dialogView.findViewById(R.id.friday);
        CheckBox saturdayCheckbox = dialogView.findViewById(R.id.saturday);
        CheckBox sundayCheckbox = dialogView.findViewById(R.id.sunday);

        Button saveButton = dialogView.findViewById(R.id.save_button);

        // Variables to store selected time
        final int[] selectedHour = {0};
        final int[] selectedMinute = {0};
        final boolean[] timeSelected = {false};

        // Time picker button setup
        timePickerButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    HomeActivity.this,
                    (timePicker, hour, minute) -> {
                        selectedHour[0] = hour;
                        selectedMinute[0] = minute;
                        timeSelected[0] = true;
                        selectedTimeText.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    },
                    selectedHour[0], selectedMinute[0], true);
            timePickerDialog.show();
        });

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            // Check if time is selected
            if (!timeSelected[0]) {
                Toast.makeText(HomeActivity.this, "Please select workout time", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build days string
            StringBuilder daysBuilder = new StringBuilder();
            if (mondayCheckbox.isChecked()) daysBuilder.append("Mon,");
            if (tuesdayCheckbox.isChecked()) daysBuilder.append("Tue,");
            if (wednesdayCheckbox.isChecked()) daysBuilder.append("Wed,");
            if (thursdayCheckbox.isChecked()) daysBuilder.append("Thu,");
            if (fridayCheckbox.isChecked()) daysBuilder.append("Fri,");
            if (saturdayCheckbox.isChecked()) daysBuilder.append("Sat,");
            if (sundayCheckbox.isChecked()) daysBuilder.append("Sun,");

            // Check if at least one day is selected
            if (daysBuilder.length() == 0) {
                Toast.makeText(HomeActivity.this, "Please select at least one day", Toast.LENGTH_SHORT).show();
                return;
            }

            // Remove last comma
            if (daysBuilder.length() > 0) {
                daysBuilder.deleteCharAt(daysBuilder.length() - 1);
            }

            String type = workoutTypeSpinner.getSelectedItem().toString();
            String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour[0], selectedMinute[0]);
            String days = daysBuilder.toString();

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

                    scheduleWorkoutAlarm(type, time, days);

                    Toast.makeText(HomeActivity.this, "Workout is scheduled", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this,
                        "Failed to schedule workout", Toast.LENGTH_SHORT).show());
    }

    private void loadWorkouts() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("workouts").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    workouts.clear();
                    for (var document : queryDocumentSnapshots) {
                        String type = document.getString("type");
                        String time = document.getString("time");
                        String days = document.getString("days");
                        workouts.add(new Workout(type, time, days));
                    }
                    workoutAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this,
                        "Failed to load workouts", Toast.LENGTH_SHORT).show());
    }

    // Метод для установки будильника для уведомлений о тренировке
    private void scheduleWorkoutAlarm(String type, String time, String days) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent);
                    Toast.makeText(this, "Please, allow precise alarms", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            // Парсим время тренировки
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            // Для каждого выбранного дня устанавливаем отдельный будильник
            String[] daysArray = days.split(",");
            for (String day : daysArray) {
                int dayOfWeek = getDayOfWeek(day);

                if (dayOfWeek != -1) {
                    // Создаем уникальный requestCode для каждого будильника
                    int requestCode = (hour * 100 + minute) * 10 + dayOfWeek;

                    // Создаем Intent для BroadcastReceiver
                    Intent intent = new Intent(this, WorkoutAlarmReceiver.class);
                    intent.putExtra("workoutType", type);
                    intent.putExtra("workoutTime", time);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

                    // Настраиваем календарь на нужный день недели и время
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);

                    // Если время уже прошло на текущей неделе, переносим на следующую
                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    }

                    // Устанавливаем еженедельный повторяющийся будильник
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY * 7, // Повторение каждую неделю
                            pendingIntent
                    );

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    } else {
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    }

                    Log.d("WorkoutAlarm", "Alarm scheduled at " + calendar.getTime() +
                            " for workout " + type);

                }
            }
        } catch (Exception e) {
            Log.e("WorkoutAlarm", "Error with alarm scheduling", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelWorkoutAlarm(String type, String time, String days) {
        try {
            // Парсим время тренировки
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            // Отменяем будильники для всех дней тренировки
            String[] daysArray = days.split(",");
            for (String day : daysArray) {
                int dayOfWeek = getDayOfWeek(day);

                if (dayOfWeek != -1) {
                    // Создаем такой же requestCode, как при создании
                    int requestCode = (hour * 100 + minute) * 10 + dayOfWeek;

                    Intent intent = new Intent(this, WorkoutAlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

                    // Отменяем будильник
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при отмене уведомлений: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private int getDayOfWeek(String day) {
        switch (day) {
            case "Mon": return Calendar.MONDAY;
            case "Tue": return Calendar.TUESDAY;
            case "Wed": return Calendar.WEDNESDAY;
            case "Thu": return Calendar.THURSDAY;
            case "Fri": return Calendar.FRIDAY;
            case "Sat": return Calendar.SATURDAY;
            case "Sun": return Calendar.SUNDAY;
            default: return -1;
        }
    }

}