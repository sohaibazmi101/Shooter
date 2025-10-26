package com.example.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Asteroid {
    public int x, y;
    public int size = DEFAULT_SIZE;
    public int speed = 5;
    public static final int DEFAULT_SIZE = 40;

    public Asteroid(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = DEFAULT_SIZE;
    }

    public void update() {
        y += speed;
    }

    public void draw(Canvas c, Paint p) {
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(x + size / 2f, y + size / 2f, size / 2f, p);
    }

    public Rect getRect() {
        return new Rect(x, y, x + size, y + size);
    }
}
