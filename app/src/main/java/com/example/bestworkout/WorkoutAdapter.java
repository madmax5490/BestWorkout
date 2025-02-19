// WorkoutAdapter.java
package com.example.bestworkout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private final ArrayList<Workout> workouts;

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
        holder.time.setText(workout.getTime());
        holder.days.setText(workout.getDays());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(holder.itemView.getContext(),
                R.array.workout_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.typeSpinner.setAdapter(adapter);

        holder.typeSpinner.setSelection(adapter.getPosition(workout.getType()));
        holder.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workout.setType(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        Spinner typeSpinner;
        TextView time, days;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            typeSpinner = itemView.findViewById(R.id.workout_type_spinner);
            time = itemView.findViewById(R.id.workout_time);
            days = itemView.findViewById(R.id.workout_days);
        }
    }
}