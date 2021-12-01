package cn.rjgc.donarms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.rjgc.commonlib.util.DisplayMetricsHolder;
import cn.rjgc.commonlib.util.PixelUtil;
import cn.rjgc.commonlib.util.Util;
import cn.rjgc.commonlib.util.eventbus.LiveDataBus;
import cn.rjgc.commonlib.util.shake.OnAntiShakeClickListener;
import cn.rjgc.donarms.R;
import cn.rjgc.donarms.adapter.AutoCompleteTextAdapter;
import cn.rjgc.donarms.bean.OilStationBean;
import cn.rjgc.donarms.databinding.ActivityHighlightSearchBinding;
import cn.rjgc.donarms.util.EventType;

/**
 * @author Don
 */
public class HighlightSearchActivity extends AppCompatActivity {
    private ActivityHighlightSearchBinding binding;
    private AutoCompleteTextAdapter adapter;
    /*String[] mdata = new String[]{
            "济南", "青岛", "山东济南", "山东青岛", "中国", "广州", "北京", "上海",
            "济南1", "青岛1", "山东济南1", "山东青岛1", "中国1", "广州1", "北京1", "上海1"
    };*/

    private List<OilStationBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHighlightSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        DisplayMetricsHolder.initDisplayMetricsIfNotInitialized(getApplicationContext());
        //最小值为1，表示输入几个字后开始匹配
        binding.actv.setThreshold(1);
        binding.actv.setDropDownVerticalOffset((int) PixelUtil.toPixelFromDIP(8));
        binding.actv.setDropDownBackgroundResource(R.drawable.white_bg_corner5);
        adapter = new AutoCompleteTextAdapter(mList, this);
        //为自动完成文本框设置适配器
        binding.actv.setAdapter(adapter);
        binding.actv.setOnClickListener(new OnAntiShakeClickListener() {
            @Override
            public void onAntiShakeClick(View view) {
                AppCompatAutoCompleteTextView v = (AppCompatAutoCompleteTextView) view;
                if (!Util.isEmpty(mList)) {
                    v.showDropDown();
                }
            }
        });
        binding.actv.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.actv.showDropDown();
            }
        });
        binding.actv.setOnItemClickListener((parent, view, position, id) -> {
           binding.actv.setText(adapter.getItem(position).getName());
        });

        LiveDataBus.get().with(EventType.CLOSE_DIALOG, Boolean.class).observe(this, isClose -> {
            if (isClose) {
                binding.actv.dismissDropDown();
            } else {
                if (binding.actv.isFocused()) {
//                    MainThread.runLater(() -> binding.actv.showDropDown(), 200);
                    binding.actv.showDropDown();
                }
                // setAlwaysVisible(getListPopupWindow(binding.receiver));
            }
        });
    }

    private void initData() {
        mList.add(new OilStationBean("石化邹城","邹城北"));
        mList.add(new OilStationBean("石化邹城1","邹城南"));
        mList.add(new OilStationBean("石化邹城2","邹城东"));
        mList.add(new OilStationBean("石化邹城3","邹城西"));
        mList.add(new OilStationBean("石化济宁","济宁北"));
        mList.add(new OilStationBean("石化济宁1","济宁南"));
        mList.add(new OilStationBean("石化济宁2","济宁西"));
        mList.add(new OilStationBean("石化济宁3","济宁东"));
    }


    /**
     * 获取ACTV的ListPopupWindow对象
     *
     * @param textView AutoCompleteTextView
     * @return ListPopupWindow对象
     */
    private static ListPopupWindow getListPopupWindow(AutoCompleteTextView textView) {
        try {
            Class<?> aClass = textView.getClass();
            Field field = null;
            while (aClass != null) {
                try {
                    field = aClass.getDeclaredField("mPopup");
                } catch (NoSuchFieldException ignore) {} finally {
                    aClass = aClass.getSuperclass();
                }
                if (field != null) {
                    break;
                }
            }
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            return (ListPopupWindow) field.get(textView);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 反射 setDropDownAlwaysVisible()，让弹窗用不消失
     *
     * @param popup
     */
    private static void setAlwaysVisible(@NonNull ListPopupWindow popup) {
        try {
            Class<? extends ListPopupWindow> clazz = popup.getClass();
            Method dropDownAlwaysVisible = clazz.getMethod("setDropDownAlwaysVisible", boolean.class);
            dropDownAlwaysVisible.setAccessible(true);
            dropDownAlwaysVisible.invoke(popup, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}