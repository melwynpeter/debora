package com.mel.debora_v11.utilities.database;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mel.debora_v11.adapters.AlarmAdapter;
import com.mel.debora_v11.models.Todo;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TimerAdder {
    private List<Todo> timers;
    private AlarmAdapter alarmAdapter;

    private Context context;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;
    private boolean isSuccess = false;

    public TimerAdder(){}
    public TimerAdder(Context context){
        this.context = context;
    }

    public boolean addTimer(String timer){
        init();
        addTimerToDatabase(timer);
        if(isSuccess){
            return true;
        }
        return false;
    }
    private void init(){
        preferenceManager = new PreferenceManager(context);
        db = FirebaseFirestore.getInstance();
    }

    private void addTimerToDatabase(String alarm){
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_TIMER, alarm);
        message.put(Constants.KEY_TIMER_STATUS, "false");
        message.put(Constants.KEY_TIMESTAMP, new Date());
        db.collection(Constants.KEY_COLLECTION_TIMER).add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
