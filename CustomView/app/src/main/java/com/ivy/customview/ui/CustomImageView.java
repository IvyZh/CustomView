package com.ivy.customview.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ivy.customview.R;

/**
 * Created by Ivy on 2016/12/20.
 *
 * @description:
 */

public class CustomImageView extends View {
    int mWidth, mHeight;
    private String mText;
    private int mTextSize;
    private int mTextColor;
    private Bitmap mBitmap;
    private int mImageViewType;
    private Paint mPaint;
    private Rect mTextBound;
    /**
     * 控制整体布局
     */
    private Rect rect;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, 0, defStyleAttr);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomImageView_CustomText:
                    mText = a.getString(attr);
                    break;
                case R.styleable.CustomImageView_CustomTextSize:
                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.0f, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomImageView_CustomTextColor:
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_imgSrc:
                    mBitmap = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imgScaleType:
                    mImageViewType = a.getInt(attr, 0);
                    break;
            }
        }

        a.recycle();

        mTextBound = new Rect();
        rect = new Rect();
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if (widthMode == MeasureSpec.EXACTLY) {//match_parent accurate
            mWidth = widthSize;
        } else {
            int desireByImg = getPaddingRight() + getPaddingLeft() + mBitmap.getWidth();
            int desireByText = getPaddingRight() + getPaddingLeft() + mTextBound.width();

            if (widthMode == MeasureSpec.AT_MOST) {//WARP_CONTENT
                int desire = Math.max(desireByImg, desireByText);
                mWidth = Math.min(desire, widthSize);
            }
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            int desireByImg = getPaddingBottom() + getPaddingTop() + mBitmap.getHeight();
            int desireByText = getPaddingBottom() + getPaddingTop() + mTextBound.height();

            if (heightMode == MeasureSpec.AT_MOST) {
                int desire = Math.max(desireByImg, desireByText);
                mHeight = Math.min(desire, heightSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        /**
         * 边框
         */
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        rect.left = getPaddingLeft();
        rect.right = mWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
         */
        if (mTextBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mText, paint, (float) mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);

        } else {
            //正常情况，将字体居中
            canvas.drawText(mText, mWidth / 2 - mTextBound.width() * 1.0f / 2, mHeight - getPaddingBottom(), mPaint);
        }

        //取消使用掉的快
        rect.bottom -= mTextBound.height();

        if (mImageViewType == 0) {
            canvas.drawBitmap(mBitmap, null, rect, mPaint);
        } else {
            //计算居中的矩形范围
            rect.left = mWidth / 2 - mBitmap.getWidth() / 2;
            rect.right = mWidth / 2 + mBitmap.getWidth() / 2;
            rect.top = (mHeight - mTextBound.height()) / 2 - mBitmap.getHeight() / 2;
            rect.bottom = (mHeight - mTextBound.height()) / 2 + mBitmap.getHeight() / 2;

            canvas.drawBitmap(mBitmap, null, rect, mPaint);
        }

    }

//        @Override
//        protected void onDraw(Canvas canvas) {
//            mPaint.setStrokeWidth(6);
//            mPaint.setColor(Color.GREEN);
//            mPaint.setStyle(Paint.Style.STROKE);
//            canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
//    //        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
//
//            rect.left = getPaddingLeft();
//            rect.right = mWidth - getPaddingRight();
//            rect.top = getPaddingTop();
//            rect.bottom = mHeight - getPaddingBottom();
//
//            mPaint.setColor(mTextColor);
//            mPaint.setStyle(Paint.Style.STROKE);
//
//            if (mTextBound.width() > mWidth) {
//                TextPaint paint = new TextPaint();
//                String msg = TextUtils.ellipsize(mText, paint, mWidth - getPaddingRight() - getPaddingLeft(), TextUtils.TruncateAt.END).toString();
//                Log.d("tag", msg);
//                canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
//            } else {
//                canvas.drawText(mText, mWidth / 2 - mTextBound.width() / 2, mHeight - getPaddingBottom(), mPaint);//TODO
//            }
//
//
//            rect.bottom -= mTextBound.height();
//
//            if (mImageViewType == 0) {
//                canvas.drawBitmap(mBitmap, null, rect, mPaint);
//            } else {
//                rect.left = mWidth / 2 - mBitmap.getWidth() / 2;
//                rect.right = mWidth / 2 + mBitmap.getWidth() / 2;
//                rect.top = (mHeight - mTextBound.height()) / 2 - mBitmap.getHeight() / 2;
//                rect.bottom = (mHeight - mTextBound.height()) / 2 + mBitmap.getHeight() / 2;
//                canvas.drawBitmap(mBitmap, null, rect, mPaint);
//            }
//        }
}
