package test.com.zh.dragcontentlayout.utils;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import test.com.zh.dragcontentlayout.config.Global;

/**
 * @author Army
 * @version V_1.0.0
 * @date 2017/07/31
 * @description 显示Toast
 */
public class ToastUtils {

    private static Toast toast;

    private static void createToast(Context context, String content, int resId) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            //防止内存泄漏
            Context applicationContext = context.getApplicationContext();
            if (resId > 0) {
                toast = Toast.makeText(applicationContext, resId, Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT);
            }
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        if (view != null) {
            TextView message = (TextView) view.findViewById(android.R.id.message);
            message.setGravity(Gravity.CENTER);
        }
        if (resId > 0) {
            toast.setText(resId);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void showToast(final String content) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            createToast(Global.application, content, 0);
        } else {
            UIHandler.getInstance().post(() -> createToast(Global.application, content, 0));
        }
    }

    public static void showToast(final int resId) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            createToast(Global.application, "", resId);
        } else {
            UIHandler.getInstance().post(() -> createToast(Global.application, "", resId));
        }
    }

   /* public static void showSuccessToast(String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            createSuccessToast(message);
        } else {
            UIHandler.getInstance().post(() -> createSuccessToast(message));
        }
    }

    public static void createSuccessToast(String message) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(Global.application).inflate(R.layout.common_special_toast, null);
        //初始化布局控件
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.message);
        //为控件设置属性
        mTextView.setText(message);
        //Toast的初始化
        Toast toastStart = new Toast(Global.application);
        toastStart.setGravity(Gravity.CENTER, 0, 0);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }*/
}
