package com.mel.debora_v11.activities.subactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mel.debora_v11.R;
import com.mel.debora_v11.adapters.GeneratedTextsAdapter;
import com.mel.debora_v11.adapters.ReminderAdapter;
import com.mel.debora_v11.databinding.ActivityGeneratedTextsBinding;
import com.mel.debora_v11.databinding.ActivityReminderBinding;
import com.mel.debora_v11.models.GeneratedText;
import com.mel.debora_v11.models.Reminder;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class ReminderActivity extends AppCompatActivity {
    private ActivityReminderBinding binding;
    private PreferenceManager preferenceManager;
    private ArrayList<Reminder> reminders;
    private ReminderAdapter reminderAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReminderBinding.inflate(getLayoutInflater());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        init();
        listenGeneratedTexts();
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
        reminders = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(reminders, preferenceManager.getString(Constants.KEY_USER_ID), this);
        binding.recyclerView.setAdapter(reminderAdapter);

        db = FirebaseFirestore.getInstance();

    }

    private void listenGeneratedTexts(){
        db.collection(Constants.KEY_COLLECTION_REMINDER_LIST)
                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) ->  {
        if(error != null){
            return;
        }
        if (value != null){
            int count = reminders.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    Reminder reminder = new Reminder();
                    reminder.reminderTime = documentChange.getDocument().getString(Constants.KEY_REMINDER_TIME);
                    reminder.reminderText = documentChange.getDocument().getString(Constants.KEY_REMINDER_TEXT);
                    reminder.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    reminder.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    reminders.add(reminder);
                }
                Collections.sort(reminders, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
                if(count == 0){
                    reminderAdapter.notifyDataSetChanged();
                }
                else{
                    reminderAdapter.notifyItemRangeInserted(reminders.size(), reminders.size());
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