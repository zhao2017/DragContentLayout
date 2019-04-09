package test.com.zh.dragcontentlayout;

import android.app.Application;

import test.com.zh.dragcontentlayout.config.Global;

/**
 * 创建日期：2019/4/9
 * 描述:
 *
 * @author: zhaoh
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Global.application = this;
    }
}
