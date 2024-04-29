package com.mel.debora_v11.utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.ViewModelStoreOwner;

import com.mel.debora_v11.api.Id;
import com.mel.debora_v11.api.Item;
import com.mel.debora_v11.api.YoutubeDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssistantHelper {
    private TextGenerationKotlin t = new TextGenerationKotlin();
    private ArrayList<String> intents = new ArrayList<>();

    private HashMap<String, String> responseHasMap;

    final String TAG = "deb11";

    private Context context;

    public AssistantHelper(){}
    public AssistantHelper(Context context){
        this.context = context;
    }
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
        response.put(Constants.RESPONSE_INTENT, "false");

        String intentPrediction = textClassification(prompt, getIntents(), viewModelStoreOwner);
        Log.d(TAG, "getResponse: " + intentPrediction);

        if(intentPrediction.equals(Constants.INTENT_GREETING)){ // GREETING
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GREETING, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_GREETING);
        } else if (intentPrediction.equals(Constants.INTENT_GOODBYE)) { // GOODBYE
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GOODBYE, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_GOODBYE);
            System.exit(0);
        } else if (intentPrediction.equals(Constants.INTENT_CREATOR_OF_MODEL)) { // CREATOR_OF_MODEL
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_CREATOR_OF_MODEL, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_CREATOR_OF_MODEL);

        } else if (intentPrediction.equals(Constants.INTENT_NAME_OF_MODEL)) { // NAME_OF_MODEL
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_NAME_OF_MODEL, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_NAME_OF_MODEL);

        } else if (intentPrediction.equals(Constants.INTENT_GENERATE_TEXT)) { // GENERATE_TEXT
            response.put(Constants.NEEDS_DIALOG, "true");
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERATE_TEXT, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_GENERATE_TEXT);

        } else if (intentPrediction.equals(Constants.INTENT_GENERATE_TEXT_WITHOUT_SUBJECT)) { // GENERATE_TEXT_WITHOUT_SUBJECT
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERATE_TEXT_WITHOUT_SUBJECT, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_GENERATE_TEXT_WITHOUT_SUBJECT);

        } else if (intentPrediction.equals(Constants.INTENT_GENERATE_EMAIL)) { // GENERATE TEXT
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERATE_EMAIL, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_GENERATE_EMAIL);

        } else if (intentPrediction.equals(Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT);

        } else if (intentPrediction.equals(Constants.INTENT_ALARM)) {
            String time = extractTime(prompt, viewModelStoreOwner);
            if(time != null) {
                if (setAlarm(time)) {
                    Log.d(TAG, "getResponse: " + time);
                    response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_ALARM, viewModelStoreOwner));
                    response.put(Constants.RESPONSE_INTENT, Constants.INTENT_ALARM);

                } else {
                    response.put(Constants.RESPONSE, "sorry, couldn't set an alarm");
                }
            } else {
                response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_ALARM_WITHOUT_TIME, viewModelStoreOwner));
                response.put(Constants.RESPONSE_INTENT, Constants.INTENT_ALARM_WITHOUT_TIME);
            }
        } else if (intentPrediction.equals(Constants.INTENT_ALARM_WITHOUT_TIME)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_ALARM_WITHOUT_TIME, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_ALARM_WITHOUT_TIME);
        }  else if (intentPrediction.equals(Constants.INTENT_TIMER)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_TIMER, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_TIMER);

        } else if (intentPrediction.equals(Constants.INTENT_TIMER_WITHOUT_TIME)) {
            String time = extractTime(prompt, viewModelStoreOwner);
            if(setTimer(time)){
                response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_TIMER_WITHOUT_TIME, viewModelStoreOwner));
                response.put(Constants.RESPONSE_INTENT, Constants.INTENT_TIMER_WITHOUT_TIME);

            }else{
                response.put(Constants.RESPONSE, "sorry, couldn't set a timer");
            }
        } else if (intentPrediction.equals(Constants.INTENT_YOUTUBE)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_YOUTUBE, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_YOUTUBE);

        } else if (intentPrediction.equals(Constants.INTENT_WHATSAPP)) {
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_WHATSAPP, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_WHATSAPP);

        } else if (intentPrediction.equals(Constants.INTENT_GENERAQA)){
            response.put(Constants.NEEDS_DIALOG, "true");
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERAQA, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_GENERAQA);
        }
        else{
            response.put(Constants.RESPONSE, "Sorry couldn't discern what you're trying to say");
        }
        return response;
    }

    public String doTask(String intent, String prompt, ViewModelStoreOwner viewModelStoreOwner){
        if(intent.equals(Constants.INTENT_GENERATE_TEXT)){
            String generatedText = generateText(prompt, viewModelStoreOwner);
            return generatedText;
        }else if(intent.equals(Constants.INTENT_GENERAQA)){
            String generalQA = generalQA(prompt, viewModelStoreOwner);
            return generalQA;
        }else if(intent.equals(Constants.INTENT_YOUTUBE)){
            openYoutube(context, "SEqx4UarLHE");
        }
        return "";
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
    // GENERAL QA
    private String generalQA(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        String response = "";
        CompletableFuture<String> generalQA = t.generalQATaskAsync(prompt, viewModelStoreOwner);
        try {
            response = generalQA.get();
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
    private boolean openYoutube(Context context, String id){
        boolean success = false;
        callApi();
//        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
//        Intent webIntent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("http://www.youtube.com/watch?v=" + id));
//        try {
//            context.startActivity(appIntent);
//        } catch (ActivityNotFoundException ex) {
//            context.startActivity(webIntent);
//        }
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


    // youtube api
    private void callApi(){
        Call<YoutubeDataModel> YoutubeDataCall = ApiController.getInstance()
                .getApi()
                .getYoutubeSearch(
                        "AIzaSyCdoNFF2vjxZOj7CDt6WhkOLMsSluIsCss",
                        "relevance",
                        "great are you lord by micheal smith",
                        "snippet",
                        "video"
                );

        YoutubeDataCall.enqueue(new Callback<YoutubeDataModel>() {
            @Override
            public void onResponse(Call<YoutubeDataModel> call, Response<YoutubeDataModel> response) {
                Log.d(TAG, "onResponse: " + response.body());
                Log.d(TAG, "onResponse: " + response.body().getItems());
                Log.d(TAG, "onResponse: " + response.code());
                Log.d(TAG, "onResponse: " + response.body().getItems());

                Item[] items = response.body().getItems();
                Log.d(TAG, "onResponse: " + items[0].getId());
                Id id = items[0].getId();
                Log.d(TAG, "onResponse: " + id.videoId);

                openYoutubeVideoWithId(id.videoId);

            }

            @Override
            public void onFailure(Call<YoutubeDataModel> call, Throwable t) {

            }
        });
    }
    private void openYoutubeVideoWithId(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

}



