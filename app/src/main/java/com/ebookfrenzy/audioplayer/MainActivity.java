package com.ebookfrenzy.audioplayer;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private double startTime = 0;
    private double finalTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private String music_path;

    private Button btn_forward;
    private Button btn_play;
    private Button btn_pause;
    private Button btn_backward;
    private TextView textView2;
    private SeekBar music_bar;

    private Handler durationHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this, R.raw.thisiswhatyoucamefor);
        finalTime = mp.getDuration();

        btn_forward = (Button)findViewById(R.id.btn_forward);
        btn_play = (Button)findViewById(R.id.btn_play);
        btn_pause = (Button)findViewById(R.id.btn_pause);
        btn_backward = (Button)findViewById(R.id.btn_backward);
        music_bar = (SeekBar)findViewById(R.id.music_bar);
        textView2 = (TextView) findViewById(R.id.textView2);
        btn_pause.setEnabled(false);
        music_bar.setMax(mp.getDuration());

        btn_play.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        btn_pause.setEnabled(true);
                        btn_play.setEnabled(false);
                        mp.start();
                        startTime = mp.getCurrentPosition();
                        music_bar.setProgress((int) startTime);
                        durationHandler.postDelayed(updateSeekBarTime, 100);
                    }

                }
        );

        btn_forward.setOnClickListener(
                    new Button.OnClickListener(){
                        public void onClick(View v){
                            int temp = (int)startTime;
                            if((temp-forwardTime) > 0)
                                startTime = startTime - backwardTime;
                            mp.seekTo((int) startTime);


                        }
                    }
            );

        btn_pause.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        btn_pause.setEnabled(false);
                        btn_play.setEnabled(true);
                        mp.pause();
                    }
                }
        );

        btn_backward.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){

                        int temp = (int)startTime;
                        if((temp+backwardTime)<=finalTime){
                            startTime = startTime + forwardTime;
                            mp.seekTo((int) startTime);
                        }
                    }
                }
        );

        music_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mp.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            startTime = mp.getCurrentPosition();
            //set seekbar progress
            music_bar.setProgress((int) startTime);


            long musicMin =TimeUnit.MILLISECONDS.toMinutes((long) startTime);
            long musicSec =TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                            toMinutes((long) startTime));
            String m, s;

            if(musicMin<10)
                m = "0" + Integer.toString((int)musicMin);
            else
                m = Integer.toString((int)musicMin);

            if(musicSec<10)
                s = "0" + Integer.toString((int)musicSec);
            else
                s = Integer.toString((int)musicSec);

            textView2.setText(String.format("Music Player -> %s : %s ", m, s));
//            textView2.setText(String.format("Music Player -> %d : %d ", musicMin, musicSec));

            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }
}
