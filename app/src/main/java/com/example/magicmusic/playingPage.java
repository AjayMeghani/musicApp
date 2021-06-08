package com.example.magicmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playingPage extends AppCompatActivity {
    TextView textView;
    ImageView play,next,previous;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textcontent;
    int positon;
    SeekBar seekBar;
    Thread updateSeekBar;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeekBar.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_playing_page);
        textView=findViewById(R.id.textView);
        seekBar=findViewById(R.id.seekBar);
        play=findViewById(R.id.play_btn);
        next=findViewById(R.id.next_btn);
        previous=findViewById(R.id.previous_btn);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songsList");
        textcontent =intent.getStringExtra("currentSong");
        textView.setText(textcontent);
        textView.setSelected(true);
        positon = intent.getIntExtra("postion",0);
        Uri uri =Uri.parse(songs.get(positon).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int currentPostion = 0;
                try {
                    while (currentPostion<mediaPlayer.getDuration()){
                        currentPostion =mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPostion);
                         sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                super.run();
            }
        };
         updateSeekBar.start();
         play.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (mediaPlayer.isPlaying()){
                     play.setImageResource(R.drawable.play);
                     mediaPlayer.pause();
                 }else {
                     play.setImageResource(R.drawable.pause);
                     mediaPlayer.start();
                 }
             }
         });
         previous.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mediaPlayer.stop();
                 mediaPlayer.release();
                 if (positon!=0){
                     positon = positon-1;

                 }else {
                     positon= songs.size()-1;
                 }
                 Uri uri =Uri.parse(songs.get(positon).toString());
                 mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                 mediaPlayer.start();
                 play.setImageResource(R.drawable.pause);
                 seekBar.setMax(mediaPlayer.getDuration());
                 textcontent =songs.get(positon).getName().toString();
                 textView.setText(textcontent);
             }
         });
         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mediaPlayer.stop();
                 mediaPlayer.release();
                 if (positon!=songs.size()-1){
                     positon = positon+1;

                 }else {
                     positon= songs.size()-1;
                 }
                 Uri uri =Uri.parse(songs.get(positon).toString());
                 mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                 mediaPlayer.start();
                 play.setImageResource(R.drawable.pause);
                 seekBar.setMax(mediaPlayer.getDuration());
                 textcontent =songs.get(positon).getName().toString();
                 textView.setText(textcontent);
             }
         });

    }
}