package test.com.zh.test_handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;


/**
 * 创建日期：2019/4/11
 * 描述: 测试Handler的类
 *
 * @author: zhaoh
 */
public class TestHandlerActivity extends Activity {

    public static void main(String[] args) {

        Message.obtain().recycle();
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });

        for (int i = 0; i < 20; i++) {
            
        }
    }
    
    


}
