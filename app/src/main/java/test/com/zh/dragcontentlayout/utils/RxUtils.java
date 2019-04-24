package test.com.zh.dragcontentlayout.utils;

import android.app.Activity;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * @author Army
 * @version V_1.0.0
 * @date 2017/4/29
 * @description
 */
public class RxUtils {
    private static final int DISPLAYONCE = 1;

    /**
     * 统一线程处理
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper(Activity activity) {    //compose简化线程
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {
                    if (activity != null && Looper.myLooper() == Looper.getMainLooper()) {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 全在子线程
     */
    public static <T> FlowableTransformer<T, T> rxThreadHelper() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    /**
     * 全在子线程
     */
    public static <T> FlowableTransformer<T, T> rxThreadHelperToMain() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 全在子线程
     */
    public static <T> FlowableTransformer<T, T> rxThreadHelper(Activity activity) {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {
                    if (activity != null && Looper.myLooper() == Looper.getMainLooper()) {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io());
    }

    public static <T> FlowableTransformer<T, T> rxSchedulerHelperOnce(Activity activity, int flag) {    //compose简化线程
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {
                    if (activity != null && Looper.myLooper() == Looper.getMainLooper()) {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<Integer> countDown(int count) {
        return Flowable.interval(1, TimeUnit.SECONDS)
                .take(count + 1).map(aLong -> count - aLong.intValue());
    }

//    private static AllClickInvocationHandler invocationHandler = null;

    public static void click(View v, Consumer<Object> onNext) {
//        Consumer consumer;
//        if (isNeedJudgeLogin != null && isNeedJudgeLogin.length > 0 && isNeedJudgeLogin[0]) {
//            if (invocationHandler == null) {
//                invocationHandler = new AllClickInvocationHandler();
//            }
//            consumer = (Consumer) invocationHandler.bind(onNext);
//        } else {
//            consumer = onNext;
//        }

        RxView.clicks(v).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(onNext);
    }

    public static void longClick(View v, Consumer<Object> onNext) {
        RxView.longClicks(v).subscribe(onNext);
    }

//    private static class AllClickInvocationHandler implements InvocationHandler {
//
//        private Object object = null;
//
//        public Object bind(Object obj) {
//            object = obj;
//            return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
//        }
//
//        @Override
//        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//            if (Global.isLogin) {
//                return method.invoke(object, args);
//            } else {
//                Activity topActivity = AppManager.getInstance().getTopActivity();
//                Context context;
//                Intent intent = new Intent();
//                if (topActivity == null) {
//                    context = Global.application;
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                } else {
//                    context = topActivity;
//                }
//                intent.setClassName(context, Global.loginActivity);
//                if (intent.resolveActivity(context.getPackageManager()) != null) {
//                    context.startActivity(intent);
//                }
//            }
//            return null;
//        }
//    }

    public static Observable<Boolean> meetMultiConditionDo(Function<Object[], Boolean> combiner, TextView... tvs) {
        if (tvs != null && tvs.length > 0) {
            List<Observable<CharSequence>> observableList = new ArrayList<>();
            for (int i = 0; i < tvs.length; i++) {
                observableList.add(RxTextView.textChanges(tvs[i]).skip(1));
            }
            return Observable.combineLatest(observableList, combiner).distinctUntilChanged();
        }
        return null;
    }


    //定时任务以及循环任务
    private static Disposable mDisposable;

    /**
     * milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static void timer(long milliseconds, final IRxNext next) {
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //取消订阅
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        //取消订阅
                        cancel();
                    }
                });
    }

    /**
     * 每隔milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static void interval(long milliseconds, final IRxNext next) {
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 倒计时功能
     *
     * 倒计时totalTime每间隔periodTime秒后    执行next操作
     *
     * @param totalTime 总时长
     * @param delayedTime 延时时长
     * @param periodTime 间隔时长
     * @param next
     */
    public static void intervalRange(long totalTime, long delayedTime, long periodTime, final IRxNext next) {
        //从0开始发射11个数字为：0-10依次输出，延时0s执行，每1s发射一次。
        Observable.intervalRange(0, totalTime, delayedTime, periodTime, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(totalTime-1-number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 取消订阅
     */
    public static void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public interface IRxNext {
        void doNext(long number);
    }

//    @Override
//    protected void onDestroy(){
//        //取消定时器
//        RxTimerUtil.cancel();
//        LogUtil.e("====cancel====="+ DateUtil.getNowTime());
//        super.onDestroy();
//    }


}
