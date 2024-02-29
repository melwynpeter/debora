package com.mel.debora_v11.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mel.debora_v11.adapters.ChatAdapter;
import com.mel.debora_v11.databinding.ActivityMainBinding;
import com.mel.debora_v11.models.ChatMessage;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;
import com.mel.debora_v11.utilities.TextToSpeech;
import com.mel.debora_v11.models.TextGenerationKotlin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    public static List<String> text;

    private ActivityMainBinding binding;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;
    private String savePrompt;


    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        textToSpeech = new TextToSpeech();
//        getTokenAndLoadUserData();
        getToken();
        setListeners();
        init();
        listenMessages();

    }
    private void getTokenAndLoadUserData(){
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
//        if(preferenceManager.getString(Constants.KEY_IMAGE) != null) {
//
//        }
    }
    private void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(Constants.KEY_COLLECTION_USERS)
                .document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        Log.d("mydebtest", "document refernce is : " + documentReference);
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        showToast("Sign in Succesfull!!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        showToast("Unable to update token.");
                    }
                });
    }
    private void init(){
        Log.d("calltest", "init() invoked");
        preferenceManager = new PreferenceManager(getApplicationContext());
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
            textToSpeech.convertTextToSpeech(getApplicationContext(), result);
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

    private void signOut(){
        showToast("signing out");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        preferenceManager.clear();
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Unable to sign out");
                    }
                });
    }
    private void setListeners(){
        Log.d("calltest", "setListeners() invoked");
        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
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

    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }



}