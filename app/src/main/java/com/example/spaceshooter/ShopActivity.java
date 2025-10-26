package com.example.spaceshooter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView tvCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        prefs = getSharedPreferences("GameData", MODE_PRIVATE);
        tvCoins = findViewById(R.id.tvCoins);
        updateCoins();

        Button btnSpeed = findViewById(R.id.btnSpeed);
        Button btnHealth = findViewById(R.id.btnHealth);
        Button btnBullet = findViewById(R.id.btnBullet);

        btnSpeed.setOnClickListener(v -> buy("Speed Boost", 100));
        btnHealth.setOnClickListener(v -> buy("Extra Health", 150));
        btnBullet.setOnClickListener(v -> buy("Double Bullet", 200));
    }

    private void updateCoins() {
        int coins = prefs.getInt("coins", 0);
        tvCoins.setText("Coins: " + coins);
    }

    private void buy(String name, int cost) {
        int coins = prefs.getInt("coins", 0);
        if (coins >= cost) {
            prefs.edit().putInt("coins", coins - cost).apply();
            Toast.makeText(this, "✅ Purchased " + name, Toast.LENGTH_SHORT).show();
            updateCoins();
        } else {
            Toast.makeText(this, "❌ Not enough coins", Toast.LENGTH_SHORT).show();
        }
    }
}
