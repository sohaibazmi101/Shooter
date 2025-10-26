package com.example.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Player {
    public int x, y;
    public int width = DEFAULT_WIDTH;
    public int height = DEFAULT_HEIGHT;
    public static final int DEFAULT_WIDTH = 60;
    public static final int DEFAULT_HEIGHT = 60;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
    }

    public void draw(Canvas c, Paint p) {
        // draw a simple triangle-like ship for quick visuals
        float cx = x + width / 2f;
        float topY = y;
        float leftX = x;
        float rightX = x + width;
        float bottomY = y + height;
        // draw triangle
        p.setStyle(Paint.Style.FILL);
        c.drawPath(new android.graphics.Path() {{
            moveTo(cx, topY);
            lineTo(leftX, bottomY);
            lineTo(rightX, bottomY);
            close();
        }}, p);
    }

    public Rect getRect() {
        return new Rect(x, y, x + width, y + height);
    }
}
