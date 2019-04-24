package test.com.zh.dragcontentlayout.utils;

import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;

import java.io.File;

import test.com.zh.dragcontentlayout.config.Global;

/**
 * @author Army
 * @version V_1.0.0
 * @date 2017/12/3
 * @description 通用的播放语音并且播放动画
 */
public class VoiceUtils {

    private static String currentPlayId = "";

    /**
     * 播放音频 带有倒计时控件
     * 支持暂停功能
     * 再次点击重新播放
     *
     * @param tvTime
     * @param sTime
     * @param view
     * @param voice
     * @param id
     */
    public static void playVoiceWithTimer(TextView tvTime, long sTime, View view, String voice, final String id) {
        AnimationDrawable animationDrawable;
        if (view != null) {
            animationDrawable = (AnimationDrawable) view.getBackground();
        } else {
            animationDrawable = null;
        }
        final File file;
        if (voice.startsWith("http")) {
            file = new File(Global.VOICEPATH, Md5.MD5(voice));
        } else {
            file = new File(voice);
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        boolean isPlaying = !TextUtils.isEmpty(currentPlayId) && currentPlayId.equals(id);
        if (MediaUtil.getInstance().isPlaying()
                && file.getAbsolutePath().equals(MediaUtil.getInstance().getCurrentPlayFile())
                && isPlaying) {
            currentPlayId = "";
            MediaUtil.getInstance().onPause();
            if (view != null) {
                animationDrawable.stop();
                animationDrawable.selectDrawable(0);
            }
        } else {
            currentPlayId = id;
            if (file.exists()) {
                if (view == null) {
                    playSoundNoAnimation(file);
                } else {
                    playSound(animationDrawable, file, sTime, tvTime);
                }
            } else {
                OkGo.<File>get(voice)
                        .execute(new FileCallback(file.getParent(), file.getName()) {
                            @Override
                            public void onSuccess(Response<File> response) {
                                if (!TextUtils.isEmpty(currentPlayId) && currentPlayId.equals(id)) {
                                    if (view == null) {
                                        playSoundNoAnimation(file);
                                    } else {
                                        playSound(animationDrawable, file, sTime, tvTime);
                                    }
                                }
                            }

                            @Override
                            public void onError(Response<File> response) {
                                super.onError(response);
                                currentPlayId = "";
                                ToastUtils.showToast("播放失败");
                            }
                        });
            }
        }
    }

    /**
     * 通用播放音频
     *
     * @param view
     * @param voice
     * @param id
     */
    public static void playVoice(View view, String voice, final String id) {
        AnimationDrawable animationDrawable;
        if (view != null) {
            animationDrawable = (AnimationDrawable) view.getBackground();
        } else {
            animationDrawable = null;
        }
        final File file;
        if (voice.startsWith("http")) {
            file = new File(Global.VOICEPATH, Md5.MD5(voice));
        } else {
            file = new File(voice);
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        boolean isPlaying = !TextUtils.isEmpty(currentPlayId) && currentPlayId.equals(id);
        if (MediaUtil.getInstance().isPlaying()
                && file.getAbsolutePath().equals(MediaUtil.getInstance().getCurrentPlayFile())
                && isPlaying) {
            currentPlayId = "";
            MediaUtil.getInstance().stop();
            if (view != null) {
                animationDrawable.stop();
                animationDrawable.selectDrawable(0);
            }
        } else {
            currentPlayId = id;
            if (file.exists()) {
                if (view == null) {
                    playSoundNoAnimation(file);
                } else {
                    playSound(animationDrawable, file, 0, null);
                }
            } else {
                OkGo.<File>get(voice)
                        .execute(new FileCallback(file.getParent(), file.getName()) {
                            @Override
                            public void onSuccess(Response<File> response) {
                                if (!TextUtils.isEmpty(currentPlayId) && currentPlayId.equals(id)) {
                                    if (view == null) {
                                        playSoundNoAnimation(file);
                                    } else {
                                        playSound(animationDrawable, file, 0, null);
                                    }
                                }
                            }

                            @Override
                            public void onError(Response<File> response) {
                                super.onError(response);
                                currentPlayId = "";
                                ToastUtils.showToast("播放失败");
                            }
                        });
            }
        }
    }

    /**
     * 播放音频 无动画
     *
     * @param voice
     * @param id
     */
    public static void playVoiceNoAnimation(String voice, final String id) {
        playVoice(null, voice, id);
    }

    /**
     * 播放本地文件地址音频
     *
     * @param frameAnimatio
     * @param file
     * @param sTime
     * @param tv
     */
    private static void playSound(AnimationDrawable frameAnimatio, File file, long sTime, TextView tv) {
        try {
            UpdateVoiceTimeThread updateVoiceTimeThread;
            if (tv != null) {
                updateVoiceTimeThread = UpdateVoiceTimeThread.getInstance(sTime, tv);
            } else {
                updateVoiceTimeThread = null;
            }
            MediaUtil.getInstance().setOnStartListener(() -> {
                if (updateVoiceTimeThread != null) {
                    updateVoiceTimeThread.start();
                }
            });
            MediaUtil.getInstance().play(file);
            if (frameAnimatio != null) {
                frameAnimatio.start();
            }
            //这个地方需要注意，这个地方要在播放开始之后才能调用
            MediaUtil.getInstance().setEventListener(new MediaUtil.EventListener() {
                @Override
                public void onStop() {
                    Log.e("Voice", "onStop");
                    if (frameAnimatio != null) {
                        frameAnimatio.stop();
                        frameAnimatio.selectDrawable(0);
                    }
                    if (updateVoiceTimeThread != null) {
                        updateVoiceTimeThread.stop();
                    }
                    ToastUtils.showToast("这个是完成后做的回调哦");
                }
            });

            MediaUtil.getInstance().setOnPauseListener(new MediaUtil.OnPauseListener() {
                @Override
                public void onPause() {
                    ToastUtils.showToast("暂停了啊");
                    if (frameAnimatio != null) {
                        frameAnimatio.stop();
                        frameAnimatio.selectDrawable(0);
                    }
                    if (updateVoiceTimeThread != null) {
                        updateVoiceTimeThread.pause();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void playSoundNoAnimation(File file) {
        playSound(null, file, 0, null);
    }

    public static void isVoicePlay(View view, String voice, String id) {
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        boolean isPlaying = !TextUtils.isEmpty(currentPlayId) && currentPlayId.equals(id);
        if (MediaUtil.getInstance().isPlaying()
                && (Global.VOICEPATH + Md5.MD5(voice)).equals(MediaUtil.getInstance().getCurrentPlayFile())
                && isPlaying) {
            animationDrawable.start();
            MediaUtil.getInstance().setEventListener(() -> {
                currentPlayId = "";
                animationDrawable.stop();
                animationDrawable.selectDrawable(0);
            });
        } else {
            animationDrawable.stop();
            animationDrawable.selectDrawable(0);
        }
    }

    public static void playSoundWithListener(File file, MediaUtil.EventListener eventListener) {
        try {
            MediaUtil.getInstance().play(file);
            MediaUtil.getInstance().setEventListener(eventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isVoicePlay() {
        return MediaUtil.getInstance().isPlaying() && !TextUtils.isEmpty(currentPlayId);
    }


    public static void clearId() {
        currentPlayId = "";
        MediaUtil.getInstance().stop();
    }
}
