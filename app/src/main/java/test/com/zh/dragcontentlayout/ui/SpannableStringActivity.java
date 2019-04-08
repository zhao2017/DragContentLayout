package test.com.zh.dragcontentlayout.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.com.zh.dragcontentlayout.R;

/**
 * 创建日期：2019/4/8
 * 描述:测试SpanableString
 *
 * @author: zhaoh
 */
public class SpannableStringActivity extends Activity {

    private static final String TAG = SpannableStringActivity.class.getSimpleName();
    @BindView(R.id.tv_content)
    TextView tvContent;

    private static final String p_tag = "<p>";
    public static void navToActivity(Context context) {
        Intent intent = new Intent(context, SpannableStringActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_spanablestring);
        ButterKnife.bind(this);
        initData();
    }
    private void initData() {
        String originContent = "测试用的数据看看[space]我能变颜色不,哈哈哈我还能点击哦";
        SpannableString spannableString = new SpannableString(originContent);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#68F709"));
        /**
         * Object what, int start, int end, int flags
         * what: span
         * start: span作用的起始位置
         * end: ：结束应用指定Span的位置，特效并不包括这个位置需要注意此处哦。
         * flagS:共有四种：
         *
         * Spanned.SPAN_INCLUSIVE_INCLUSIVE
         * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
         * Spanned.SPAN_INCLUSIVE_EXCLUSIVE
         * Spanned.SPAN_EXCLUSIVE_INCLUSIVE
         *
         */
        spannableString.setSpan(foregroundColorSpan,1, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        MyClickSpan myClickSpan = new MyClickSpan();
        spannableString.setSpan(myClickSpan,8,15,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置此方法后，点击事件才能生效
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        tvContent.setText(spannableString);


    }

    class MyClickSpan extends ClickableSpan{
        @Override
        public void onClick( View widget) {
            // 处理小米弹出应用名字的问题
            Toast toast = Toast.makeText(SpannableStringActivity.this, null, Toast.LENGTH_LONG);
            toast.setText("我被点击了！！！");
            toast.show();
        }
    }

}
