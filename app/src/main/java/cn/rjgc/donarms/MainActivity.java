package cn.rjgc.donarms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.rjgc.commonlib.util.shake.OnAntiShakeClickListener;
import cn.rjgc.donarms.databinding.ActivityMainBinding;

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
    }

    private void initEvent() {
        //键盘显示和隐藏监听
        binding.btnKeyboard.setOnClickListener(v -> {
            Intent intent = new Intent(this, KeyboardListenerActivity.class);
            startActivity(intent);
        });

        binding.btnAntiShake.setOnClickListener(new OnAntiShakeClickListener() {
            @Override
            public void onAntiShakeClick() {
                Toast.makeText(MainActivity.this, "防抖动点击", Toast.LENGTH_LONG).show();
            }
        });
    }

}