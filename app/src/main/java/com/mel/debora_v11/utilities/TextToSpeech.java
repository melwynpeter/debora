package com.mel.debora_v11.utilities;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

public class TextToSpeech {
    private Context context;
    private android.speech.tts.TextToSpeech t1;

    private String text;


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
}
