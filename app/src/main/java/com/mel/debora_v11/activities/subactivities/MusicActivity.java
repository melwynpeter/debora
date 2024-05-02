package com.mel.debora_v11.activities.subactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.mel.debora_v11.R;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.nio.file.Files;
import java.sql.Array;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {
    private static final int READ_EXTERNAL_AUDIO_REQUEST_CODE = 100;
    String songItem[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, READ_EXTERNAL_AUDIO_REQUEST_CODE);
        }
        getSongs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, READ_EXTERNAL_AUDIO_REQUEST_CODE);
        }
    }
    private ArrayList<File> getAllSongs(File file){
        File[] files = file.listFiles();
        ArrayList<File> songList = new ArrayList<>();

        for (File eachSingFile : files){
            if(eachSingFile.isDirectory() && !eachSingFile.isHidden()){
                songList.addAll(getAllSongs(eachSingFile));
            }
            else{
                if(eachSingFile.getName().endsWith(".mp3") || eachSingFile.getName().endsWith(".wav;")){
                    songList.add(eachSingFile);
                }
            }
        }
        return songList;
    }
    private void getSongs(){
        ArrayList<File> songs = getAllSongs(Environment.getExternalStorageDirectory());
        songItem = new String[songs.size()];
        for(int i = 0; i < songs.size(); i++){
            songItem[i] = songs.get(i).getName().toString();
            Log.d("deb11", "getSongs: " + songItem[i]);
        }
    }
}