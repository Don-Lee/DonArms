package cn.rjgc.commonlib.util;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * @author donle
 * 可感知生命周期的Runnable
 */
public abstract class LifecycleRunnable implements Runnable, LifecycleObserver {
    private Lifecycle mLifecycle;
    private boolean isDestroy = false;

    public LifecycleRunnable(Lifecycle lifecycle) {
        mLifecycle = lifecycle;
        // 添加生命周期观察者
        mLifecycle.addObserver(this);
    }

    @Override
    public void run() {
        if (!isDestroy) {
            lifecycleRun();
        }
        mLifecycle.removeObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void lifecycleDestroy() {
        isDestroy = true;
        mLifecycle.removeObserver(this);
    }

    public void lifecycleRun() {

    }
}
