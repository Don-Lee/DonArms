package cn.rjgc.commonlib.util.shake;

import android.view.View;

/**
 * Date 2021/9/29
 *
 * @author Don
 */
public abstract class OnAntiShakeClickListener implements View.OnClickListener {
    private Long lastClickTime = 0L;
    private final Long FILTER_TIMEM = 1000L;

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - lastClickTime >= FILTER_TIMEM) {
            lastClickTime = System.currentTimeMillis();
            onAntiShakeClick();
        }
    }

    public abstract void onAntiShakeClick();
}
