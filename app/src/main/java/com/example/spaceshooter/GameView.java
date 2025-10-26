package com.example.spaceshooter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private com.example.spaceshooter.Player player;
    private ArrayList<com.example.spaceshooter.Bullet> bullets;
    private ArrayList<com.example.spaceshooter.Asteroid> asteroids;
    private int score = 0;
    private int health = 3;
    private boolean gameOver = false;
    private TextView statsView;
    private SharedPreferences prefs;
    private long lastAsteroidTime = 0;
    private final Random random = new Random();

    // Constructor for programmatic creation
    public GameView(Context context) {
        super(context);
        init(context);
    }

    // Constructor for XML inflation
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // Constructor with style attribute (optional)
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // Shared initialization
    private void init(Context context) {
        getHolder().addCallback(this);
        prefs = context.getSharedPreferences("GameData", Context.MODE_PRIVATE);
        setFocusable(true);
    }

    public void setStatsTextView(TextView view) {
        this.statsView = view;
    }

    private void initGame() {
        int px = Math.max(0, getWidth() / 2 - com.example.spaceshooter.Player.DEFAULT_WIDTH / 2);
        int py = Math.max(0, getHeight() - 140);
        player = new com.example.spaceshooter.Player(px, py);
        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();
        score = 0;
        health = 3;
        gameOver = false;
        lastAsteroidTime = System.currentTimeMillis();
        updateStatsText();
    }

    public synchronized void startGame() {
        initGame();
        if (thread == null || !thread.isAlive()) {
            thread = new GameThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        } else {
            thread.setRunning(true);
        }
    }

    public synchronized void stopGame() {
        gameOver = true;
        if (thread != null) {
            thread.setRunning(false);
            try {
                thread.join(300);
            } catch (InterruptedException e) {
                e.addSuppressed(e);
            }
            thread = null;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        startGame();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (player != null) {
            player.x = Math.max(0, width / 2 - player.width / 2);
            player.y = Math.max(0, height - 140);
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        stopGame();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (player == null || gameOver) return true;

        float tx = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                bullets.add(new com.example.spaceshooter.Bullet(player.x + player.width / 2 - com.example.spaceshooter.Bullet.DEFAULT_WIDTH / 2, player.y));
            case MotionEvent.ACTION_MOVE:
                int newX = (int) (tx - player.width / 2f);
                newX = Math.max(0, Math.min(newX, getWidth() - player.width));
                player.x = newX;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void update() {
        if (gameOver) return;
        long now = System.currentTimeMillis();

        if (now - lastAsteroidTime > 800) {
            int maxX = Math.max(1, getWidth() - com.example.spaceshooter.Asteroid.DEFAULT_SIZE);
            int ax = random.nextInt(maxX);
            asteroids.add(new com.example.spaceshooter.Asteroid(ax, -com.example.spaceshooter.Asteroid.DEFAULT_SIZE));
            lastAsteroidTime = now;
        }

        if (bullets != null) for (com.example.spaceshooter.Bullet b : bullets) b.update();
        if (asteroids != null) for (com.example.spaceshooter.Asteroid a : asteroids) a.update();

        if (bullets != null) {
            bullets.removeIf(b -> b.y + b.height < 0);
        }

        if (asteroids != null) {
            asteroids.removeIf(a -> a.y - a.size > getHeight());
        }

        // Bullet <-> Asteroid collisions
        if (bullets != null && asteroids != null) {
            for (Iterator<com.example.spaceshooter.Bullet> bi = bullets.iterator(); bi.hasNext(); ) {
                com.example.spaceshooter.Bullet b = bi.next();
                for (Iterator<com.example.spaceshooter.Asteroid> ai = asteroids.iterator(); ai.hasNext(); ) {
                    com.example.spaceshooter.Asteroid a = ai.next();
                    if (Rect.intersects(b.getRect(), a.getRect())) {
                        bi.remove();
                        ai.remove();
                        score += 10;
                        int coins = prefs.getInt("coins", 0);
                        prefs.edit().putInt("coins", coins + 10).apply();
                        break;
                    }
                }
            }
        }

        // Player <-> Asteroid collisions
        if (player != null && asteroids != null) {
            for (Iterator<com.example.spaceshooter.Asteroid> ai = asteroids.iterator(); ai.hasNext(); ) {
                com.example.spaceshooter.Asteroid a = ai.next();
                if (Rect.intersects(player.getRect(), a.getRect())) {
                    ai.remove();
                    health--;
                    if (health <= 0) {
                        gameOver = true;
                        if (thread != null) thread.setRunning(false);
                    }
                }
            }
        }

        updateStatsText();
    }

    private void updateStatsText() {
        if (statsView != null) {
            String s = "Score: " + score + " | Health: " + health;
            statsView.post(() -> statsView.setText(s));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas == null) return;

        canvas.drawColor(Color.BLACK);
        Paint p = new Paint();

        if (player != null) {
            p.setColor(Color.CYAN);
            player.draw(canvas, p);
        }

        if (bullets != null) {
            p.setColor(Color.YELLOW);
            for (com.example.spaceshooter.Bullet b : bullets) b.draw(canvas, p);
        }

        if (asteroids != null) {
            p.setColor(Color.rgb(255, 112, 67));
            for (com.example.spaceshooter.Asteroid a : asteroids) a.draw(canvas, p);
        }

        if (gameOver) {
            p.setColor(Color.RED);
            p.setTextSize(60);
            String text = "GAME OVER";
            float textWidth = p.measureText(text);
            canvas.drawText(text, (getWidth() - textWidth) / 2f, getHeight() / 2f, p);
        }
    }
}
