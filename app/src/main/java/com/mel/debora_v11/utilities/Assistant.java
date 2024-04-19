package com.mel.debora_v11.utilities;

import androidx.lifecycle.ViewModelStoreOwner;

import com.mel.debora_v11.models.TextGenerationKotlin;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class Assistant {
    private TextGenerationKotlin t = new TextGenerationKotlin();
    private ArrayList<String> intents = new ArrayList<>();

    public ArrayList<String> getIntents() {
        intents.add("greeting");
        intents.add("goodbye");
        intents.add("creator");
        intents.add("name");
        intents.add("email");
        intents.add("alarm");
        intents.add("alarm_with_time");
        intents.add("alarm_with_time_and_date");
        intents.add("timer");
        intents.add("timer_with_time");
        intents.add("youtube");
        return intents;
    }

    public String getResponse(String prompt, ViewModelStoreOwner viewModelStoreOwner){
        String response = "";
        String prediction = textClassification(prompt, getIntents(), viewModelStoreOwner);
        return response;
    }


    private String textClassification(String prompt, ArrayList<String> intent, ViewModelStoreOwner viewModelStoreOwner){
        AtomicReference<String> response = new AtomicReference<>("");
        CompletableFuture<String> generatedText = t.textClassificationAsync(prompt, intent, viewModelStoreOwner);
        generatedText.thenAccept(result -> {
            response.set(result);
        }
        );

        return response.get();
    }

}
