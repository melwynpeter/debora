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

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>{
    private Context context;
    private List<Reminder> reminders;
    private String userId;
    public ReminderAdapter(List<Reminder> reminders, String userId){
        this.reminders = reminders;
        this.userId = userId;
    }
    public ReminderAdapter(List<Reminder> reminders, String userId, Context context){
        this.reminders = reminders;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        holder.reminderTextView.setText(reminder.reminderText);
        holder.reminderDateTimeTextView.setText(reminder.dateTime);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView reminderTextView;
        public TextView reminderDateTimeTextView;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            reminderTextView = itemView.findViewById(R.id.reminder);
            reminderDateTimeTextView = itemView.findViewById(R.id.textDateTime);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Reminder reminder = reminders.get(position);
            String actualGeneratedText = reminder.reminderText;

        }
    }
}
