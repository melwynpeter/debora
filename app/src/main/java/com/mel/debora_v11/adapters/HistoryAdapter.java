package com.mel.debora_v11.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.debora_v11.databinding.ItemContainerHistoryBinding;
import com.mel.debora_v11.models.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private final List<History> history;

    public HistoryAdapter(List<History> history) {
        this.history = history;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerHistoryBinding itemContainerHistoryBinding = ItemContainerHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new HistoryViewHolder(itemContainerHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.setHistoryData(history.get(position));
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        ItemContainerHistoryBinding binding;

        HistoryViewHolder(ItemContainerHistoryBinding itemContainerHistoryBinding){
            super(itemContainerHistoryBinding.getRoot());
            binding = itemContainerHistoryBinding;
        }

        void setHistoryData(History history){
            binding.textName.setText(history.conversationId);
        }
    }
}
