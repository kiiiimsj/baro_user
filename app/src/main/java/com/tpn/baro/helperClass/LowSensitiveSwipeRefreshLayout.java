package com.tpn.baro.helperClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

public class LowSensitiveSwipeRefreshLayout extends SwipyRefreshLayout {
    private float mInitialDownY;
    private int mTouchSlop = 300;


    public LowSensitiveSwipeRefreshLayout(Context context) {
        super(context);
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
    }

    public LowSensitiveSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return super.getChildDrawingOrder(childCount, i);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
    }

    @Override
    public void setProgressBackgroundColor(int colorRes) {
        super.setProgressBackgroundColor(colorRes);
    }

    @Override
    public void setColorScheme(int... colors) {
        super.setColorScheme(colors);
    }

    @Override
    public void setColorSchemeResources(int... colorResIds) {
        super.setColorSchemeResources(colorResIds);
    }

    @Override
    public void setColorSchemeColors(int... colors) {
        super.setColorSchemeColors(colors);
    }

    @Override
    public boolean isRefreshing() {
        return super.isRefreshing();
    }

    @Override
    public void setDistanceToTriggerSync(int distance) {
        super.setDistanceToTriggerSync(distance);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean canChildScrollUp() {
        return super.canChildScrollUp();
    }

    @Override
    public boolean canChildScrollDown() {
        return super.canChildScrollDown();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mInitialDownY = ev.getY();
            if(action == MotionEvent.ACTION_MOVE) {
                final float yDiff = ev.getY() - mInitialDownY;
                if (yDiff < mTouchSlop) {
                    return false;
                }
            }
        }
        if (action == MotionEvent.ACTION_UP) {
            mInitialDownY = ev.getY();
            if(action == MotionEvent.ACTION_MOVE) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        super.requestDisallowInterceptTouchEvent(b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public SwipyRefreshLayoutDirection getDirection() {
        return super.getDirection();
    }

    @Override
    public void setDirection(SwipyRefreshLayoutDirection direction) {
        super.setDirection(direction);
    }
}
