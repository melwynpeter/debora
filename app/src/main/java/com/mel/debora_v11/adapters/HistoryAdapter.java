package com.mel.debora_v11.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.debora_v11.R;
import com.mel.debora_v11.activities.ChatActivity;
import com.mel.debora_v11.databinding.ItemContainerHistoryBinding;
import com.mel.debora_v11.models.History;
import com.mel.debora_v11.utilities.Constants;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private Context context;
    private List<History> histories;
    public HistoryAdapter(Context context, List<History> histories){
        this.context = context;
        this.histories = histories;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        History history = histories.get(position);

        holder.historyName.setText(history.conversationName);
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView historyName;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            historyName = itemView.findViewById(R.id.historyName);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            History history = histories.get(position);
            String conversationId = history.conversationId;
            String conversationName = history.conversationName;
            Toast.makeText(context, "The position is: Conversation Id: " + conversationId + " and Conversation Name: " + conversationName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(Constants.KEY_CONVERSATION_ID, conversationId);
            intent.putExtra(Constants.KEY_CONVERSATION_NAME, conversationName);
            context.startActivity(intent);


        }
    }
}
