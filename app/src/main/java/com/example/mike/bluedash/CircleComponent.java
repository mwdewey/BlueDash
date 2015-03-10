package com.example.mike.bluedash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Mikes Gaming on 3/10/2015.
 */
public class CircleComponent extends View {
    int x;
    int y;
    int radius;
    int color;

    int min;
    int max;
    int progress;

    Paint fillPaint;
    Paint outlinePaint;
    Paint textPaint;

    public CircleComponent(Context context, int x, int y, int radius,int min, int max, int progress, int color){
        super(context);

        this.x = x;
        this.y = y;
        this.radius = radius;
        this.progress = progress;
        this.color = color;

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(color);
        fillPaint.setStrokeWidth(0);
        fillPaint.setStyle(Paint.Style.FILL);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setColor(Color.BLACK);
        outlinePaint.setStrokeWidth(5);
        outlinePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(40);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawCircle(x, y, radius, fillPaint);

    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;

        setMeasuredDimension(width, height);
    }

}
