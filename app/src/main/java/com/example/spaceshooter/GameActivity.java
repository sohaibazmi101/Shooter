package com.example.spaceshooter;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private TextView tvStats;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvStats = findViewById(R.id.tvStats);
        gameView = findViewById(R.id.gameView);
        btnBack = findViewById(R.id.btnBack);

        gameView.setStatsTextView(tvStats);

        btnBack.setOnClickListener(v -> {
            gameView.stopGame();
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.stopGame();
    }
}
