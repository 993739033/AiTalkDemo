package com.app.aitalkdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by 知らないのセカイ on 2017/5/3.
 */

public class MyViewDragHelper extends FrameLayout {
    private ViewDragHelper myDrag;

    public MyViewDragHelper(Context context) {
        super(context);
    }

    public MyViewDragHelper(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public MyViewDragHelper(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        myDrag = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == contentView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (isleft){
               newleft = Math.min(Math.max(left, -dragwidth), 0);
                   }else{
                    newleft = Math.max(Math.min(left, dragwidth), 0);
                }
                return newleft;
            }

            private int draggx;

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                draggx = left;
                if (Btn_delete.getVisibility() == View.GONE) {
                    Btn_delete.setVisibility(VISIBLE);
                }
                if (changedView == contentView) {
                    Btn_delete.offsetLeftAndRight(dx);
                    invalidate();
                }

            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                Boolean settleOpenTo = false;
                if (isleft) {
                    if (xvel < -1.3) {
                        settleOpenTo = true;
                    } else if (draggx <= -dragwidth / 2) {
                        settleOpenTo = true;
                    }
                    settleDestx= settleOpenTo ? -dragwidth : 0;
                }else {
                    if (xvel >1.3) {
                        settleOpenTo = true;
                    } else if (draggx >= dragwidth / 2) {
                        settleOpenTo = true;
                    }
                    settleDestx= settleOpenTo ? dragwidth : 0;
                }

                myDrag.smoothSlideViewTo(contentView, settleDestx, 0);

                ViewCompat.postInvalidateOnAnimation(MyViewDragHelper.this);
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return myDrag.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        myDrag.processTouchEvent(event);
        return true;
    }

    private TextView contentView;
    private Button Btn_delete;
    private int dragwidth;
    private Boolean isleft=true;
    int settleDestx;
    int newleft;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = (TextView) getChildAt(0);
        Btn_delete = (Button) getChildAt(1);
        if(Btn_delete.getId()==R.id.This_Btn){
            isleft=false;
        }
        if (Btn_delete.getVisibility() == VISIBLE) {
            Btn_delete.setVisibility(View.GONE);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (myDrag.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isleft) {
            Btn_delete.layout(contentView.getRight(), contentView.getTop(),
                    contentView.getRight() + Btn_delete.getMeasuredWidth(), contentView.getBottom());
        }else{
            Btn_delete.layout(contentView.getLeft()-contentView.getMeasuredWidth(), contentView.getTop(),
                    contentView.getLeft(), contentView.getBottom());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        dragwidth = Btn_delete.getMeasuredWidth();
        Btn_delete.measure(Btn_delete.getMeasuredWidth(), heightMeasureSpec);
    }
}
