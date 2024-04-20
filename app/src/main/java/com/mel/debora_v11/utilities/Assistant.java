package com.mel.debora_v11.utilities;

import android.util.Log;

import androidx.lifecycle.ViewModelStoreOwner;

import com.mel.debora_v11.models.TextGenerationKotlin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class Assistant {
    private TextGenerationKotlin t = new TextGenerationKotlin();
    private ArrayList<String> intents = new ArrayList<>();

    final String TAG = "deb11";
    public ArrayList<String> getIntents() {
        intents.add("greeting");
        intents.add("goodbye");
        intents.add("creator_of_model");
        intents.add("name_of_model");
        intents.add("generate_email");
        intents.add("generate_text");
        intents.add("alarm");
        intents.add("alarm_with_time");
        intents.add("alarm_with_time_and_date");
        intents.add("timer");
        intents.add("timer_with_time");
        intents.add("youtube");
        intents.add("general_qa");
        return intents;
    }

    public String getResponse(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        String response = "";
        String prediction = textClassification(prompt, getIntents(), viewModelStoreOwner);
        Log.d(TAG, "getResponse: " + prediction);

        return response;
    }

    private String getIntent(String speechCommand, ViewModelStoreOwner viewModelStoreOwner){
        String intent = "";



        //This first removes all non-letter characters, folds to lowercase, then splits the input, doing all the work in a single line
//        String[] words = instring.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
//        s = s.replace("[^a-zA-Z ]", "").toLowerCase();
//        if(greetingPatterns.contains(s)){
//            intent = Constants.INTENT_GREETING;
//        } else if (goodbyePatterns.contains(s)) {
//            intent = Constants.INTENT_GOODBYE;
//        } else if (creatorPatterns.contains(s)) {
//            intent = Constants.INTENT_CREATOR;
//        } else if (namePatterns.contains(s)) {
//            intent = Constants.INTENT_NAME;
//        }else if (generateTextPatterns.contains(s)) {
//            intent = Constants.INTENT_GENERATE_TEXT;
//        }else if (doesStartsWithCollection(s, cleanedGenerateTextWithSubjectPatterns)) {
//            intent = Constants.INTENT_GENERATE_TEXT_WITH_SUBJECT;
//            subject = extractSubject(s, cleanedGenerateTextWithSubjectPatterns);
//        }else if (generateEmailPatterns.contains(s)) {
//            intent = Constants.INTENT_GENERATE_EMAIL;
//        }else if (doesStartsWithCollection(s, cleanedGenerateEmailWithSubjectPattern)) {
//            intent = Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT;
//            subject = extractSubject(s, cleanedGenerateEmailWithSubjectPattern);
//        }else if (setAlarmPatterns.contains(s)) {
//            intent = Constants.INTENT_ALARM;
//        }else if (doesStartsWithCollection(s, cleanedSetAlarmWithTimePatterns)) {
//            intent = Constants.INTENT_ALARM_WITH_TIME;
//            subject = extractSubject(s, cleanedSetAlarmWithTimePatterns);
//        }else if (doesStartsWithCollection(s, cleanedSetAlarmWithTimeAndDatePatterns)) {
//            intent = Constants.INTENT_ALARM_WITH_TIME_AND_DATE;
//            subject = extractSubject(s, cleanedSetAlarmWithTimeAndDatePatterns);
//
//        }else if (doesStartsWithCollection(s, cleanedGenerateEmailWithSubjectPattern)) {
//            intent = Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT;
//            subject = extractSubject(s, cleanedGenerateEmailWithSubjectPattern);
//        }
        return intent;
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

}



