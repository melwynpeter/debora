package com.mel.debora_v11.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.mel.debora_v11.R;
import com.mel.debora_v11.databinding.ActivityAudioBinding;
import com.mel.debora_v11.databinding.ActivityMainBinding;
import com.mel.debora_v11.utilities.Assistant;
import com.mel.debora_v11.utilities.AssistantHelper;
import com.mel.debora_v11.utilities.TextToSpeech;

import java.util.ArrayList;

public class AudioActivity extends AppCompatActivity {

    private ActivityAudioBinding binding;

    private final int RECORD_AUDIO_REQUEST_CODE = 100;

    SpeechRecognizer speechRecognizer;

    private Intent speechRecognizerIntent;

    private int count = 0;
    TextToSpeech textToSpeech;

    private boolean needOneMoreSpeech = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAudioBinding.inflate(getLayoutInflater());
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
        textToSpeech = new TextToSpeech();

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

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
//                finish();
            }

            @Override
            public void onError(int error) {
                if(!needOneMoreSpeech){
                    finish();
                }
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                binding.output.setText(data.get(0));
                speechRecognizer.stopListening();
                String response = sendData(data.get(0));
                if(needsOneMoreSpeech(response)){
                    recreate();
                }
                else{
                    finish();
                }
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

    private String sendData(String data){
        data = data.toLowerCase();
        AssistantHelper assistantHelper = new AssistantHelper();
        String response = assistantHelper.getResponse(data);
        Assistant assistant = new Assistant();
        String response1 = assistant.getResponse(data, this);
        textToSpeech.convertTextToSpeech(this, response);
        return response;
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

}