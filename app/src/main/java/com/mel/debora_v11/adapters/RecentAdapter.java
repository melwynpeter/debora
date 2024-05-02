package com.mel.debora_v11.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.debora_v11.R;
import com.mel.debora_v11.models.Recent;
import com.mel.debora_v11.models.Reminder;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentViewHolder>{
    private Context context;
    private List<Recent> recents;
    private String userId;
    public RecentAdapter(List<Recent> recents, String userId){
        this.recents = recents;
        this.userId = userId;
    }
    public RecentAdapter(List<Recent> recents, String userId, Context context){
        this.recents = recents;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public RecentAdapter.RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_recent, parent, false);
        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentAdapter.RecentViewHolder holder, int position) {
        Recent recent = recents.get(position);

        holder.recentTextView.setText(recent.recentActivity);
//        holder.recentDateTi.setText(recent.recentActivityType);
        holder.recentDateTimeTextView.setText(recent.dateTime);
    }

    @Override
    public int getItemCount() {
        return recents.size();
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView recentTextView;
        public TextView recentDateTimeTextView;

        public RecentViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            recentTextView = itemView.findViewById(R.id.recent);
            recentDateTimeTextView = itemView.findViewById(R.id.textDateTime);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Recent recent = recents.get(position);
            String actualRecentActivity = recent.recentActivity;

        }
    }
}
