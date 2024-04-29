package com.mel.debora_v11.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mel.debora_v11.adapters.ChatAdapter;
import com.mel.debora_v11.databinding.FragmentChatBinding;
import com.mel.debora_v11.models.ChatMessage;
import com.mel.debora_v11.utilities.TextGenerationKotlin;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;
import com.mel.debora_v11.utilities.mTextToSpeech;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;


public class ChatFragment extends Fragment {
    public static List<String> text;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;

    private FragmentChatBinding binding;

    private mTextToSpeech textToSpeech;

    private String savePrompt;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat, container, false);
        binding = FragmentChatBinding.inflate(getLayoutInflater());

        textToSpeech = new mTextToSpeech();
        init();
        listenMessages();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners(){
        binding.sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textToSpeech.stopUtterance();
                if (binding.promptInput.getText().toString().trim().isEmpty()){
                    textToSpeech.convertTextToSpeech(getActivity(), "please enter a prompt to continue");
                }
                else{
                    sendMessage();
                    receiveMessage();
                }

            }
        });

        binding.backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();

            }
        });
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
    private void sendMessage(){

        Log.d("calltest", "sendMessage() invoked");

        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, "GEMINI MODEL");
        message.put(Constants.KEY_MESSAGE, binding.promptInput.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        db.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        savePrompt = binding.promptInput.getText().toString();
        binding.promptInput.setText(null);
    }
    private void receiveMessage(){
//        text = new ArrayList<>();
        TextGenerationKotlin t = new TextGenerationKotlin();
        CompletableFuture<String> generatedText = t.generateConversationAsync(savePrompt, this);
        generatedText.thenAccept(result -> {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, "GEMINI MODEL");
            message.put(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            message.put(Constants.KEY_MESSAGE, result);
            message.put(Constants.KEY_TIMESTAMP, new Date());
            db.collection(Constants.KEY_COLLECTION_CHAT).add(message);
            textToSpeech.convertTextToSpeech(getActivity(), result);
        });
    }
    private void listenMessages(){
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, "GEMINI MODEL")
                .addSnapshotListener(eventListener);
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, "GEMINI MODEL")
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }
    private final EventListener<QuerySnapshot> eventListener = (value, error) ->  {
        if(error != null){
            return;
        }
        if (value != null){
            int count = chatMessages.size();
            Log.d("countCheck", "count before loop: " + count);
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                    Log.d("countCheck", "count inside loop: " + count);

                }
                Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
                if(count == 0){
                    chatAdapter.notifyDataSetChanged();
                }
                else{
                    chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                    binding.chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                }
                binding.chatRecyclerView.setVisibility(View.VISIBLE);

            }
            binding.progressBar.setVisibility(View.GONE);
        }
    };
    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}