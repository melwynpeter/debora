package com.mel.debora_v11.activities.subactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mel.debora_v11.R;
import com.mel.debora_v11.activities.MainActivity;
import com.mel.debora_v11.activities.SignInActivity;
import com.mel.debora_v11.adapters.AccountAdapter;
import com.mel.debora_v11.adapters.TodoAdapter;
import com.mel.debora_v11.databinding.ActivityChatBinding;
import com.mel.debora_v11.databinding.ActivityTodoBinding;
import com.mel.debora_v11.models.ChatMessage;
import com.mel.debora_v11.models.Todo;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class TodoActivity extends AppCompatActivity {

    private ActivityTodoBinding binding;
    private PreferenceManager preferenceManager;
    private ArrayList<Todo> todos;
    private TodoAdapter todoAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodoBinding.inflate(getLayoutInflater());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        init();
        listenTodos();
        setListeners();


        setContentView(binding.getRoot());
    }

    private void setListeners(){

        binding.backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init(){
        preferenceManager = new PreferenceManager(this);
        todos = new ArrayList<>();
        todoAdapter = new TodoAdapter(todos, preferenceManager.getString(Constants.KEY_USER_ID));
        binding.recyclerView.setAdapter(todoAdapter);

        db = FirebaseFirestore.getInstance();

    }

    private void listenTodos(){
        db.collection(Constants.KEY_COLLECTION_TODO_LIST)
                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) ->  {
        if(error != null){
            return;
        }
        if (value != null){
            int count = todos.size();
            Log.d("countCheck", "count before loop: " + count);
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    Todo todo = new Todo();
                    todo.todo = documentChange.getDocument().getString(Constants.KEY_TODO);
                    todo.todoStatus = documentChange.getDocument().getString(Constants.KEY_TODO_STATUS);
                    todo.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    todo.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    todos.add(todo);
                    Log.d("countCheck", "count inside loop: " + count);
                }
                Collections.sort(todos, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
                if(count == 0){
                    todoAdapter.notifyDataSetChanged();
                }
                else{
                    todoAdapter.notifyItemRangeInserted(todos.size(), todos.size());
                    binding.recyclerView.scrollToPosition(0);
                }
                binding.recyclerView.setVisibility(View.VISIBLE);

            }
            binding.progressBar.setVisibility(View.GONE);
        }
    };

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}