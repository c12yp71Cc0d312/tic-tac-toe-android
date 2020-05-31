package com.example.soptask2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    GameScreen gameScreenLayout;
    public static String name1;
    public static String name2;
    MediaPlayer win1, win2, draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameScreenLayout = new GameScreen(getApplicationContext(),this);
        Log.d(TAG, "onCreate: name1: " + name1);
        Log.d(TAG, "onCreate: name2: " + name2);
        if(win1 == null)
            win1 = MediaPlayer.create(this, R.raw.win1);
        if(win2 == null)
            win2 = MediaPlayer.create(this, R.raw.win2);
        if(draw == null)
            draw = MediaPlayer.create(this, R.raw.gameover);
        setContentView(gameScreenLayout);
    }

    public String getName1() {
        Log.d(TAG, "getName1: " + name1);
        Intent fromStartScreen = getIntent();
        name1 = fromStartScreen.getStringExtra("name1");
        return name1;
    }

    public String getName2() {
        Log.d(TAG, "getName2: " + name2);
        Intent fromStartScreen = getIntent();
        name2 = fromStartScreen.getStringExtra("name2");
        return name2;
    }

    public void playWin1() {
        win1.start();
    }

    public void playWin2() {
        win2.start();
    }

    public void playDraw() {
        draw.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        win1.release();
        win2.release();
        draw.release();
        win1 = null;
        win2 = null;
        draw = null;
    }
}
