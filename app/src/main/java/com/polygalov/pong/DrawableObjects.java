package com.polygalov.pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class DrawableObjects {

    private Bitmap bitmap;
    protected int x, y;
    private Rect hitbox;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getHitbox() {
        return hitbox;
    }

    public DrawableObjects(Context context, int id, int x, int y) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        this.x = x - bitmap.getWidth() / 2;
        this.y = y - bitmap.getHeight() / 2;

        hitbox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        hitbox.left = x;
        hitbox.top = y;
        hitbox.right = x + bitmap.getWidth();
        hitbox.bottom = y + bitmap.getHeight();
    }
}
