package cn.rjgc.donarms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.rjgc.commonlib.util.eventbus.LiveDataBus;
import cn.rjgc.commonlib.util.shake.OnAntiShakeClickListener;
import cn.rjgc.donarms.databinding.ActivityMainBinding;
import cn.rjgc.donarms.util.EventType;
import cn.rjgc.donarms.view.HighlightSearchActivity;
import cn.rjgc.donarms.view.KeyboardListenerActivity;

/**
 * @author Don
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initEvent();

        LiveDataBus.get().with(EventType.REFRESH_TEXT, Boolean.class).observe(this, aBoolean -> {
            if (aBoolean) {
                binding.btnEventBus.setText("文案已变化");
            }
        });
    }

    private void initEvent() {
        //键盘显示和隐藏监听
        binding.btnKeyboard.setOnClickListener(new OnAntiShakeClickListener() {
            @Override
            public void onAntiShakeClick(View view) {
                Intent intent = new Intent(MainActivity.this, KeyboardListenerActivity.class);
                startActivity(intent);
            }
        });

        binding.btnAntiShake.setOnClickListener(new OnAntiShakeClickListener() {
            @Override
            public void onAntiShakeClick(View view) {
                Toast.makeText(MainActivity.this, "防抖动点击", Toast.LENGTH_LONG).show();
            }
        });

        binding.btnEventBus.setOnClickListener(new OnAntiShakeClickListener() {
            @Override
            public void onAntiShakeClick(View view) {
                Intent intent = new Intent(MainActivity.this, KeyboardListenerActivity.class);
                startActivity(intent);
            }
        });

        binding.btnHighlightView.setOnClickListener(new OnAntiShakeClickListener() {
            @Override
            public void onAntiShakeClick(View view) {
                Intent intent = new Intent(MainActivity.this, HighlightSearchActivity.class);
                startActivity(intent);
            }
        });
    }

}