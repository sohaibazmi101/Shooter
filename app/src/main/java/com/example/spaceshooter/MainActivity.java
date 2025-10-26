package com.example.spaceshooter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnShop = findViewById(R.id.btnShop);

        btnStart.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GameActivity.class)));

        btnShop.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ShopActivity.class)));
    }
}
