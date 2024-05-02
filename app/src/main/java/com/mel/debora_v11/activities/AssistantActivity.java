package com.mel.debora_v11.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mel.debora_v11.R;
import com.mel.debora_v11.databinding.ActivityAssistantBinding;
import com.mel.debora_v11.utilities.AssistantHelper;
import com.mel.debora_v11.utilities.AudioClassificationHelper;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.OnTimerTickListener;
import com.mel.debora_v11.utilities.mTextToSpeech;
import com.mel.debora_v11.utilities.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class AssistantActivity extends AppCompatActivity implements OnTimerTickListener {

    private ActivityAssistantBinding binding;

    private final int RECORD_AUDIO_REQUEST_CODE = 100;

    SpeechRecognizer speechRecognizer;

    private Intent speechRecognizerIntent;

    private int count = 0;
    mTextToSpeech textToSpeech;
    mTextToSpeech tts;

    private boolean needOneMoreSpeech = false;

    private String TAG = "deb11*";

    private float rms;
    private ArrayList<Float> rmsArr;

    private Timer timer;

    private HashMap<String, String> assistantResponse;

    private CompletableFuture<String> future;

    private AudioClassificationHelper audioHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAssistantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SEGMENTED_SESSION, RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SEGMENTED_SESSION, RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS);

//        speechRecognizer.startListening(speechRecognizerIntent);
        speechRecognizer.startListening(speechRecognizerIntent);


        // INITIALIZE TEXT TO SPEECH
        textToSpeech = new mTextToSpeech();
        tts = new mTextToSpeech();

        rmsArr = new ArrayList<>();
        timer = new Timer(this);
        timer.start();

        setListeners();
        speechRecognizerResult();
    }

    private void setListeners(){
//        binding.buttonMic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(count == 0){
//                    count = 1;
//                    speechRecognizer.startListening(speechRecognizerIntent);
//                }
//                else{
//                    count = 0;
//                    speechRecognizer.stopListening();
//                }
//            }
//        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               speechRecognizer.stopListening();
               finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }


    private void speechRecognizerResult(){
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {
//                Log.d(TAG, "onRmsChanged: " + rmsdB);
                float rms = rmsdB;
                sendRms(rms);
            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
//                finish();
                // stop visualizer
                binding.waveformView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(int error) {
                if(!needOneMoreSpeech && !(assistantResponse != null && assistantResponse.get(Constants.NEEDS_DIALOG).equals("true"))){
                    finish();
                }
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                binding.output.setText(data.get(0));
                speechRecognizer.stopListening();

                // prompt
                String prompt = data.get(0);

                // get response
                String response = sendData(prompt);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
//                ArrayList data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                String word = (String) data.get(data.size() - 1);
////                recognisedText.setText(word);
//                binding.output.setText(word);
//                finish();
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }

            @Override
            public void onSegmentResults(@NonNull Bundle segmentResults) {
//                ArrayList<String> data = segmentResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                binding.output.setText(data.get(0));
//                speechRecognizer.stopListening();
            }

            @Override
            public void onEndOfSegmentedSession() {
                RecognitionListener.super.onEndOfSegmentedSession();
//                finish();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RECORD_AUDIO_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private String sendData(String prompt){
        prompt = prompt.toLowerCase();
        AssistantHelper assistantHelper = new AssistantHelper(this);
        assistantResponse = assistantHelper.getResponse(prompt, this);

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Log.d(TAG, "sendData: run async start");

            Log.d(TAG, "sendData: " + assistantResponse);
            textToSpeech.convertTextToSpeech(AssistantActivity.this, assistantResponse.get(Constants.RESPONSE));
            Log.d(TAG, "sendData: run async end");

        });
        String response = assistantResponse.get(Constants.RESPONSE);
        String needsDialog = assistantResponse.get(Constants.NEEDS_DIALOG);
        String responseExtra = assistantResponse.get(Constants.RESPONSE_EXTRA);

        String finalPrompt = prompt;
        future.thenAccept((result) -> {
            Log.d(TAG, "sendData: thenAccept start");
            if(response != null && needsOneMoreSpeech(response)){
                recreate();
            }
            else if (assistantResponse != null && needsDialog != null && needsDialog.equals("true")){
//                String taskString = getTask(assistantResponse.get(Constants.RESPONSE_INTENT), finalPrompt);
//                showBottomDialog(AssistantActivity.this, taskString);

                    CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                        String taskString = getTask(assistantResponse.get(Constants.RESPONSE_INTENT), finalPrompt);
                        return taskString;
                    });
                    future1.thenAccept(result1 -> {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showBottomDialog(AssistantActivity.this, result1, assistantResponse.get(Constants.RESPONSE_INTENT));
                            }
                        });
                    });
                    future1.join();
            }
            else if (assistantResponse != null && responseExtra != null && !responseExtra.equals("false")){

                getTask(assistantResponse.get(Constants.RESPONSE_INTENT), responseExtra);
                finish();

//                String taskString = getTask(assistantResponse.get(Constants.RESPONSE_INTENT), finalPrompt);
//                showBottomDialog(AssistantActivity.this, taskString);

//                    CompletableFuture<Void> future1 = CompletableFuture.supplyAsync(() -> {
//                        String taskString = getTask(assistantResponse.get(Constants.RESPONSE_INTENT), finalPrompt);
//                    });
//                    future1.thenAccept(result1 -> {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showBottomDialog(AssistantActivity.this, result1, assistantResponse.get(Constants.RESPONSE_INTENT));
//                            }
//                        });
//                    });
//                    future1.join();
            }else if (assistantResponse != null && needsDialog != null && needsDialog.equals("false")){

                getTask(assistantResponse.get(Constants.RESPONSE_INTENT), finalPrompt);
                finish();

            }
            else{
                finish();
            }
            Log.d(TAG, "sendData: thenAccept end");

        });

        future.join();
        Log.d(TAG, "sendData: sendData end");
        return assistantResponse.get(Constants.RESPONSE);
    }
    private String getTask(String intent, String prompt){
        AssistantHelper assistantHelper = new AssistantHelper(this);
        return assistantHelper.doTask(intent, prompt, this);
    }

    private boolean needsOneMoreSpeech(String response){
        if(response.endsWith("*")){
            needOneMoreSpeech = true;
            return true;
        }else{
            needOneMoreSpeech = false;
            return false;
        }
    }

    private void sendRms(float rms){
        this.rms = rms;
        if(rms > 0.0f) {
            rmsArr.add(rms);
//            Log.d(TAG, "sendRms: ADDEDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
        }
    }

    @Override
    public void onTimerTick(@NonNull String duration) {
//        Log.d(TAG, "onTimerTick: " + duration);


        binding.waveformView.addAmplitude(rms);
    }

    private void showBottomText(){

    }

    private void showBottomDialog(Context context, String taskString, String intent){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);

        ImageView closeDialogButton = dialog.findViewById(R.id.closeDialogButton);

        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(tts.isUtterance()){
                    tts.stopUtterance();
                }
                finish();
            }
        });

        TextView textView = dialog.findViewById(R.id.taskText);
        // set text
        if(taskString != ""){
            if(intent == Constants.INTENT_GENERAQA){
                tts.convertTextToSpeech(this, taskString);
            }
            textView.setText(taskString);
        }

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(tts.isUtterance()){
                    tts.stopUtterance();
                }
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    private void tts(String response){
        textToSpeech.convertTextToSpeech(this, response);
    }

}