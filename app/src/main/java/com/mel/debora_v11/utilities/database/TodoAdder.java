package com.mel.debora_v11.utilities.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mel.debora_v11.adapters.ChatAdapter;
import com.mel.debora_v11.adapters.TodoAdapter;
import com.mel.debora_v11.models.ChatMessage;
import com.mel.debora_v11.models.Todo;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TodoAdder {
    private List<Todo> todos;
    private TodoAdapter todoAdapter;

    private Context context;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;
    private boolean isSuccess = false;

    public TodoAdder(){}
    public TodoAdder(Context context){
        this.context = context;
    }

    public boolean addTodo(String todo){
        init();
        addTodoToDatabase(todo);
        if(isSuccess){
            return true;
        }
        return false;
    }
    private void init(){
        preferenceManager = new PreferenceManager(context);
        db = FirebaseFirestore.getInstance();
    }

    private void addTodoToDatabase(String todo){
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_TODO, todo);
        message.put(Constants.KEY_TODO_STATUS, "false");
        message.put(Constants.KEY_TIMESTAMP, new Date());
        db.collection(Constants.KEY_COLLECTION_TODO_LIST).add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                isSuccess(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isSuccess(false);
            }
        });
    }
    private void isSuccess(boolean success){
        if(success){
            isSuccess = true;
        }
    }


}
