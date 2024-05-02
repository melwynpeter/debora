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
import com.mel.debora_v11.adapters.GeneralQAAdapter;
import com.mel.debora_v11.adapters.GeneratedTextsAdapter;
import com.mel.debora_v11.databinding.ActivityGeneralQaactivityBinding;
import com.mel.debora_v11.databinding.ActivityGeneratedTextsBinding;
import com.mel.debora_v11.models.GeneralQA;
import com.mel.debora_v11.models.GeneratedText;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class GeneralQAActivity extends AppCompatActivity {
    private ActivityGeneralQaactivityBinding binding;
    private PreferenceManager preferenceManager;
    private ArrayList<GeneralQA> generalQAs;
    private GeneralQAAdapter generalQAAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeneralQaactivityBinding.inflate(getLayoutInflater());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        init();
        listenGeneralQAs();
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
        generalQAs = new ArrayList<>();
        generalQAAdapter = new GeneralQAAdapter(generalQAs, preferenceManager.getString(Constants.KEY_USER_ID), this);
        binding.recyclerView.setAdapter(generalQAAdapter);

        db = FirebaseFirestore.getInstance();

    }

    private void listenGeneralQAs(){
        db.collection(Constants.KEY_COLLECTION_GENERAL_QA_LIST)
                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) ->  {
        if(error != null){
            return;
        }
        if (value != null){
            int count = generalQAs.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    GeneralQA generalQA = new GeneralQA();
                    generalQA.generalQAResponse = documentChange.getDocument().getString(Constants.KEY_GENERAL_QA_RESPONSE);
                    generalQA.generalQAPrompt = documentChange.getDocument().getString(Constants.KEY_GENERAL_QA_PROMPT);
                    generalQA.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    generalQA.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    generalQAs.add(generalQA);
                }
                Collections.sort(generalQAs, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
                if(count == 0){
                    generalQAAdapter.notifyDataSetChanged();
                }
                else{
                    generalQAAdapter.notifyItemRangeInserted(generalQAs.size(), generalQAs.size());
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