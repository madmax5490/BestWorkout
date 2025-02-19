package com.example.bestworkout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private ArrayList<Workout> workouts;

    public WorkoutAdapter(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.type.setText(workout.getType());
        holder.time.setText(workout.getTime());
        holder.days.setText(workout.getDays());
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView type, time, days;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.workout_type);
            time = itemView.findViewById(R.id.workout_time);
            days = itemView.findViewById(R.id.workout_days);
        }
    }
}