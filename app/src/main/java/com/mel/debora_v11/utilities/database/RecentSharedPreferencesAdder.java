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

public class RecentSharedPreferencesAdder {
    private Context context;
    private PreferenceManager preferenceManager;
    private boolean isSuccess = false;
    public RecentSharedPreferencesAdder(){}
    public void addRecentSharedPreferences(String recentActivity, String recentActivityExtra){
        init();
        addRecentToSharedPreferences(recentActivity, recentActivityExtra);
    }
    private void init(){
        preferenceManager = new PreferenceManager(context);
    }

    private void addRecentToSharedPreferences(String recentActivity, String recentActivityExtra){
        preferenceManager.putString(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        preferenceManager.putString(Constants.KEY_RECENT_ACTIVITY, recentActivity);
        preferenceManager.putString(Constants.KEY_RECENT_ACTIVITY_EXTRA, recentActivityExtra);
        preferenceManager.putString(Constants.KEY_TIMESTAMP, new Date() + "");
    }
}
