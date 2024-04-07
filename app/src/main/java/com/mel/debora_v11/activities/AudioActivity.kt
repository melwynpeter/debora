package com.mel.debora_v11.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mel.debora_v11.R
import com.mel.debora_v11.databinding.ActivityAudioBinding
import com.mel.debora_v11.databinding.ActivityMainBinding
import com.mel.debora_v11.utilities.AudioClassificationHelper

class AudioActivity : AppCompatActivity() {
    private lateinit var activityAudioBinding: ActivityAudioBinding


    private lateinit var audioHelper: AudioClassificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAudioBinding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(activityAudioBinding.root)
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
            // (https://issuetracker.google.com/issues/139738913)
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }
}