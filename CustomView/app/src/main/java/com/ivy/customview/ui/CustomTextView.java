package com.ivy.customview.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ivy.customview.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Ivy on 2016/12/20.
 *
 * @description:
 */

public class CustomTextView extends View {

    private String mText;
    private int mTextSize;
    private int mTextColor;
    private Paint mPaint;
    private Rect mBound;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);//默认的布局文件调用的是两个参数的构造方法，所以记得让所有的构造调用我们的三个参数的构造，我们在三个参数的构造中获得自定义属性。
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTextView_CustomText:
                    mText = a.getString(attr);
                    break;
                case R.styleable.CustomTextView_CustomTextSize:
                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.0f, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomTextView_CustomTextColor:
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
            }
        }

        a.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);

        mBound = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mText = randomText();
                postInvalidate();
            }
        });
    }

    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append("" + i);
        }

        return sb.toString();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.d("tag", "onDraw...");
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mTextColor);
        canvas.drawText(mText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("tag", "onMeasure...");

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }
}
