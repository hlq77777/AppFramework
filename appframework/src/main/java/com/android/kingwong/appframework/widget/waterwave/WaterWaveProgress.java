
package com.android.kingwong.appframework.widget.waterwave;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.android.kingwong.appframework.R;
import com.android.kingwong.appframework.util.DimensionUtil;

public class WaterWaveProgress extends View {

    private static final int WAVE_PAINT_COLOR = Color.rgb(223, 83, 64);
    // y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 10;
    private static final int OFFSET_Y = 0;
    private static final int TRANSLATE_X_SPEED_ONE = 3;
    private static final int TRANSLATE_X_SPEED_TWO = 2;

    private int duration = 3000;
    private float max = 100;
    private float min = 0;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;

    private ValueAnimator progressValueAnimator;
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private ValueAnimator transtantValueAnimator;

    private float progressLastValue = min;

    private float transtantLastValue = 0;

    private float progress;
    private double heightPiece;

    private float real_progress;

    private int coveringColor;
    private int outerCircleColor;
    private int outerCirclePadding;

    private Paint linePaint;
    private Paint transtantlinePaint;

    private Paint mWavePaint;
    private PaintFlagsDrawFilter mDrawFilter;
    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);

    private int strokeWidth;
    private int lineWidth;
    private int lineSpace;
    //	private int width;
//	private int height;
    private RectF circle;
    private int startAngle = 90;
    private float plusAngle = 360;
    private float space;//水波河虚线的距离

    private float transtantAngle = 360;

    private float offset = 10;

    public WaterWaveProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        initSize(context);
        mXOffsetSpeedOne = WidgetUtil.dipToPx(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = WidgetUtil.dipToPx(context, TRANSLATE_X_SPEED_TWO);

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Style.FILL);
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        mWavePaint.setAlpha(90);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(strokeWidth + offset);
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);

        transtantlinePaint = new Paint();
        transtantlinePaint.setAntiAlias(true);
        transtantlinePaint.setStrokeWidth(strokeWidth + offset);
        transtantlinePaint.setColor(Color.TRANSPARENT);
        transtantlinePaint.setStyle(Paint.Style.STROKE);
        transtantlinePaint.setStrokeJoin(Paint.Join.ROUND);
        transtantlinePaint.setStrokeCap(Paint.Cap.ROUND);
        transtantlinePaint.setAlpha(0);
        transtantlinePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        LinearGradient lg=new LinearGradient(0,0,100,100, Color.RED,Color.BLUE, Shader.TileMode.MIRROR);//颜色渐变
//		linePaint.setShader(lg);
//		linePaint.setPathEffect(new DashPathEffect(new float[]{lineWidth, lineSpace}, 0));

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        progressValueAnimator = new ValueAnimator();
        progressValueAnimator.setInterpolator(interpolator);
        progressValueAnimator.addUpdateListener(new ProgressAnimatorListenerImp());
        transtantValueAnimator = new ValueAnimator();
        transtantValueAnimator.setInterpolator(interpolator);
        transtantValueAnimator.addUpdateListener(new TranstantLineAnimatorListenerImp());

        try {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSize(Context context) {
        this.lineWidth = DimensionUtil.getSizeInPixels(3, context);
        this.lineSpace = DimensionUtil.getSizeInPixels(2, context);
        this.strokeWidth = DimensionUtil.getSizeInPixels(10, context);
    }

    private void initExternalCircle(int width, int height) {
        int padding = strokeWidth / 2;
        circle = new RectF();
        circle.set(padding + 3, padding + 3, width - padding - 3, height - padding - 3);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterWaveProgress);
        coveringColor = typedArray.getColor(R.styleable.WaterWaveProgress_coveringColor, 0);
        outerCircleColor = typedArray.getColor(R.styleable.WaterWaveProgress_outerCircleColor, 0);
        outerCirclePadding = typedArray.getDimensionPixelSize(R.styleable.WaterWaveProgress_outerCirclePadding, 50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawArc(circle, startAngle, plusAngle, false, linePaint);
        canvas.drawArc(circle, -90, transtantAngle, false, transtantlinePaint);

        Path path = new Path();
        canvas.save();
        path.reset();
        canvas.clipPath(path);
        path.addCircle(width / 2, height / 2, width / 2 - space / 2-offset/2, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);
        canvas.setDrawFilter(mDrawFilter);
        if (coveringColor != 0) {
            Paint coveringPaint = new Paint();
            coveringPaint.setAntiAlias(true);
            coveringPaint.setStyle(Style.FILL);
            coveringPaint.setColor(coveringColor);
            coveringPaint.setAlpha(50);
            canvas.drawCircle(width / 2, height / 2, width / 2, coveringPaint);
        }
        if (outerCircleColor != 0) {
            Paint outerCirclePaint = new Paint();
            outerCirclePaint.setAntiAlias(true);
            outerCirclePaint.setStyle(Style.STROKE);
            outerCirclePaint.setColor(outerCircleColor);
            canvas.drawCircle(width / 2, height / 2, width / 2 + outerCirclePadding, outerCirclePaint);
        }
        resetPositonY();
        mWavePaint.setXfermode(xfermode);
        for (int i = 0; i < mTotalWidth; i++) {
            if (progress == 0) {
                break;
            }
            canvas.drawLine(i+space/2, (float) (mTotalHeight - mResetOneYPositions[i] - real_progress * heightPiece)+space/2, i+space/2,
                    mTotalHeight+space/2,
                    mWavePaint);

            canvas.drawLine(i+space/2, (float) (mTotalHeight - mResetTwoYPositions[i] - real_progress * heightPiece)+space/2, i+space/2,
                    mTotalHeight+space/2,
                    mWavePaint);
        }
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;

        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }
        mWavePaint.setXfermode(null);
        postInvalidate();
    }

    private void resetPositonY() {
        int yOneInterval = mYPositions.length - mXOneOffset;
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initExternalCircle(w, h);
        mTotalWidth = (int) (w-space);
        mTotalHeight = (int) (h-space);
        heightPiece = mTotalHeight / max;
        mYPositions = new float[mTotalWidth];
        mResetOneYPositions = new float[mTotalWidth];
        mResetTwoYPositions = new float[mTotalWidth];

        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (progress <= max && progress >= min) {
            animateProgressValue();
        }
        setValue(100);
    }

    private void setRealProgress(float real_progress) {
        this.real_progress = real_progress;
    }

    private class ProgressAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float value = (Float) valueAnimator.getAnimatedValue();
            setRealProgress(value);
            progressLastValue = value;
        }
    }

    private void animateProgressValue() {
        if (progressValueAnimator != null) {
            progressValueAnimator.setFloatValues(progressLastValue, progress);
            progressValueAnimator.setDuration(duration);
            progressValueAnimator.start();
            transtantValueAnimator.setFloatValues(transtantLastValue, 100);
            transtantValueAnimator.setDuration(2000);
            transtantValueAnimator.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        space = 4 * strokeWidth;
        int size;
        int width = (int) (getMeasuredWidth() + space);
        int height = (int) (getMeasuredHeight() + space);


        if (width > height) {
            size = height;
        } else {
            size = width;
        }
        size = (int) (size + offset);
        setMeasuredDimension(size, size);
    }

    public void setValue(float value) {
        this.transtantAngle = (360f * value) / 100;
    }

    private class TranstantLineAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float value = (Float) valueAnimator.getAnimatedValue();
            if(value<=100){
                updateValueNeedle(value);
                transtantLastValue = value;
            }
        }
    }

    private void updateValueNeedle(float value) {
        setValue(value);
    }
}
