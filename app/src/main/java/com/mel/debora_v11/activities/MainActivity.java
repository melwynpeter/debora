package com.mel.debora_v11.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mel.debora_v11.R;
import com.mel.debora_v11.databinding.ActivityMainBinding;
import com.mel.debora_v11.fragments.AccountFragment;
import com.mel.debora_v11.fragments.ChatFragment;
import com.mel.debora_v11.fragments.HistoryFragment;
import com.mel.debora_v11.fragments.HomeFragment;
import com.mel.debora_v11.fragments.RoutineFragment;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;
import com.mel.debora_v11.utilities.TextToSpeech;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private PreferenceManager preferenceManager;


    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        textToSpeech = new TextToSpeech();
//        getTokenAndLoadUserData();
        getToken();
        setListeners();


        // fragments
        binding.bottomNavigationView.setBackground(null);
        replaceFragment(new HomeFragment());

        }

    @Override
    protected void onStart() {
        super.onStart();
        if(binding.bottomAppbar.getTranslationY() == binding.bottomAppbar.getHeight()){
            binding.bottomNavigationView.animate().translationY(0);
            binding.bottomAppbar.animate().translationY(0);
        }
    }

    private void replaceFragment(Fragment fragment){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        }

    private void setListeners(){

        // bottom nav prob check
        if(binding.bottomAppbar.isScrolledDown()){
            binding.bottomNavigationView.animate().translationY(0);
            binding.bottomAppbar.animate().translationY(0);
        }
//        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            }
            else if(item.getItemId() == R.id.routine) {
                replaceFragment(new RoutineFragment());
            }
            else if(item.getItemId() == R.id.chat) {
//                replaceFragment(new ChatFragment());
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom  ).replace(R.id.frame_layout, (new ChatFragment())).addToBackStack(null).commit();
//                binding.bottomNavigationView.animate().translationY(binding.bottomNavigationView.getHeight());
//                binding.bottomAppbar.animate().translationY(binding.bottomAppbar.getHeight());
                startActivity(new Intent(this, ChatActivity.class));
            }
            else if(item.getItemId() == R.id.history) {
                replaceFragment(new HistoryFragment());
            }
            else if(item.getItemId() == R.id.account) {
                replaceFragment(new AccountFragment());
            }

            return true;
        });
    }
    private void getTokenAndLoadUserData(){
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        binding.imageProfile.setImageBitmap(bitmap);
//        if(preferenceManager.getString(Constants.KEY_IMAGE) != null) {
//
//        }
    }
    private void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(Constants.KEY_COLLECTION_USERS)
                .document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        Log.d("mydebtest", "document refernce is : " + documentReference);
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        showToast("Sign in Succesfull!!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        showToast("Unable to update token.");
                    }
                });
    }

    private void signOut(){
        showToast("signing out");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        preferenceManager.clear();
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Unable to sign out");
                    }
                });
    }





}