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
import com.mel.debora_v11.models.Todo;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder>{
    private Context context;
    private List<Todo> todos;
    private String userId;
    public TodoAdapter(List<Todo> todos, String userId){
        this.todos = todos;
        this.userId = userId;
    }
    public TodoAdapter(List<Todo> todos, String userId, Context context){
        this.todos = todos;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public TodoAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);

        holder.todoTextView.setText(todo.todo);
        holder.todoDateTimeTextView.setText(todo.dateTime);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView todoTextView;
        public TextView todoDateTimeTextView;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            todoTextView = itemView.findViewById(R.id.todo);
            todoDateTimeTextView = itemView.findViewById(R.id.textDateTime);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Todo todo = todos.get(position);
            String actualTodo = todo.todo;

        }
    }
}
