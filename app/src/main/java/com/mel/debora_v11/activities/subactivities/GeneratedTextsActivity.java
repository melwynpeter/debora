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
import com.mel.debora_v11.adapters.GeneratedTextsAdapter;
import com.mel.debora_v11.databinding.ActivityAlarmManagerBinding;
import com.mel.debora_v11.databinding.ActivityGeneratedTextsBinding;
import com.mel.debora_v11.models.Alarm;
import com.mel.debora_v11.models.GeneratedText;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class GeneratedTextsActivity extends AppCompatActivity {
    private ActivityGeneratedTextsBinding binding;
    private PreferenceManager preferenceManager;
    private ArrayList<GeneratedText> generatedTexts;
    private GeneratedTextsAdapter generatedTextsAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeneratedTextsBinding.inflate(getLayoutInflater());

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
        generatedTexts = new ArrayList<>();
        generatedTextsAdapter = new GeneratedTextsAdapter(generatedTexts, preferenceManager.getString(Constants.KEY_USER_ID), this);
        binding.recyclerView.setAdapter(generatedTextsAdapter);

        db = FirebaseFirestore.getInstance();

    }

    private void listenGeneratedTexts(){
        db.collection(Constants.KEY_COLLECTION_GENERATED_TEXTS_LIST)
                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) ->  {
        if(error != null){
            return;
        }
        if (value != null){
            int count = generatedTexts.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    GeneratedText generatedText = new GeneratedText();
                    generatedText.generatedText = documentChange.getDocument().getString(Constants.KEY_GENERATED_TEXTS);
                    generatedText.generatedTextPrompt = documentChange.getDocument().getString(Constants.KEY_GENERATED_TEXTS_PROMPT);
                    generatedText.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    generatedText.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    generatedTexts.add(generatedText);
                }
                Collections.sort(generatedTexts, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
                if(count == 0){
                    generatedTextsAdapter.notifyDataSetChanged();
                }
                else{
                    generatedTextsAdapter.notifyItemRangeInserted(generatedTexts.size(), generatedTexts.size());
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