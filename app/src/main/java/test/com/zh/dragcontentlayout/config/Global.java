package test.com.zh.dragcontentlayout.config;

import android.app.Application;
import android.os.Environment;

/**
 * 创建日期：2019/4/9
 * 描述:
 *
 * @author: zhaoh
 */
public class Global {

    public static Application application;

    public static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhaohApp/";
    /**
     * 语音存放路径
     */
    public static String VOICEPATH;

}
