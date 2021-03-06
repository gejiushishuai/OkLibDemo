package com.oklib.view.stv;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.oklib.R;

/**
 * 创建时间：2017/7/11
 * 编写者：黄伟才
 * 功能描述：
 */

public class MoveEffectAdjuster extends SuperTextView.Adjuster {

    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
    private float totalWidth = 50f;
    private float startLocation = -99999f;
    private Paint paint = new Paint();
    private Path firstLinePath = new Path();
    private Path secondLinePath = new Path();
    private RectF rectF = new RectF();
    private float density;

    @Override
    public void adjust(SuperTextView v, Canvas canvas) {
        int width = v.getWidth();
        int height = v.getHeight();
        if (density == 0) {
            density = v.getResources().getDisplayMetrics().density;
        }
        if (startLocation == -99999f) {
            startLocation = (float) (Math.random() * width);
        }
        if (startLocation < -(totalWidth * density + height * Math.tan(60))) {
            startLocation = width;
        }
        int firstLineWidth = (int) (totalWidth * density / 5 * 3);
        double velocity = 1.5 * density;
        startLocation = ((float) (startLocation - velocity));
        firstLinePath.reset();
        firstLinePath.moveTo(startLocation, height);
        firstLinePath.lineTo(startLocation + firstLineWidth, height);
        firstLinePath.lineTo((float) (startLocation + firstLineWidth + height * Math.tan(60)), 0);
        firstLinePath.lineTo((float) (startLocation + height * Math.tan(60)), 0);
        firstLinePath.close();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(v.getResources().getColor(R.color.oklib_black));
        paint.setStyle(Paint.Style.FILL);
        int secondLineWidth = (int) (totalWidth * density / 5);
        secondLinePath.reset();
        secondLinePath.moveTo(startLocation + secondLineWidth * 4, height);
        secondLinePath.lineTo(startLocation + secondLineWidth * 4 + secondLineWidth, height);
        secondLinePath.lineTo(
                (float) (startLocation + secondLineWidth * 4 + secondLineWidth + height * Math.tan(60)),
                0);
        secondLinePath.lineTo((float) (startLocation + secondLineWidth * 4 + height * Math.tan(60)),
                0);
        secondLinePath.close();

        firstLinePath.addPath(secondLinePath);
        rectF.setEmpty();
        rectF.set(0, 0, width, height);
        // 创建一个图层，在图层上演示图形混合后的效果
        int sc = canvas.saveLayer(0, 0, width, height, null, Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        paint.setColor(v.getResources().getColor(R.color.oklib_purple));
        canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
        paint.setXfermode(xfermode);
        paint.setColor(v.getResources().getColor(R.color.oklib_opacity_3_white));
        canvas.drawPath(firstLinePath, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(sc);
    }
}
