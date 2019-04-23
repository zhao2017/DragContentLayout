package test.com.zh.dragcontentlayout.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @author heqijun
 * @description 控制textview倒计时的countdownTimer
 * @date 2019/4/18 11:00 AM
 */
public class UpdateVoiceTimeThread {

    private CountDownTimer cdt;
    private final static int TIME_CHANGE_DELAY = 1000;
    private String time;
    private static UpdateVoiceTimeThread instance = null;
    public static long l;
    private TextView tv;

    public static UpdateVoiceTimeThread getInstance(long sTime, TextView tv) {
        if (instance == null) {
            synchronized (UpdateVoiceTimeThread.class) {
                if (instance == null) {
                    instance = new UpdateVoiceTimeThread(sTime, tv);
                }
            }
        }
        return instance;
    }

    private UpdateVoiceTimeThread(long sTime, TextView tv) {
        this.tv = tv;
        time = StringUtils.formatVoiceTime(sTime);
        l = sTime;
    }

    private CountDownTimer getTimer() {
        if (cdt != null) {
            cdt.cancel();
            cdt = null;
        }
        cdt = new CountDownTimer(l, TIME_CHANGE_DELAY) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                tv.setText(StringUtils.formatVoiceTime(millisUntilFinished));
                l = l - TIME_CHANGE_DELAY;
            }

            @Override
            public void onFinish() {
                tv.setText(time);

            }

        };
        return cdt;
    }

    public void start() {
        getTimer();
        cdt.start();
    }

    public void pause() {
        cdt.cancel();
        tv.setText(StringUtils.formatVoiceTime(l));
    }

    public void stop() {
        instance = null;
        if (cdt != null) {
            cdt.cancel();
            cdt = null;
        }
        tv.setText(time);
    }

}
