/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mel.debora_v11.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.mel.debora_v11.R
import com.mel.debora_v11.databinding.ActivityMainBinding
import com.mel.debora_v11.fragments.AccountFragment
import com.mel.debora_v11.fragments.HistoryFragment
import com.mel.debora_v11.fragments.HomeFragment
import com.mel.debora_v11.fragments.RoutineFragment
import com.mel.debora_v11.utilities.AudioClassificationHelper
import com.mel.debora_v11.utilities.AudioClassificationListener
import com.mel.debora_v11.utilities.Constants
import com.mel.debora_v11.utilities.PreferenceManager
import com.mel.debora_v11.utilities.TextToSpeech
import org.tensorflow.lite.support.label.Category

class MainActivity2 : AppCompatActivity() {
//    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var binding: ActivityMainBinding

    private lateinit var preferenceManager: PreferenceManager


    private lateinit var textToSpeech: TextToSpeech

    private lateinit var audioHelper: AudioClassificationHelper

    private val RECORD_AUDIO_REQUEST_CODE = 100

    private val audioClassificationListener = object : AudioClassificationListener {
        override fun onResult(results: List<Category>, inferenceTime: Long) {
            runOnUiThread {
                if(results.isNotEmpty() && (results.get(0).label.toString() == "debora")) {
                    if(results.get(0).score > 0.900000) {
                        Toast.makeText(
                            applicationContext,
                            results.get(0).label.toString() + results.get(0).score.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(applicationContext, AudioActivity::class.java))
//                        audioHelper.stopAudioClassification()
                    }
                }
            }
        }

        override fun onError(error: String) {
            runOnUiThread {
                Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        preferenceManager = PreferenceManager(applicationContext)
        textToSpeech = TextToSpeech()
//        getTokenAndLoadUserData();
        //        getTokenAndLoadUserData();
        getToken()
        setListeners()


        // Audio Recording permission


        // Audio Recording permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        } else {
            // INIT AUDIO CLASSSIFIER
            audioHelper = AudioClassificationHelper(
                applicationContext,
                audioClassificationListener
            )
        }

        // fragments

        // fragments
        binding.bottomNavigationView.background = null
        replaceFragment(HomeFragment())

        setContentView(binding.root)
    }

    override fun onRestart() {
        super.onRestart()
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        }

        if (audioHelper.recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
            Toast.makeText(this, "already recoding", Toast.LENGTH_SHORT).show()
        }
        if (::audioHelper.isInitialized ) {
            audioHelper.startAudioClassification()
//            audioHelper.initClassifier() // infinite loop
        }

    }

    override fun onStop() {
        super.onStop()
        if (::audioHelper.isInitialized ) {
            audioHelper.stopAudioClassification()
        }
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

    private fun setListeners() {

        // bottom nav prob check
//        if (binding.bottomAppbar.isScrolledDown()) {
//            binding.bottomNavigationView.animate().translationY(0)
//            binding.bottomAppbar.animate().translationY(0)
//        }
        //        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//        });
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.getItemId() == R.id.home) {
                replaceFragment(HomeFragment())
            } else if (item.getItemId() == R.id.routine) {
                replaceFragment(RoutineFragment())
            } else if (item.getItemId() == R.id.chat) {
//                replaceFragment(new ChatFragment());
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom  ).replace(R.id.frame_layout, (new ChatFragment())).addToBackStack(null).commit();
//                binding.bottomNavigationView.animate().translationY(binding.bottomNavigationView.getHeight());
//                binding.bottomAppbar.animate().translationY(binding.bottomAppbar.getHeight());
                startActivity(Intent(this, ChatActivity::class.java))
            } else if (item.getItemId() == R.id.history) {
                replaceFragment(HistoryFragment())
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(AccountFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            updateToken(
                token
            )
        }
    }

    private fun updateToken(token: String) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection(Constants.KEY_COLLECTION_USERS)
            .document(
                preferenceManager.getString(Constants.KEY_USER_ID)
            )
        Log.d("mydebtest", "document refernce is : $documentReference")
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
            .addOnSuccessListener {
                //                        showToast("Sign in Succesfull!!");
            }
            .addOnFailureListener {
                //                        showToast("Unable to update token.");
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                // INIT AUDIO CLASSSIFIER
                audioHelper = AudioClassificationHelper(
                    applicationContext,
                    audioClassificationListener
                )
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
