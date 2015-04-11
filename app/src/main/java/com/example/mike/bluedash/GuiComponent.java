package com.example.mike.bluedash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Mikes Gaming on 3/10/2015.
 */
public class GuiComponent extends View {
    int x;
    int y;
    int height;
    int width;
    int color;

    int min;
    int max;
    int progress;

    Paint fillPaint;
    Paint outlinePaint;
    Paint textPaint;
    Paint backgroundPaint;

    GuiComponent(Context context,int x, int y, int width, int height,int min, int max, int progress, int color){
        super(context);
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.color = color;

        this.min = min;
        this.max = max;
        this.progress = progress;

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(color);
        fillPaint.setStrokeWidth(0);
        fillPaint.setStyle(Paint.Style.FILL);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int tempColor = Color.rgb(Color.red(color)/2,Color.green(color)/2,Color.blue(color)/2);
        backgroundPaint.setColor(tempColor);
        backgroundPaint.setStrokeWidth(0);
        backgroundPaint.setStyle(Paint.Style.FILL);

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
        canvas.drawRect(x, y, x+width, y+height, backgroundPaint);
        canvas.drawRect(x, y-((float)progress/(max-min))*(height)+height, x+width, y+height, fillPaint);

        fillPaint.setColor(color);
        fillPaint.setStrokeWidth(0);
        fillPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x+width, y+height, outlinePaint);

        String displayText = String.valueOf(progress);
        int textLength = Math.round(textPaint.measureText(displayText, 0, displayText.length()));
        canvas.drawText(displayText,(x*2+width-textLength)/2,(y*2+height)/2,textPaint);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;

        setMeasuredDimension(width, height);
    }
}
