// WorkoutAdapter.java
package com.example.bestworkout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private ArrayList<Workout> workouts;
    private OnDeleteClickListener deleteClickListener;

    // Interface for delete button clicks
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public WorkoutAdapter(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
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

        // Set workout type in spinner
        for (int i = 0; i < holder.workoutType.getCount(); i++) {
            if (holder.workoutType.getItemAtPosition(i).toString().equals(workout.getType())) {
                holder.workoutType.setSelection(i);
                break;
            }
        }

        holder.workoutTime.setText(workout.getTime());
        holder.workoutDays.setText(workout.getDays());

        // Set delete button click listener
        holder.deleteButton.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        Spinner workoutType;
        TextView workoutTime;
        TextView workoutDays;
        ImageButton deleteButton;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutType = itemView.findViewById(R.id.workout_type_spinner);
            workoutTime = itemView.findViewById(R.id.workout_time);
            workoutDays = itemView.findViewById(R.id.workout_days);
            deleteButton = itemView.findViewById(R.id.delete_workout_button);
        }
    }
}