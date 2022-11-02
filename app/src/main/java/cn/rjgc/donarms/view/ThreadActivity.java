package cn.rjgc.donarms.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.rjgc.commonlib.util.AppThreadExecutors;
import cn.rjgc.commonlib.util.LifecycleRunnable;
import cn.rjgc.donarms.MainActivity;
import cn.rjgc.donarms.databinding.ActivityThreadBinding;

/**
 * 线程Activity
 * @author donle
 */
public class ThreadActivity extends AppCompatActivity {
    private ActivityThreadBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThreadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnThreadView.setOnClickListener(view -> {
            for (int i = 0; i < 10; i++) {
                //可感知生命周期
                AppThreadExecutors.getInstance().getSingleThread().execute(new LifecycleRunnable(getLifecycle()) {
                    @Override
                    public void lifecycleRun() {
                        Log.e("TAG", "run: " + Thread.currentThread().getName());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //不可感知生命周期
                AppThreadExecutors.getInstance().getSingleThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG1", "run: " + Thread.currentThread().getName());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }
}