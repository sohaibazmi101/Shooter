package com.example.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bullet {
    public int x, y;
    public int width = DEFAULT_WIDTH;
    public int height = DEFAULT_HEIGHT;
    public int speed = 20;
    public static final int DEFAULT_WIDTH = 8;
    public static final int DEFAULT_HEIGHT = 20;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
    }

    public void update() {
        y -= speed;
    }

    public void draw(Canvas c, Paint p) {
        p.setStyle(Paint.Style.FILL);
        c.drawRect(x, y, x + width, y + height, p);
    }

    public Rect getRect() {
        return new Rect(x, y, x + width, y + height);
    }
}
