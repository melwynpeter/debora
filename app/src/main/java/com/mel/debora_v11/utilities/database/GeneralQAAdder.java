package com.mel.debora_v11.utilities.database;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.util.Date;
import java.util.HashMap;

public class GeneralQAAdder {
    private Context context;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;
    private boolean isSuccess = false;

    public GeneralQAAdder(){}
    public GeneralQAAdder(Context context){
        this.context = context;
    }

    public boolean addGeneralQA(String generalQA, String prompt){
        init();
        addGeneralQAToDatabase(generalQA, prompt);
        if(isSuccess){
            return true;
        }
        return false;
    }
    private void init(){
        preferenceManager = new PreferenceManager(context);
        db = FirebaseFirestore.getInstance();
    }

    private void addGeneralQAToDatabase(String generalQA, String prompt){
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_GENERAL_QA_RESPONSE, generalQA);
        message.put(Constants.KEY_GENERAL_QA_PROMPT, prompt);
        message.put(Constants.KEY_TIMESTAMP, new Date());
        db.collection(Constants.KEY_COLLECTION_GENERAL_QA_LIST).add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
