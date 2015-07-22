package com.wonyoung.lightcontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.view.View;

/**
 * Created by wonyoung.jang on 15. 7. 22.
 */
public class ColorPickerView extends View {
    private static final int[] COLORS = {0xFFFF0000, 0xFFFF00FF,
            0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};
    private RectF mColorWheelRectangle = new RectF();
    private float mTranslationOffset;
    private float mColorWheelRadius;
    private int cx;
    private int cy;
    private Paint mColorPaint;

    public ColorPickerView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mTranslationOffset, mTranslationOffset);

        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mColorPaint.setColor(caclulateColor(mAngle));
        mColorPaint.setStyle(Paint.Style.FILL);

        cx = 0;
        cy = 0;
        Shader s = new SweepGradient(cx, cy, COLORS, null);
        Paint colorPickerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        colorPickerPaint.setShader(s);
        colorPickerPaint.setStyle(Paint.Style.STROKE);
        colorPickerPaint.setStrokeWidth(100);

        canvas.drawOval(mColorWheelRectangle, colorPickerPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getSize(widthMeasureSpec);
        int height = getSize(heightMeasureSpec);
        int min = Math.min(width, height);

        mTranslationOffset = min * 0.5f;

        mColorWheelRadius = min * 0.3f;

        setMeasuredDimension(width, height);
        mColorWheelRectangle.set(-mColorWheelRadius, -mColorWheelRadius, mColorWheelRadius, mColorWheelRadius);
    }

    private int getSize(int measureSpec) {

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int intrinsicSize = 100;

        if (mode == MeasureSpec.EXACTLY) {
            return size;
        } else if (mode == MeasureSpec.AT_MOST) {
            return Math.min(intrinsicSize, size);
        }
        return intrinsicSize;
    }
}
