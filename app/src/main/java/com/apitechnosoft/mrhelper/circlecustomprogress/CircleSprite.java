package com.apitechnosoft.mrhelper.circlecustomprogress;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Neeraj on 8/18/2017.
 */

public class CircleSprite extends ShapeSprite {

    @Override
    public ValueAnimator getAnimation() {
        return null;
    }

    @Override
    public void drawShape(Canvas canvas, Paint paint) {
        if (getDrawBounds() != null) {
            int radius = Math.min(getDrawBounds().width(), getDrawBounds().height()) / 2;
            canvas.drawCircle(getDrawBounds().centerX(),
                    getDrawBounds().centerY(),
                    radius, paint);
        }
    }
}
