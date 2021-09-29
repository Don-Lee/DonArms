package cn.rjgc.donarms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import cn.rjgc.commonlib.util.DisplayMetricsHolder;
import cn.rjgc.commonlib.util.keyboard.GlobalLayoutListener;
import cn.rjgc.commonlib.util.keyboard.OnKeyboardChangedListener;

/**
 * @author Don
 */
public class KeyboardListenerActivity extends AppCompatActivity implements OnKeyboardChangedListener, View.OnClickListener {

    public static final String TAG = "KeyboardListener";
    public static final String KEY_SHOW_NAVIGATION_BAR = "__showNavigationBar__";

    /**
     * 当前activity根布局
     */
    private View mLlRoot;
    /**
     * 展示键盘改变相关数据
     */
    private TextView mTvResult;
    /**
     * 屏幕相关参数
     */
    private TextView mTvScreenParams;
    /**
     * 打开隐藏底部NavigationBar的MainActivity
     */
    private TextView mBtnDismissBar;
    /**
     * 收起键盘
     */
    private TextView mDismissKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_listener);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //是否显示底部导航键
        boolean isShow = getIntent().getBooleanExtra(KEY_SHOW_NAVIGATION_BAR, true);
        if (!isShow) {
            hideBottomUIMenu();
        }

        mTvScreenParams = findViewById(R.id.tv_screen_params);
        initScreenParams();

        mBtnDismissBar = findViewById(R.id.btn_dismiss_bar);
        mBtnDismissBar.setOnClickListener(this);
        mBtnDismissBar.setVisibility(isShow ? View.VISIBLE : View.GONE);

        mDismissKeyboard = findViewById(R.id.btn_dismiss_keyboard);
        mDismissKeyboard.setOnClickListener(this);
        mTvResult = findViewById(R.id.tv_result);

        mLlRoot = findViewById(R.id.ll_root);
        mLlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutListener(mLlRoot, this));
    }

    private void initScreenParams() {
        DisplayMetricsHolder.initDisplayMetricsIfNotInitialized(getApplicationContext());
        DisplayMetrics metrics = DisplayMetricsHolder.getScreenDisplayMetrics();
        if (metrics != null) {
            mTvScreenParams.setText("screenWidth:" + metrics.widthPixels + " px\nscreenHeight:" + metrics.heightPixels + " px");
        } else {
            mTvScreenParams.setText("cannot get screen height.");
        }
    }

    /**
     * 键盘监听
     *
     * @param isShow         键盘是否展示
     * @param keyboardHeight 键盘高度(当isShow为false时,keyboardHeight=0)
     * @param screenWidth    屏幕宽度
     * @param screenHeight   屏幕可用高度(不包含底部虚拟键盘NavigationBar), 即屏幕高度-键盘高度(keyboardHeight)
     */
    @Override
    public void onChange(boolean isShow, int keyboardHeight, int screenWidth, int screenHeight) {
        StringBuilder sb = new StringBuilder();
        sb.append("键盘是否展开: ").append(isShow).append("\n")
                .append("键盘高度(px): ").append(keyboardHeight).append("\n")
                .append("屏幕宽度(px): ").append(screenWidth).append("\n")
                .append("屏幕可用高度(px): ").append(screenHeight).append("\n");
        mTvResult.append(sb.toString() + "\n");

        Log.e(TAG, "键盘是否展开: " + isShow);
        Log.e(TAG, "键盘高度(px): " + keyboardHeight);
        Log.e(TAG, "屏幕宽度(px): " + screenWidth);
        Log.e(TAG, "屏幕可用高度(px): " + screenHeight);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
            _window.setAttributes(params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dismiss_bar:
                startActivityWithoutNavigationBar(this);
                break;
            case R.id.btn_dismiss_keyboard:
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                break;
        }
    }

    private static void startActivityWithoutNavigationBar(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_SHOW_NAVIGATION_BAR, false);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}