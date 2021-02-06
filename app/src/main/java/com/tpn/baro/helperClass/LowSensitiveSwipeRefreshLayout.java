package com.tpn.baro.helperClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

public class LowSensitiveSwipeRefreshLayout extends SwipeRefreshLayout {
    private float mInitialDownY;
    private float mInitialUpY;
    private int mTouchSlop = 300;
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void setProgressViewOffset(boolean scale, int start, int end) {
        super.setProgressViewOffset(scale, start, end);
    }

    @Override
    public int getProgressViewStartOffset() {
        return super.getProgressViewStartOffset();
    }

    @Override
    public int getProgressViewEndOffset() {
        return super.getProgressViewEndOffset();
    }

    @Override
    public void setProgressViewEndTarget(boolean scale, int end) {
        super.setProgressViewEndTarget(scale, end);
    }

    @Override
    public void setSlingshotDistance(int slingshotDistance) {
        super.setSlingshotDistance(slingshotDistance);
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
    }

    public LowSensitiveSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public LowSensitiveSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return super.getChildDrawingOrder(childCount, i);
    }

    @Override
    public void setOnRefreshListener(@Nullable OnRefreshListener listener) {
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
    public void setProgressBackgroundColorSchemeResource(int colorRes) {
        super.setProgressBackgroundColorSchemeResource(colorRes);
    }

    @Override
    public void setProgressBackgroundColorSchemeColor(int color) {
        super.setProgressBackgroundColorSchemeColor(color);
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
    public int getProgressCircleDiameter() {
        return super.getProgressCircleDiameter();
    }

    @Override
    public boolean canChildScrollUp() {
        return super.canChildScrollUp();
    }

    @Override
    public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback callback) {
        super.setOnChildScrollUpCallback(callback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            mInitialDownY = ev.getY();
        }
        if(action == MotionEvent.ACTION_UP) {
            mInitialUpY = ev.getY();
        }
        if(action == MotionEvent.ACTION_MOVE) {
            final float yDiff = ev.getY() - mInitialDownY;
            if(yDiff < 150) {
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        super.requestDisallowInterceptTouchEvent(b);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public int getNestedScrollAxes() {
        return super.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        super.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return super.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return super.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        super.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return super.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return super.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return super.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
