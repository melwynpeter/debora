package com.mel.debora_v11.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.debora_v11.R;
import com.mel.debora_v11.models.GeneratedText;
import com.mel.debora_v11.models.Reminder;
import com.mel.debora_v11.models.Timer;

import java.util.List;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerViewHolder>{
    private Context context;
    private List<Timer> timers;
    private String userId;
    public TimerAdapter(List<Timer> timers, String userId){
        this.timers = timers;
        this.userId = userId;
    }
    public TimerAdapter(List<Timer> reminders, String userId, Context context){
        this.timers = reminders;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public TimerAdapter.TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_timer, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerAdapter.TimerViewHolder holder, int position) {
        Timer timer = timers.get(position);

        holder.timerTextView.setText(timer.timer);
        holder.timerDateTimeTextView.setText(timer.dateTime);
    }

    @Override
    public int getItemCount() {
        return timers.size();
    }

    public class TimerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView timerTextView;
        public TextView timerDateTimeTextView;

        public TimerViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            timerTextView = itemView.findViewById(R.id.timer);
            timerDateTimeTextView = itemView.findViewById(R.id.textDateTime);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Timer timer = timers.get(position);
            String actualGeneratedText = timer.timer;

        }
    }
}
