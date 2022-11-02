package cn.rjgc.commonlib.util;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * @author donle
 * 可感知生命周期的Runnable
 */
public abstract class LifecycleRunnable implements Runnable, LifecycleObserver {
    private boolean isStop;
    public LifecycleRunnable(Lifecycle lifecycle) {
        // 添加生命周期观察者
        lifecycle.addObserver(this);
    }

    @Override
    public void run() {
        if (!isStop) {
            lifecycleRun();
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void lifecycleStop() {
        isStop = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void lifecycleDestroy() {
        isStop = true;
    }

    public void lifecycleRun() {

    }
}
