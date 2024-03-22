package com.mel.debora_v11.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mel.debora_v11.R;
import com.mel.debora_v11.adapters.ChatAdapter;
import com.mel.debora_v11.databinding.ActivityChatBinding;
import com.mel.debora_v11.databinding.FragmentChatBinding;
import com.mel.debora_v11.models.ChatMessage;
import com.mel.debora_v11.models.History;
import com.mel.debora_v11.models.TextGenerationKotlin;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;
import com.mel.debora_v11.utilities.TextToSpeech;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ChatActivity extends AppCompatActivity {

    public static List<String> text;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;

    private ActivityChatBinding binding;

    private TextToSpeech textToSpeech;

    private String savePrompt;

    TextGenerationKotlin t;
    private boolean generatedConversationName = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());

        textToSpeech = new TextToSpeech();
        init();
        listenMessages(preferenceManager.getString(Constants.KEY_CONVERSATION_ID));
        setListeners();
        t = new TextGenerationKotlin();
        Toast.makeText(this, preferenceManager.getString(Constants.KEY_CONVERSATION_ID), Toast.LENGTH_SHORT).show();


        setContentView(binding.getRoot());
    }
    private void setListeners(){
        binding.sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textToSpeech.stopUtterance();
                if (binding.promptInput.getText().toString().trim().isEmpty()){
                    textToSpeech.convertTextToSpeech(getApplicationContext(), "please enter a prompt to continue");
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
//                getParentFragmentManager().popBackStack();
                finish();
            }
        });

        binding.newConversationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessages.clear();
                chatAdapter.notifyDataSetChanged();
                createNewConversation(false);

            }
        });
    }


    private void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    private void init(){
        Log.d("calltest", "init() invoked");
        preferenceManager = new PreferenceManager(getApplicationContext());

        initConversation();


        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        db = FirebaseFirestore.getInstance();

//        TextGenerationKotlin t = new TextGenerationKotlin();

        // check conversation name
        db.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constants.KEY_CONVERSATION_ID, preferenceManager.getString(Constants.KEY_CONVERSATION_ID))
                .whereEqualTo(Constants.KEY_CONVERSATION_NAME, "New Chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        generatedConversationName = false;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        generatedConversationName = true;
                    }
                });

    }
    private void initConversation(){
        if(!preferenceManager.containsString(Constants.KEY_CONVERSATION_ID) && !getIntent().hasExtra(Constants.KEY_CONVERSATION_ID)){
            createNewConversation(true);
        }
        else if(getIntent().hasExtra(Constants.KEY_CONVERSATION_ID)){
            String conversationId = getIntent().getStringExtra(Constants.KEY_CONVERSATION_ID);
            String conversationName = getIntent().getStringExtra(Constants.KEY_CONVERSATION_NAME);

            preferenceManager.putString(Constants.KEY_CONVERSATION_ID, conversationId);
            preferenceManager.putString(Constants.KEY_CONVERSATION_NAME, conversationName);
        }
        if(preferenceManager.containsString(Constants.KEY_CONVERSATION_NAME)){
            binding.conversationName.setText(preferenceManager.getString(Constants.KEY_CONVERSATION_NAME));
        }
    }

    private void createNewConversation(boolean isFirstConversation){

        generatedConversationName = false;

//        if(isFirstConversation){
//            preferenceManager.putString(Constants.KEY_CONVERSATION_ID, (Constants.KEY_USER_ID + "First Conversation"));
//        }


        String conversationName = "New Chat";
        String conversationCreatorId = preferenceManager.getString(Constants.KEY_USER_ID);
        String conversationCreationDate = (new Date()).toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> conversation = new HashMap<>();
        conversation.put(Constants.KEY_CONVERSATION_NAME, conversationName);
        conversation.put(Constants.KEY_CONVERSATION_CREATOR_ID, conversationCreatorId);
        conversation.put(Constants.KEY_CONVERSATION_CREATION_DATE, conversationCreationDate);
        db.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .add(conversation)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("mydebtest", "conversation added successfully" + documentReference);
                        preferenceManager.putString(Constants.KEY_CONVERSATION_ID, documentReference.getId());
                        preferenceManager.putString(Constants.KEY_CONVERSATION_NAME, "New Chat");
                        binding.conversationName.setText("New Chat");
                        if(textToSpeech.isUtterance()) {
                            textToSpeech.stopUtterance();
                        }
                        listenMessages(preferenceManager.getString(Constants.KEY_CONVERSATION_ID));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("mydebtest", "Error adding user", e);
//                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void sendMessage(){

        Log.d("calltest", "sendMessage() invoked");

        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, "GEMINI MODEL");
        message.put(Constants.KEY_MESSAGE, binding.promptInput.getText().toString());
        message.put(Constants.KEY_CONVERSATION_ID, preferenceManager.getString(Constants.KEY_CONVERSATION_ID));
        message.put(Constants.KEY_TIMESTAMP, new Date());
        db.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        savePrompt = binding.promptInput.getText().toString();
        binding.promptInput.setText(null);
    }
    private void receiveMessage(){
//        text = new ArrayList<>();
        CompletableFuture<String> generatedText = t.generateConversationAsync(savePrompt, this);
        generatedText.thenAccept(result -> {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, "GEMINI MODEL");
            message.put(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            message.put(Constants.KEY_CONVERSATION_ID, preferenceManager.getString(Constants.KEY_CONVERSATION_ID));
            message.put(Constants.KEY_MESSAGE, result);
            message.put(Constants.KEY_TIMESTAMP, new Date());
            db.collection(Constants.KEY_COLLECTION_CHAT).add(message);
            textToSpeech.convertTextToSpeech(getApplicationContext(), result);
        });
    }
    private void listenMessages(String conversationId){
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, "GEMINI MODEL")
                .whereEqualTo(Constants.KEY_CONVERSATION_ID, conversationId)
                .addSnapshotListener(eventListener);
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, "GEMINI MODEL")
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_CONVERSATION_ID, conversationId)
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
                if(count == 1 && generatedConversationName == false){
                    generateConversationName();
                    Toast.makeText(this, "generated a coversaruib bame", Toast.LENGTH_SHORT).show();
                }
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

    private void generateConversationName(){
        CompletableFuture<String> generatedText = t.generateConversationNameAsync(chatMessages.get(1).message, this);
        generatedText.thenAccept(result -> {
            DocumentReference documentReference = db.collection(Constants.KEY_COLLECTION_CONVERSATION)
                    .document(
                            preferenceManager.getString(Constants.KEY_CONVERSATION_ID)
                    );
            documentReference.update(Constants.KEY_CONVERSATION_NAME, result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.conversationName.setText(result);
                            generatedConversationName = true;
                            preferenceManager.putString(Constants.KEY_CONVERSATION_NAME, result);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                        showToast("Unable to update token.");
                        }
                    });
        });
    }


    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}