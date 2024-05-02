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
import com.mel.debora_v11.adapters.RecentAdapter;
import com.mel.debora_v11.databinding.ActivityAlarmManagerBinding;
import com.mel.debora_v11.databinding.ActivityRecentBinding;
import com.mel.debora_v11.models.Alarm;
import com.mel.debora_v11.models.Recent;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class RecentActivity extends AppCompatActivity {
    private ActivityRecentBinding binding;
    private PreferenceManager preferenceManager;
    private ArrayList<Recent> recents;
    private RecentAdapter recentAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentBinding.inflate(getLayoutInflater());

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
        recents = new ArrayList<>();
        recentAdapter = new RecentAdapter(recents, preferenceManager.getString(Constants.KEY_USER_ID));
        binding.recyclerView.setAdapter(recentAdapter);

        db = FirebaseFirestore.getInstance();

    }

    private void listenTodos(){
        db.collection(Constants.KEY_COLLECTION_RECENT_ACTIVITY_LIST)
                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) ->  {
        if(error != null){
            return;
        }
        if (value != null){
            int count = recents.size();
            Log.d("countCheck", "count before loop: " + count);
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    Recent recent = new Recent();
                    recent.recentActivity = documentChange.getDocument().getString(Constants.KEY_RECENT_ACTIVITY);
                    recent.recentActivityType = documentChange.getDocument().getString(Constants.KEY_RECENT_ACTIVITY_TYPE);
                    recent.recentActivityExtra = documentChange.getDocument().getString(Constants.KEY_RECENT_ACTIVITY_EXTRA);
                    recent.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    recent.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    recents.add(recent);
                }
                Collections.sort(recents, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
                if(count == 0){
                    recentAdapter.notifyDataSetChanged();
                }
                else{
                    recentAdapter.notifyItemRangeInserted(recents.size(), recents.size());
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