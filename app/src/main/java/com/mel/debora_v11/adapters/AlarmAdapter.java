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
import com.mel.debora_v11.models.Alarm;
import com.mel.debora_v11.models.GeneratedText;
import com.mel.debora_v11.models.Reminder;
import com.mel.debora_v11.models.Timer;
import com.mel.debora_v11.models.Todo;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>{
    private Context context;
    private List<Alarm> alarms;
    private String userId;
    public AlarmAdapter(List<Alarm> alarms, String userId){
        this.alarms = alarms;
        this.userId = userId;
    }
    public AlarmAdapter(List<Alarm> alarms, String userId, Context context){
        this.alarms = alarms;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public AlarmAdapter.AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);

        holder.alarmTextView.setText(alarm.alarm);
        holder.alarmDateTimeTextView.setText(alarm.dateTime);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView alarmTextView;
        public TextView alarmDateTimeTextView;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            alarmTextView = itemView.findViewById(R.id.alarm);
            alarmDateTimeTextView = itemView.findViewById(R.id.textDateTime);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Alarm alarm = alarms.get(position);
            String actualTodo = alarm.alarm;

        }
    }
}
