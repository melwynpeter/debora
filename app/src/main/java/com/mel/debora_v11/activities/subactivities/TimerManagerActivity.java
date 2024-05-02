package com.mel.debora_v11.activities.subactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mel.debora_v11.R;
import com.mel.debora_v11.adapters.AlarmAdapter;
import com.mel.debora_v11.adapters.TimerAdapter;
import com.mel.debora_v11.databinding.ActivityAlarmManagerBinding;
import com.mel.debora_v11.databinding.ActivityTimerManagerBinding;
import com.mel.debora_v11.models.Alarm;
import com.mel.debora_v11.models.Timer;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class TimerManagerActivity extends AppCompatActivity {
    private ActivityTimerManagerBinding binding;
    private PreferenceManager preferenceManager;
    private ArrayList<Timer> timers;
    private TimerAdapter timerAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerManagerBinding.inflate(getLayoutInflater());

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
        timers = new ArrayList<>();
        timerAdapter = new TimerAdapter(timers, preferenceManager.getString(Constants.KEY_USER_ID));
        binding.recyclerView.setAdapter(timerAdapter);

        db = FirebaseFirestore.getInstance();

    }

    private void listenTodos(){
        db.collection(Constants.KEY_COLLECTION_TIMER)
                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) ->  {
        if(error != null){
            return;
        }
        if (value != null){
            int count = timers.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    Timer timer = new Timer();
                    timer.timer = documentChange.getDocument().getString(Constants.KEY_TIMER);
                    timer.timerStatus = documentChange.getDocument().getString(Constants.KEY_TIMER_STATUS);
                    timer.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    timer.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    timers.add(timer);
                }
                Collections.sort(timers, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
                if(count == 0){
                    timerAdapter.notifyDataSetChanged();
                }
                else{
                    timerAdapter.notifyItemRangeInserted(timers.size(), timers.size());
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