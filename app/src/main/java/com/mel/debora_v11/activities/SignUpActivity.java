package com.mel.debora_v11.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mel.debora_v11.R;
import com.mel.debora_v11.databinding.ActivitySignUpBinding;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setBackward();
        setListeners();
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.debora_bg2_out, null));
    }
    private void setBackward(){
//        Intent intent = new Intent(getApplicationContext(), )
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }
    private void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK){
                        if(o.getData() != null){
                            Uri imageUri = o.getData().getData();
                            try{
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                binding.imageProfile.setImageBitmap(bitmap);
                                binding.textAddImage.setVisibility(View.GONE);
                                encodedImage = encodeImage(bitmap);
                            }catch(FileNotFoundException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails(){
        if (binding.username.getText().toString().trim().isEmpty()){
            showToast("Enter a Username");
            return false;
        }
        else if(binding.emailAddress.getText().toString().trim().isEmpty()){
            showToast("Enter an email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.emailAddress.getText().toString()).matches()){
            showToast("Invalid email");
            return false;
        }
        else if(binding.password.getText().toString().trim().isEmpty()){
            showToast("Enter a password");
            return false;
        }
        else if (binding.confirmPassword.getText().toString().trim().isEmpty()){
            showToast("Enter a password to confirm");
            return false;
        }
        else if(!binding.confirmPassword.getText().toString().equals(binding.password.getText().toString())){
            showToast("Passwords do not match");
            return false;
        }
        else {
            return true;
        }
    }
    private void signUp(){
        String username = binding.username.getText().toString();
        String emailAddress = binding.emailAddress.getText().toString();
        String password = binding.password.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_USERNAME, username);
        user.put(Constants.KEY_EMAIL, emailAddress);
        user.put(Constants.KEY_PASSWORD, password);
        user.put(Constants.KEY_IMAGE, encodedImage);
        db.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("mydebtest", "user added successfully" + documentReference);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                        preferenceManager.putString(Constants.KEY_USERNAME, username);
                        preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("mydebtest", "Error adding user", e);
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setListeners(){
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidSignUpDetails()){
                    signUp();
                }
                else{
                    showToast("some error occured");
                }
            }
        });
        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.signUpButton.setEnabled(false);
//            binding.signUpButton.setVisibility(View.INVISIBLE);
//            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.signUpButton.setEnabled(true);
//            binding.progressBar.setVisibility(View.INVISIBLE);
//            binding.signUpButton.setVisibility(View.VISIBLE);
        }

    }
}