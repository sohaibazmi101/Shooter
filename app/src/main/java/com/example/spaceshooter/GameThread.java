package com.example.spaceshooter;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;
    private boolean running = false;

    private static final int TARGET_FPS = 60;
    private static final long FRAME_TIME = 1000 / TARGET_FPS;

    public GameThread(SurfaceHolder holder, GameView view) {
        this.surfaceHolder = holder;
        this.gameView = view;
    }

    public void setRunning(boolean isRunning) {
        this.running = isRunning;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;

        while (running) {
            startTime = System.currentTimeMillis();
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (canvas != null) {
                        gameView.update();
                        gameView.draw(canvas);
                    }
                }
            } finally {
                if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
            }

            timeMillis = System.currentTimeMillis() - startTime;
            waitTime = FRAME_TIME - timeMillis;
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    // ignore interruption
                }
            }
        }
    }
}
