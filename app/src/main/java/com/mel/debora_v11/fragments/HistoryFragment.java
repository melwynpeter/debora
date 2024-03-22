package com.mel.debora_v11.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mel.debora_v11.R;
import com.mel.debora_v11.adapters.HistoryAdapter;
import com.mel.debora_v11.databinding.FragmentHistoryBinding;
import com.mel.debora_v11.models.ChatMessage;
import com.mel.debora_v11.models.Conversation;
import com.mel.debora_v11.models.History;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;
    PreferenceManager preferenceManager;

    List<History> histories = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_history, container, false);
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getActivity());

        getHistory();

        return binding.getRoot();
    }
    private void getHistory() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constants.KEY_CONVERSATION_CREATOR_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loading(false);
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            histories = new ArrayList<>();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                                History history = new History();
                                history.conversationId = queryDocumentSnapshot.getId();
                                history.conversationName = queryDocumentSnapshot.getString(Constants.KEY_CONVERSATION_NAME);
                                history.dateTime = getReadableDateTime(queryDocumentSnapshot.getDate(Constants.KEY_TIMESTAMP));
                                history.dateObject = queryDocumentSnapshot.getDate(Constants.KEY_TIMESTAMP);
                                histories.add(history);
                            }
                            Collections.sort(histories, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));

                            if (histories.size() > 0) {
                                HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), histories);
                                binding.historyRecyclerView.setAdapter(historyAdapter);
                                binding.historyRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                showErrorMessage();
                            }
                        } else {
                            showErrorMessage();
                        }

                    }
                });
    }


    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "NO history available"));

    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}