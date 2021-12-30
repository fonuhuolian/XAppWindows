package org.fonuhuolian.xappwindows.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

public class CircleWebview extends WebView {

    private int width;

    private int height;

    private int mRadius = 0;

    public CircleWebview(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public CircleWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public CircleWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    // This method gets called when the view first loads, and also whenever the
    // view changes. Use this opportunity to save the view's width and height.
    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

        width = newWidth;

        height = newHeight;

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRadius == 0)
            return;

        Path path = new Path();

        path.setFillType(Path.FillType.INVERSE_WINDING);

        path.addRoundRect(new RectF(getScrollX(), getScrollY(), getScrollX() + width, getScrollY() + height), mRadius, mRadius, Path.Direction.CW);

        canvas.drawPath(path, createPorterDuffClearPaint());
    }

    private Paint createPorterDuffClearPaint() {
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.FILL);

        paint.setAntiAlias(true);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        return paint;
    }

}
