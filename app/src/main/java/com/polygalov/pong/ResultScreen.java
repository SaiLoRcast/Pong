package com.polygalov.pong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultScreen extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity_screen);
        final TextView gameResult =
                (TextView) findViewById(R.id.game_result);

        String result = getIntent().getStringExtra("GAME_RESULT");
        if (result.equals("WIN")) {
            gameResult.setText(R.string.win);
        } else {
            gameResult.setText(R.string.lose);
        }

        RelativeLayout relativeLayout = findViewById(R.id.game_result_layout);
        relativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}