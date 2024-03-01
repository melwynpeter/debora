package com.mel.debora_v11.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mel.debora_v11.R;
import com.mel.debora_v11.adapters.ChatAdapter;
import com.mel.debora_v11.databinding.FragmentChatBinding;
import com.mel.debora_v11.models.ChatMessage;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    public static List<String> text;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;

    private FragmentChatBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat, container, false);
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private void showToast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
    private void init(){
        Log.d("calltest", "init() invoked");
        preferenceManager = new PreferenceManager(getActivity());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        db = FirebaseFirestore.getInstance();
    }
}