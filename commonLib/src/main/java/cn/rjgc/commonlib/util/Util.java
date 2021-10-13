package cn.rjgc.commonlib.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

/**
 * Date 2021/10/13
 *
 * @author Don
 */
public class Util {
    public static int getColor(Context context,int res) {
        return ContextCompat.getColor(context,res);
    }

    public static Drawable getDrawable(Context context, int res) {
        return ContextCompat.getDrawable(context, res);
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length < 1;
    }
}
