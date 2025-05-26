package com.example.bestworkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutTipAdapter extends RecyclerView.Adapter<WorkoutTipAdapter.TipViewHolder> {

    private Context context;
    private List<WorkoutTip> tips;

    public WorkoutTipAdapter(Context context, List<WorkoutTip> tips) {
        this.context = context;
        this.tips = tips;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workout_tip, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        WorkoutTip tip = tips.get(position);
        holder.tipText.setText(tip.getTipText());
        holder.tipImage.setImageResource(tip.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {
        ImageView tipImage;
        TextView tipText;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            tipImage = itemView.findViewById(R.id.tip_image);
            tipText = itemView.findViewById(R.id.tip_text);
        }
    }
}
