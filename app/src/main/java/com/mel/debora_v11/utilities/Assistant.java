package com.mel.debora_v11.utilities;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.ViewModelStoreOwner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Assistant {
    private TextGenerationKotlin t = new TextGenerationKotlin();
    private ArrayList<String> intents = new ArrayList<>();

    private HashMap<String, String> responseHasMap;

    final String TAG = "deb11";
    public ArrayList<String> getIntents() {
        intents.add(Constants.INTENT_GREETING);
        intents.add(Constants.INTENT_GOODBYE);
        intents.add(Constants.INTENT_CREATOR_OF_MODEL);
        intents.add(Constants.INTENT_NAME_OF_MODEL);
        intents.add(Constants.INTENT_GENERATE_EMAIL);
        intents.add(Constants.INTENT_GENERATE_TEXT);
        intents.add(Constants.INTENT_GENERATE_TEXT_WITHOUT_SUBJECT);
        intents.add(Constants.INTENT_ALARM);
        //intents.add(Constants.INTENT_ALARM_WITH_TIME);
        //intents.add(Constants.INTENT_ALARM_WITH_TIME_AND_DATE);
        intents.add(Constants.INTENT_TIMER);
        intents.add(Constants.INTENT_TIMER_WITHOUT_TIME);
        intents.add(Constants.INTENT_YOUTUBE);
        intents.add(Constants.INTENT_GENERAQA);
        intents.add(Constants.INTENT_INVALID_ACTION);
        intents.add(Constants.INTENT_NOT_SURE);
        return intents;
    }

    public HashMap<String, String> getResponse(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        HashMap<String, String> response = new HashMap<>();
        response.put(Constants.NEEDS_DIALOG, "false");
        String intentPrediction = textClassification(prompt, getIntents(), viewModelStoreOwner);
        Log.d(TAG, "getResponse: " + intentPrediction);

        if(intentPrediction.equals(Constants.INTENT_GREETING)){ // GREETING
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GREETING, viewModelStoreOwner));
        } else if (intentPrediction.equals(Constants.INTENT_GOODBYE)) { // GOODBYE
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GOODBYE, viewModelStoreOwner));
            System.exit(0);
        } else if (intentPrediction.equals(Constants.INTENT_CREATOR_OF_MODEL)) { // CREATOR_OF_MODEL
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_CREATOR_OF_MODEL, viewModelStoreOwner));
        } else if (intentPrediction.equals(Constants.INTENT_NAME_OF_MODEL)) { // NAME_OF_MODEL
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_NAME_OF_MODEL, viewModelStoreOwner));
        } else if (intentPrediction.equals(Constants.INTENT_GENERATE_TEXT)) { // GENERATE_TEXT
            response.put(Constants.NEEDS_DIALOG, "true");
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERATE_TEXT, viewModelStoreOwner));
            String generatedText = generateText(prompt, viewModelStoreOwner);
        } else if (intentPrediction.equals(Constants.INTENT_GENERATE_TEXT_WITHOUT_SUBJECT)) { // GENERATE_TEXT_WITHOUT_SUBJECT
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERATE_TEXT_WITHOUT_SUBJECT, viewModelStoreOwner));
        } else if (intentPrediction.equals(Constants.INTENT_GENERATE_EMAIL)) { // GENERATE TEXT
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERATE_EMAIL, viewModelStoreOwner));
        } else if (intentPrediction.equals(Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT, viewModelStoreOwner));
        } else if (intentPrediction.equals(Constants.INTENT_ALARM)) {
            String time = extractTime(prompt, viewModelStoreOwner);
            if(time != null) {
                if (setAlarm(time)) {
                    Log.d(TAG, "getResponse: " + time);
                    response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_ALARM, viewModelStoreOwner));
                } else {
                    response.put(Constants.RESPONSE, "sorry, couldn't set an alarm");
                }
            } else {
                response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_ALARM_WITHOUT_TIME, viewModelStoreOwner));
            }
        } else if (intentPrediction.equals(Constants.INTENT_ALARM_WITHOUT_TIME)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_ALARM_WITHOUT_TIME, viewModelStoreOwner));
        }  else if (intentPrediction.equals(Constants.INTENT_TIMER)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_TIMER, viewModelStoreOwner));
        } else if (intentPrediction.equals(Constants.INTENT_TIMER_WITHOUT_TIME)) {
            String time = extractTime(prompt, viewModelStoreOwner);
            if(setTimer(time)){
                response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_TIMER_WITHOUT_TIME, viewModelStoreOwner));
            }else{
                response.put(Constants.RESPONSE, "sorry, couldn't set a timer");
            }
        } else if (intentPrediction.equals(Constants.INTENT_YOUTUBE)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_YOUTUBE, viewModelStoreOwner));
        } else if (intentPrediction.equals(Constants.INTENT_WHATSAPP)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_WHATSAPP, viewModelStoreOwner));
        } else{
            response.put(Constants.RESPONSE, "Sorry couldn't discern what you're trying to say");
        }
        return response;
    }

    private void doTask(String intent, String prompt){
        if(intent.equals(Constants.INTENT_GENERATE_TEXT)){
            generateText(prompt, null);
        }
    }


    private String textClassification(String prompt, ArrayList<String> intent, ViewModelStoreOwner viewModelStoreOwner) {
        String response;
        CompletableFuture<String> generatedText = t.textClassificationAsync(prompt, intent, viewModelStoreOwner);
//        generatedText.thenAccept(result -> {
//            response = result;
//        }
//        );
        try {
            response = generatedText.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }


    // TODO: tasks
    // TASKS

    // GREETING
    private String getTextResponse(String prompt, String intent, ViewModelStoreOwner viewModelStoreOwner) {
        String response = "";
        Responses responses = new Responses();
        ArrayList<String> intentResponses = responses.getResponses(intent);
        response = select(prompt, intentResponses,  viewModelStoreOwner);
        return response;
    }

    // GENERATE TEXT
    private String generateText(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        String response = "";
        CompletableFuture<String> generatedText = t.generateTextTaskAsync(prompt, viewModelStoreOwner);
        try {
            response = generatedText.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    // SET ALARM
    private boolean setAlarm(String time){
        boolean success = true;
        return success;
    }

    // SET TIMER
    private boolean setTimer(String time){
        boolean success = false;
        return success;
    }

    // OPEN YOUTUBE
    private boolean openYoutube(){
        boolean success = false;
        return success;
    }





    // UTILITIES
    private String select(String prompt, ArrayList<String> intentResponses, ViewModelStoreOwner viewModelStoreOwner){
        String response;
        CompletableFuture<String> generatedText = t.selectResponseAsync(prompt, intentResponses, viewModelStoreOwner);
        try {
            response = generatedText.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private String extractTime(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        String time = "";
        CompletableFuture<String> extractedTime = t.extractTimeAsync(prompt, viewModelStoreOwner);
        try {
            time = extractedTime.get();
            Log.d(TAG, "extractTime: " + time);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "extractTime: " + time);

        if(!time.equals("null")){
            return time;
        }else{
            return null;
        }
    }
}



