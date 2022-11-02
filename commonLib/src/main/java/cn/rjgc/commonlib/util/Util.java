package cn.rjgc.commonlib.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

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

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() < 1;
    }
}
