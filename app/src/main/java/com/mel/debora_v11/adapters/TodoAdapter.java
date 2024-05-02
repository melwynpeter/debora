package com.mel.debora_v11.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.debora_v11.databinding.ItemContainerReceivedMessageBinding;
import com.mel.debora_v11.databinding.ItemContainerSentMessageBinding;
import com.mel.debora_v11.databinding.ItemContainerTodoBinding;
import com.mel.debora_v11.models.ChatMessage;
import com.mel.debora_v11.models.Todo;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Todo> todos;
    private final String todoMakerId;


    public TodoAdapter(List<Todo> todos, String todoMakerId) {
        this.todos = todos;
        this.todoMakerId = todoMakerId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new TodoAdapter.AddTodoViewHolder(
                    ItemContainerTodoBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((TodoAdapter.AddTodoViewHolder) holder).setData(todos.get(position));
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return todos.get(position).todoMakerId.equals(todoMakerId);
        return 0;
    }

    static class AddTodoViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerTodoBinding binding;
        AddTodoViewHolder(ItemContainerTodoBinding itemContainerTodoBinding){
            super(itemContainerTodoBinding.getRoot());
            binding = itemContainerTodoBinding;
        }

        void setData(Todo todo){
            binding.textMessage.setText(todo.todo);
            binding.textDateTime.setText(todo.dateTime);
        }
    }
}
