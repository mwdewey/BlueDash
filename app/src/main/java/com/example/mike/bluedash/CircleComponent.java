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

    final double lineStartAngle = (4.0 * Math.PI) / 3.0;
    float lineX;
    float lineY;

    Paint fillPaint;
    Paint outlinePaint;
    Paint textPaint;
    Paint backgroundPaint;

    public CircleComponent(Context context, int x, int y, int radius,int min, int max, int progress, int color){
        super(context);

        this.x = x;
        this.y = y;
        this.radius = radius;
        this.progress = progress;
        this.color = color;
        this.lineX = (float)(x + Math.cos(lineStartAngle) * (float)radius);
        this.lineY = (float)(y - Math.sin(lineStartAngle) * (float)radius);

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
        canvas.drawCircle(x, y, radius, fillPaint);
        canvas.drawLine(x, y, lineX, lineY, outlinePaint);


        //these values are used to draw the dashed line
        int numDashes = 10; // will actually be one more than the number here

        for (int i = 0; i <= numDashes; i++) {
            double percent = (double)i/(double)numDashes;
            double angleChange = percent * ((5.0 * Math.PI) / 3.0);
            double lineAngle = lineStartAngle - angleChange;
            float innerDashX = (float)(x + Math.cos(lineAngle) * (float)radius*(0.9));
            float innerDashY = (float)(y - Math.sin(lineAngle) * (float)radius*(0.9));
            float outerDashX = (float)(x + Math.cos(lineAngle) * (float)radius*(1.0));
            float outerDashY = (float)(y - Math.sin(lineAngle) * (float)radius*(1.0));
            canvas.drawLine(innerDashX, innerDashY, outerDashX, outerDashY, outlinePaint);

            float textX = (float)(x + Math.cos(lineAngle) * (float)radius*(0.7)) - 40;
            float textY = (float)(y - Math.sin(lineAngle) * (float)radius*(0.7));
            canvas.drawText("" + (((double)100/numDashes)*i),textX,textY,textPaint);
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;

        setMeasuredDimension(width, height);
    }

    public void updateLine(int progress) {
        double percent = progress/100.0;
        double angleChange = percent * ((5.0 * Math.PI) / 3.0);
        double lineAngle = lineStartAngle - angleChange;
        this.lineX = (float)(x + Math.cos(lineAngle) * (float)radius);
        this.lineY = (float)(y - Math.sin(lineAngle) * (float)radius);
    }

}
