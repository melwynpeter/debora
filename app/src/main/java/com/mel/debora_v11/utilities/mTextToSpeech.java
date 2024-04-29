package com.mel.debora_v11.utilities;

import android.content.Context;
import android.os.Build;
import android.speech.tts.UtteranceProgressListener;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class mTextToSpeech {
    private Context context;
    private android.speech.tts.TextToSpeech t1;

    private String text;

    private AppCompatActivity activity;

    private boolean isUtteranceStart = false;
    private boolean isUtteranceDone = true;

    public mTextToSpeech(){}
    public mTextToSpeech(AppCompatActivity activity){
        this.activity = activity;
    }


    public void convertTextToSpeech(Context context, String text){
        int success;
        t1 = new android.speech.tts.TextToSpeech(context, new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != android.speech.tts.TextToSpeech.ERROR){
                    t1.setLanguage(Locale.ENGLISH);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        t1.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        t1.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        isUtteranceStart = true;
                        isUtteranceDone = false;
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        isUtteranceStart = false;
                        isUtteranceDone = true;
                    }

                    @Override
                    public void onError(String utteranceId) {

                    }
                });
            }
        });
    }
    public void stopUtterance(){
        if (t1 != null){
            t1.stop();
        }
    }

    public boolean isUtterance(){
        if(t1 != null){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isUtteranceStart(){
        if(isUtteranceStart){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isUtteranceDone(){
        if(isUtteranceDone){
            return true;
        }
        else {
            return false;
        }
    }
}
