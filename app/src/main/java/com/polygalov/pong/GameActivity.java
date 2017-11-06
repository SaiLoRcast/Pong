package com.polygalov.pong;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;


public class GameActivity extends Activity implements GameView.OnGameFinishListener {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        gameView = new GameView(this, size.x, size.y);
        gameView.setGameFinishListener(this);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public void onGameFinish(String result) {
        Intent i = new Intent(this, ResultScreen.class);
        i.putExtra("GAME_RESULT", result);
        startActivity(i);
        finish();
    }
}
