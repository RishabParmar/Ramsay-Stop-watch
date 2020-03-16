package com.example.parma.ramsayalarm;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SeekBar seekTimer;
    TextView timer;
    Button startAlarm;
    Button stopAlarm;
    CountDownTimer countDowner;
    MediaPlayer screamRamsay;
    ImageView ramsay;
    // without static, total was causing certain issues
    String screamClips[] = { "cartwheelcut", "idiotsandwichfinalcut", "lsaucefinalcut"};
    String timerText;
    static int total;

    public void convertSecondsToMinutesSecondsFormat(int time) {
        String secondPart;
        int first = time/60;
        int second = time%60;
        if(second < 10) { secondPart = "0"+ Integer.toString(second); }
        else { secondPart = Integer.toString(second); }
        timerText = Integer.toString(first) + ":" + secondPart;
    }

    public void stopCountDown (View view) {
        if(screamRamsay.isPlaying()) { screamRamsay.stop(); }
        seekTimer.setEnabled(true);
        initializeSeekBar();
        stopAlarm.setVisibility(View.INVISIBLE);
        countDowner.cancel();
    }

    public void countDown(View view) {
        ramsay = findViewById(R.id.ramsay);
        if(ramsay.getVisibility() == View.VISIBLE) { ramsay.setVisibility(View.INVISIBLE); }
        String params[] = timerText.split(":");
        int minutes = Integer.parseInt(params[0]);
        int seconds = Integer.parseInt(params[1]);
        total = minutes*60 + seconds;

        countDowner = new CountDownTimer(total*1000, 1000) {

            @Override
            public void onTick(long l) {
                total--;
                convertSecondsToMinutesSecondsFormat((int)l/1000);
                timer.setText(timerText);
            }

            @Override
            public void onFinish() {
                // Play the gordan ramsey music
                // Randomizing the scream from the list of available clips
                String scream = screamClips[new Random().nextInt(screamClips.length)];
                screamRamsay = MediaPlayer.create(MainActivity.this, getApplicationContext().getResources()
                        .getIdentifier(scream, "raw", getApplicationContext().getPackageName()));
                screamRamsay.start();

                // Making gordon appear in a cool enlarging animation
                ramsay.setScaleX(0f);
                ramsay.setScaleY(0f);
                ramsay.setVisibility(View.VISIBLE);
                ramsay.animate().scaleX(1f).scaleY(1f).setDuration(500);

                // reset the members
                initializeSeekBar();
                stopAlarm.setVisibility(View.INVISIBLE);
                seekTimer.setEnabled(true);
            }
        }.start();

        seekTimer.setEnabled(false);

        // Turn the stop alarm button on
        stopAlarm = findViewById(R.id.stopAlarm);
        stopAlarm.setVisibility(View.VISIBLE);
        startAlarm.setVisibility(View.INVISIBLE);
    }

    public void initializeSeekBar() {
        seekTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                startAlarm.setVisibility(View.VISIBLE);
                convertSecondsToMinutesSecondsFormat(i);
                timer.setText(timerText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = findViewById(R.id.timerText);
        seekTimer = findViewById(R.id.timeSeekBar);
        startAlarm = findViewById(R.id.startAlarm);

        // SeekBar logic
        seekTimer.setMax(500);
        seekTimer.setMin(1);
        initializeSeekBar();
}
}
