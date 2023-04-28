package usc.yuangang.es.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class MyCircleProgress extends View {

    private static final String TAG = "MyCircleProgress";
    private Paint _paint;
    private RectF _rectF;
    private Rect _rect;
    private int _current = 75, _max = 100;
    private float _arcWidth = 23;
    private float _width;

    public MyCircleProgress(Context context) {
        this(context, null);
    }

    public MyCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _paint = new Paint();
        _paint.setAntiAlias(true);
        _rectF = new RectF();
        _rect = new Rect();
    }

    public void setCurrent(int _current) {
        this._current = _current;
        invalidate();
    }

    public void setMax(int _max) {
        this._max = _max;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        _width = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(_arcWidth);
        _paint.setColor(Color.BLACK);
        float bigCircleRadius = _width / 2;
        float smallCircleRadius = bigCircleRadius - _arcWidth;
        canvas.drawCircle(bigCircleRadius, bigCircleRadius, smallCircleRadius, _paint);
        _paint.setColor(Color.parseColor("#822A3D"));
        _rectF.set(_arcWidth, _arcWidth, _width - _arcWidth, _width - _arcWidth);
        canvas.drawArc(_rectF, 270, _current * 360 / _max, false, _paint);
        String txt = _current * 100 / _max+"";
        _paint.setStrokeWidth(4);
        _paint.setTextSize(40);
        _paint.setStyle(Paint.Style.FILL); // Add this line
        _paint.getTextBounds(txt, 0, txt.length(), _rect);
        _paint.setColor(Color.WHITE);
        canvas.drawText(txt, bigCircleRadius - _rect.width() / 2, bigCircleRadius + _rect.height() / 2, _paint);
    }

}
