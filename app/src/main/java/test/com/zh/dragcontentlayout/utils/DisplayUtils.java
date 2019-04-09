package test.com.zh.dragcontentlayout.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;

import test.com.zh.dragcontentlayout.config.Global;


/**
 * @author Army
 * @version V_1.0.0
 * @date 2017/07/31
 * @description 跟屏幕有关的工具类
 */
public class DisplayUtils {

    /**
     * 获得手机屏幕高度
     */
    public static int getScreenHeight() {
        return Global.application.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获得手机屏幕宽度
     */
    public static int getScreenWidth() {
        return Global.application.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        Resources resources = Global.application.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * dp转px
     */
    public static int dip2px(float dpValue) {
        final float scale = Global.application.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转px
     */
    public static int sp2px(float spValue) {
        float scale = Global.application.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dp(float dpValue) {
        final float scale = Global.application.getResources().getDisplayMetrics().density;
        return (int) (dpValue / scale + 0.5f);
    }

    /**
     * px转sp
     */
    public static int px2sp(float spValue) {
        float scale = Global.application.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue / scale + 0.5f);
    }

    /**
     * 获取导航栏高度
     * <p>0代表不存在</p>
     */
    public static int getNavBarHeight() {
        Resources res = Global.application.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 判断底部虚拟按键是否存在<br/>
     * 此方法在有些全面屏的手机上有问题，推荐监听主界面的rootview的layoutchange事件
     */
    public static boolean isNavigationBarShow(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }


    /**
     * popupWindow 动态设置高度问题
     *
     * @param rootView
     * @param belowView
     * @return
     */
    public static int getHeight(View rootView, View belowView, boolean needBottomHeight) {
        rootView.measure(0, 0);
        int[] location = new int[2];
        belowView.getLocationOnScreen(location);//view距离顶部的高度
        int x = location[0];
        int y = location[1];
        int viewBottomHeight = belowView.getBottom();//view距离顶部的高度
        int height = DisplayUtils.getScreenHeight();//屏幕的高度
        if (needBottomHeight) {
            return height - y - (belowView.getHeight()) - 0;
        } else {
            return height - y - (belowView.getHeight()) - dip2px(48);
        }
    }
}
