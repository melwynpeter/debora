package com.mel.debora_v11.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mel.debora_v11.R;
import com.mel.debora_v11.databinding.FragmentHistoryBinding;
import com.mel.debora_v11.models.Conversation;
import com.mel.debora_v11.models.History;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;
    PreferenceManager preferenceManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_history, container, false);
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getActivity());

        return binding.getRoot();
    }
    private void getHistory(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null){
                        List<History> histories = new ArrayList<>();
//                        for (QueryDocumentSnapshot queryDocumentSnapshot){
//                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
//                                continue;
//                            }
//                            Conversation conversation = new Conversation();
//                            conversation.conversationName = queryDocumentSnapshot.getString(Constants.KEY_CONVERSATION_NAME);
//                        }
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
}