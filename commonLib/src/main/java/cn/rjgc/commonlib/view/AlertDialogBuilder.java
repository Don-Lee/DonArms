package cn.rjgc.commonlib.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Date 2021/10/13
 * 建议使用 MaterialAlertDialogBuilder 可参考material-components-android项目
 * @author Don
 */
public class AlertDialogBuilder extends AlertDialog.Builder {
    public AlertDialogBuilder(@NonNull Context context) {
        this(context, 0);
    }

    public AlertDialogBuilder(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
}
