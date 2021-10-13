package cn.rjgc.commonlib.util;

import android.util.TypedValue;

/**
 * Date 2021/9/28
 * dp、px、sp互转
 * @author Don
 */
public class PixelUtil {
    /**
     * DIP to PX
     * 需要先初始化DisplayMetricsHolder
     */
    public static float toPixelFromDIP(float value) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                DisplayMetricsHolder.getWindowDisplayMetrics());
    }

    /**
     * DIP to PX
     * 需要先初始化DisplayMetricsHolder
     */
    public static float toPixelFromDIP(double value) {
        return toPixelFromDIP((float) value);
    }

    /**
     * SP to PX
     * 需要先初始化DisplayMetricsHolder
     */
    public static float toPixelFromSP(float value) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                value,
                DisplayMetricsHolder.getWindowDisplayMetrics());
    }

    /**
     * SP to PX
     * 需要先初始化DisplayMetricsHolder
     */
    public static float toPixelFromSP(double value) {
        return toPixelFromSP((float) value);
    }

    /**
     * PX to DP
     * 需要先初始化DisplayMetricsHolder
     */
    public static float toDIPFromPixel(float value) {
        return value / DisplayMetricsHolder.getWindowDisplayMetrics().density;
    }
}
