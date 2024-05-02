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
import com.mel.debora_v11.utilities.database.AlarmAdder;
import com.mel.debora_v11.utilities.database.GeneralQAAdder;
import com.mel.debora_v11.utilities.database.GeneratedTextsAdder;
import com.mel.debora_v11.utilities.database.TimerAdder;
import com.mel.debora_v11.utilities.database.TodoAdder;

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

    private String videoQuery = "";
    private String callRecepient = "";

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
        intents.add(Constants.INTENT_ALARM);
        intents.add(Constants.INTENT_TIMER);
        intents.add(Constants.INTENT_REMINDER);
        intents.add(Constants.INTENT_TODO);
        intents.add(Constants.INTENT_OPEN_YOUTUBE);
        intents.add(Constants.INTENT_OPEN_YOUTUBE_AND_PLAY);
        intents.add(Constants.INTENT_GENERAQA);
        intents.add(Constants.INTENT_PHONE_CALL);
        intents.add(Constants.INTENT_PLAY_MUSIC);
        intents.add(Constants.INTENT_INVALID_ACTION);
        intents.add(Constants.INTENT_NOT_SURE);
        return intents;
    }

    public HashMap<String, String> getResponse(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        HashMap<String, String> response = new HashMap<>();
        response.put(Constants.NEEDS_DIALOG, "false");
        response.put(Constants.RESPONSE_INTENT, "false");
        response.put(Constants.RESPONSE_EXTRA, "false");

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

        } else if (intentPrediction.equals(Constants.INTENT_GENERATE_EMAIL)) { // GENERATE TEXT
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERATE_EMAIL, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_GENERATE_EMAIL);

        }  else if (intentPrediction.equals(Constants.INTENT_ALARM)) { //SET ALARM
            String time = extractTime(prompt, viewModelStoreOwner);
            if(time != null && !time.equals("null")) {
                if (setAlarm(time)) {
                    Log.d(TAG, "getResponse: " + time);
                    response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_ALARM, time, viewModelStoreOwner));
                    response.put(Constants.RESPONSE_INTENT, Constants.INTENT_ALARM);

                } else {
                    response.put(Constants.RESPONSE, "sorry, couldn't set an alarm");
                }
            } else {
                response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_ALARM_WITHOUT_TIME, viewModelStoreOwner));
                response.put(Constants.RESPONSE_INTENT, Constants.INTENT_ALARM_WITHOUT_TIME);
            }
        } else if (intentPrediction.equals(Constants.INTENT_TIMER)) { // TIMER
            String time = extractTime(prompt, viewModelStoreOwner);
            if(time != null && !time.equals("null")) {
                if (setTimer(time)) {
                    Log.d(TAG, "getResponse: " + time);
                    response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_TIMER, time, viewModelStoreOwner));
                    response.put(Constants.RESPONSE_INTENT, Constants.INTENT_TIMER);

                } else {
                    response.put(Constants.RESPONSE, "sorry, couldn't set an alarm");
                }
            } else {
                response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_TIMER_WITHOUT_TIME, viewModelStoreOwner));
                response.put(Constants.RESPONSE_INTENT, Constants.INTENT_TIMER_WITHOUT_TIME);
            }

        }else if (intentPrediction.equals(Constants.INTENT_REMINDER)) { // REMINDER
            String time = extractTimeAndRemind(prompt, viewModelStoreOwner);
            if(time != null && !time.equals("null")) {
                String timeAndRemind[] = seperateTimeAndRemind(time);
                if (setReminder(timeAndRemind[0], timeAndRemind[1])) {
                    Log.d(TAG, "getResponse: " + time);
                    response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_REMINDER, timeAndRemind[1], viewModelStoreOwner));
                    response.put(Constants.RESPONSE_INTENT, Constants.INTENT_REMINDER);

                } else {
                    response.put(Constants.RESPONSE, "sorry, couldn't set an alarm");
                }
            }
        } else if (intentPrediction.equals(Constants.INTENT_TODO)) { // TO DO
            String todo = extractTodo(prompt, viewModelStoreOwner);
            if(todo != null && !todo.equals("null")) {
                if (addTodo(todo)) {
                    Log.d(TAG, "getResponse: " + todo);
                    response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_TODO, todo, viewModelStoreOwner));
                    response.put(Constants.RESPONSE_INTENT, Constants.INTENT_TODO);

                } else {
                    response.put(Constants.RESPONSE, "sorry, couldn't set an alarm");
                }
            }
        } else if (intentPrediction.equals(Constants.INTENT_OPEN_YOUTUBE)) { // OPEN YOUTUBE
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_OPEN_YOUTUBE, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_OPEN_YOUTUBE);

        } else if (intentPrediction.equals(Constants.INTENT_OPEN_YOUTUBE_AND_PLAY)) { // OPEN YOUTUBE AND PLAY
            videoQuery = extractYoutubeVideoQuery(prompt, viewModelStoreOwner);
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_OPEN_YOUTUBE_AND_PLAY, videoQuery, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_OPEN_YOUTUBE_AND_PLAY);
            response.put(Constants.RESPONSE_EXTRA, videoQuery);
        } else if (intentPrediction.equals(Constants.INTENT_WHATSAPP)) { // WHATSAPP
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_WHATSAPP, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_WHATSAPP);

        } else if (intentPrediction.equals(Constants.INTENT_GENERAQA)){ // GENERAL QA
            response.put(Constants.NEEDS_DIALOG, "true");
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_GENERAQA, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_GENERAQA);
        } else if (intentPrediction.equals(Constants.INTENT_PHONE_CALL)){ // PHONE CALL
            callRecepient = extractCallRecipient(prompt, viewModelStoreOwner);
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_PHONE_CALL, callRecepient, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_PHONE_CALL);
            response.put(Constants.RESPONSE_EXTRA, callRecepient);
        }else if (intentPrediction.equals(Constants.INTENT_PLAY_MUSIC)){ // PLAY MUSIC
            response.put(Constants.RESPONSE, getTextResponse(prompt, Constants.INTENT_PLAY_MUSIC, viewModelStoreOwner));
            response.put(Constants.RESPONSE_INTENT, Constants.INTENT_PLAY_MUSIC);
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
        }else if(intent.equals(Constants.INTENT_OPEN_YOUTUBE)){
            openYoutube();
        }else if(intent.equals(Constants.INTENT_OPEN_YOUTUBE_AND_PLAY)){
            openYoutubeAndPlay(prompt);
        }else if(intent.equals(Constants.INTENT_PHONE_CALL)){
            makeCall(prompt);
        }else if(intent.equals(Constants.INTENT_PLAY_MUSIC)){
            playMusic(prompt);
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
    private String getTextResponse(String prompt, String intent, String replaceString, ViewModelStoreOwner viewModelStoreOwner) {
        String response = "";
        Responses responses = new Responses();
        ArrayList<String> intentResponses = responses.getResponses(intent);
        response = select(prompt, intentResponses,  viewModelStoreOwner);
        String swappedResponse = swapString(response, replaceString);
        return swappedResponse;
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

        String finalResponse = response;
        new Thread(new Runnable() {
            @Override
            public void run() {
                GeneratedTextsAdder generatedTextsAdder = new GeneratedTextsAdder(context);
                generatedTextsAdder.addGeneratedTexts(finalResponse, prompt);
            }
        }).start();

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

        String finalResponse = response;
        new Thread(new Runnable() {
            @Override
            public void run() {
                GeneralQAAdder generalQAAdder = new GeneralQAAdder(context);
                generalQAAdder.addGeneralQA(finalResponse, prompt);
            }
        }).start();
        return response;
    }

    // SET ALARM
    private boolean setAlarm(String time){
        boolean success = false;
        AlarmController alarmController = new AlarmController(context);
        if(!time.contains("null")){
            if(alarmController.setAlarm(time)) {
                success = true;
            }

        }
        return success;
    }

    // SET TIMER
    private boolean setTimer(String time){
        boolean success = false;
        TimerController timerController = new TimerController(context);
        if(!time.contains("null")){
            if(timerController.setTimer(time)) {
                success = true;
            }
        }
        return success;
    }

    // SET REMINDER
    private boolean setReminder(String time, String remind){
        boolean success = false;

        if(time != ""){
        ReminderController reminderController = new ReminderController(context);
            if(reminderController.setReminder(time, remind)){
                success = true;
            }
        }
        return success;
    }
    private boolean addTodo(String todo){
        boolean success = false;
        TodoAdder todoAdder = new TodoAdder(context);
        if(todoAdder.addTodo(todo)){
         success = true;
        }
        return success;
    }

    private boolean makeCall(String call){
        boolean success = false;
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+90223 + "" + 64183));
        context.startActivity(callIntent);
        return success;
    }

    // OPEN YOUTUBE
    private boolean openYoutube(){
        boolean success = false;
        openYoutubeVideoWithId("");
        return success;
    }

    // OPEN YOUTUBE AND PLAY
    private boolean openYoutubeAndPlay(String videoQuery){
        boolean success = false;
        callApi(videoQuery);
        return success;

    }

    // PLAY MUSIC TODO: PLAY MUSIC**
    private boolean playMusic(String songName){
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
    private String extractTimeAndRemind(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        String time = "";
        CompletableFuture<String> extractedTimeAndRemind = t.extractTimeAndRemindAsync(prompt, viewModelStoreOwner);
        try {
            time = extractedTimeAndRemind.get();
            Log.d(TAG, "extractTimeAndRemind: " + time);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "extractTimeAndRemind: " + time);

        if(!time.equals("null")){
            return time;
        }else{
            return null;
        }
    }
    private String extractTodo(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        String todo = "";
        CompletableFuture<String> extractedTodo = t.extractTodoAsync(prompt, viewModelStoreOwner);
        try {
            todo = extractedTodo.get();
            Log.d(TAG, "extractedTodo: " + todo);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "extractedTodo: " + todo);

        if(!todo.equals("null")){
            return todo;
        }else{
            return null;
        }
    }

    private String extractYoutubeVideoQuery(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        String youtubeVideoQuery = "";
        CompletableFuture<String> extractedYoutubeVideoQuery = t.extractYoutubeVideoQueryAsync(prompt, viewModelStoreOwner);
        try {
            youtubeVideoQuery = extractedYoutubeVideoQuery.get();
            Log.d(TAG, "extractTime: " + youtubeVideoQuery);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "extractYoutubeQuery: " + youtubeVideoQuery);

        if(!youtubeVideoQuery.equals("null")){
            return youtubeVideoQuery;
        }else{
            return null;
        }
    }
    private String extractCallRecipient(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        String callRecipient = "";
        CompletableFuture<String> extractedCallRecipient = t.extractCallRecipientAsync(prompt, viewModelStoreOwner);
        try {
            callRecipient = extractedCallRecipient.get();
            Log.d(TAG, "extractTime: " + callRecipient);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "extractYoutubeQuery: " + callRecipient);

        if(!callRecipient.equals("null")){
            return callRecipient;
        }else{
            return null;
        }
    }

    private String[] seperateTimeAndRemind(String timeAndRemind){
        String timeAndRemindArray[] = new String[2];
        String time = "";
        if(timeAndRemind.contains(",")) {
            timeAndRemindArray = timeAndRemind.split(",");
            if(timeAndRemindArray[0].contains("seconds")){
                time = timeAndRemindArray[0].replace(" seconds", "");
                time = "00:00:" + time;
            }
        }
        return timeAndRemindArray;
    }

    // youtube api
    private void callApi(String query){
//        query = (query == null)? "great are you lord by micheal smith" : query;
        Call<YoutubeDataModel> YoutubeDataCall = ApiController.getInstance()
                .getApi()
                .getYoutubeSearch(
                        "AIzaSyCdoNFF2vjxZOj7CDt6WhkOLMsSluIsCss",
                        "relevance",
                        query,
                        "snippet",
                        "video"
                );

        Log.d(TAG, "callApi: " + query);

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

    private String swapString(String response, String replaceString){
        if(response.contains("{time}")){
            response = response.replace("{time}", replaceString);
        }
        else if (response.contains("{videoQuery}")){
            response = response.replace("{videoQuery}", replaceString);
        }
        else if (response.contains("{callRecipient}")){
            response = response.replace("{callRecipient}", replaceString);
        }else if (response.contains("{todo}")){
            response = response.replace("{todo}", replaceString);
        }else if (response.contains("{question}")){
            response = response.replace("{question}", replaceString);
        }else if (response.contains("{remind}")){
            response = response.replace("{remind}", replaceString);
        }
        return response;
    }


}



