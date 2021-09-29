package cn.rjgc.commonlib.util.keyboard;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import cn.rjgc.commonlib.util.DisplayMetricsHolder;
import cn.rjgc.commonlib.util.PixelUtil;

/**
 * Date 2021/9/28
 * 此文件代码来自com.facebook.react.ReactRootView#CustomGlobalLayoutListener
 * @author Don
 */
public class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private final Rect mVisibleViewArea;
    private final int mMinKeyboardHeightDetected;

    private int mKeyboardHeight = 0;

    /**
     * Activity的根布局(Activity#setContentView方法传入的View) 或 DecorView
     */
    private View mView;
    private OnKeyboardChangedListener mListener;

    /**
     * @param v Activity的根布局(Activity#setContentView方法传入的View) 或 DecorView
     * @param l OnKeyboardChangedListener
     */
    public GlobalLayoutListener(View v, OnKeyboardChangedListener l) {
        mView = v;
        DisplayMetricsHolder.initDisplayMetricsIfNotInitialized(mView.getContext().getApplicationContext());
        mVisibleViewArea = new Rect();
        mMinKeyboardHeightDetected = (int) PixelUtil.toPixelFromDIP(60);
        mListener = l;
    }

    @Override
    public void onGlobalLayout() {
        if (mView == null) {
            return;
        }
        checkForKeyboardEvents();
    }

    private void checkForKeyboardEvents() {
        mView.getRootView().getWindowVisibleDisplayFrame(mVisibleViewArea);
        //noinspection ConstantConditions
        final int heightDiff =
                DisplayMetricsHolder.getWindowDisplayMetrics().heightPixels - mVisibleViewArea.bottom;
        if (mKeyboardHeight != heightDiff && heightDiff > mMinKeyboardHeightDetected) {
            // keyboard is now showing, or the keyboard height has changed
            mKeyboardHeight = heightDiff;
            if (mListener != null) {
                mListener.onChange(
                        true,
                        mKeyboardHeight,
                        mVisibleViewArea.width(),
                        mVisibleViewArea.bottom
                );
            }
        } else if (mKeyboardHeight != 0 && heightDiff <= mMinKeyboardHeightDetected) {
            // keyboard is now hidden
            mKeyboardHeight = 0;
            if (mListener != null) {
                mListener.onChange(
                        false,
                        mKeyboardHeight,
                        mVisibleViewArea.width(),
                        mVisibleViewArea.bottom
                );
            }
        }
    }
}
