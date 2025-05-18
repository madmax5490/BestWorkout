package com.example.bestworkout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

public class WorkoutAlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "workout_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WorkoutAlarm", "WorkoutAlarmReceiver.onReceive вызван");

        String workoutType = intent.getStringExtra("workoutType");
        String workoutTime = intent.getStringExtra("workoutTime");

        Log.d("WorkoutAlarm", "Type of workout: " + workoutType + ", tyme: " + workoutTime);

        // Создаем Intent для запуска приложения при клике на уведомление
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        // Создаем и отображаем уведомление
        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Workout Time!")
                .setContentText("It's time for your " + workoutType + " workout")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        Log.d("WorkoutAlarm", "Notification sent");
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Workout Notification";
            String description = "Channel for workout notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            Log.d("WorkoutAlarm", "Notification channel is created");
        }
    }
}