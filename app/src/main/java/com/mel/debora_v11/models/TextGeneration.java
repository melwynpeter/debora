package com.mel.debora_v11.models;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TextGeneration extends ViewModel {
    private Context context;
    private TextView textView;
    private String prompt;
    private String apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o";
    private MutableLiveData<String> generatedTextViewText;


    public TextGeneration(Context context, TextView textView, String prompt) {
        this.context = context;
        this.textView = textView;
        this.prompt = prompt;
    }

    public MutableLiveData<String> getCurrentName() {
        if (generatedTextViewText == null) {
            generatedTextViewText = new MutableLiveData<String>();
        }
        return generatedTextViewText;
    }

    public void generateText(){
        // For text-only input
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-pro",
                /* apiKey */ apiKey);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(prompt)
                .build();

        Executor executor = AsyncTask.THREAD_POOL_EXECUTOR;

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();
                    System.out.println(resultText);
                    Log.d("mydebtest", "done");
                    textView.setText(resultText);
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, context.getMainExecutor());
        }
    }
    public static CompletableFuture<String> generateAsyncContent(){
        return CompletableFuture.supplyAsync(() -> {
            return "Async task content completed";
        });
    }
}
