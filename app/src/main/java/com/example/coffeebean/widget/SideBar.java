package com.example.coffeebean.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.coffeebean.R;


public class SideBar extends View {
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private String[] c = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z", "#"};

    private int textSize;
    private int index;
    private LetterTouchListener mTouchListener;
    private TextView mTextDialog;

    private int choose = -1;// 选中
    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                14, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mPaint.setColor(getResources().getColor(R.color.color_1));
        mPaint.setAntiAlias(true);
    }
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        float strHeight = (float) (mHeight / c.length);
        for (int i = 0; i < c.length; i++) {
            float x = mWidth / 2 - mPaint.measureText(c[i]) / 2;
            float y = strHeight * i + strHeight;

            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setAntiAlias(true);
            if (i == choose) {
                mPaint.setColor(Color.parseColor("#EEFFFFFF"));
                mPaint.setFakeBoldText(true);
            }
            mPaint.setColor(getResources().getColor(R.color.color_1));
            canvas.drawText(c[i], x, y, mPaint);
            mPaint.reset();// 重置画笔
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        float y = event.getY();
        index = (int) (y / mHeight * c.length);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchListener.setLetter(c[index]);
                break;
            case MotionEvent.ACTION_MOVE:
                if (index > -1 && index < c.length) {
                    mTouchListener.setLetter(c[index]);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void setLetterTouchListener(LetterTouchListener listener) {
        mTouchListener = listener;
    }

    public interface LetterTouchListener {
        void setLetter(String letter);
    }

}