package test.com.zh.dragcontentlayout.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.view.IconTextSpan;

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
    @BindView(R.id.tv_1)
    TextView tv1;

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
        initData2();

    }

    private void initData2() {
        List<ReplacementSpan> replacementSpans = new ArrayList<>();
        String content = "Android是一种基于Linux的自由及开放源代码的操作系统，主要使用于移动设备，" +
                "如智能手机和平板电脑，由Google公司和开放手机联盟领导及开发。尚未有统一中文名称，中国大陆地区较多人使用“安卓”或“安致”。";

        StringBuilder sb = new StringBuilder();
        //第一个span；
        sb.append(" ");
        IconTextSpan topSpan = new IconTextSpan(SpannableStringActivity.this, R.color.color_666, "置顶");
        replacementSpans.add(topSpan);
        sb.append(" ");
        IconTextSpan hotSpan = new IconTextSpan(SpannableStringActivity.this, R.color.color_666, "热");
        hotSpan.setRightMarginDpValue(5);
        sb.append(content);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(sb.toString());
        for (int i = 0; i < replacementSpans.size(); i++) {
            spannableStringBuilder.setSpan(replacementSpans.get(i), 20, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        MyClickSpan myClickSpan = new MyClickSpan();
        spannableStringBuilder.setSpan(myClickSpan, 20, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置此方法后，点击事件才能生效
        tv1.setMovementMethod(LinkMovementMethod.getInstance());
        tv1.setText(spannableStringBuilder);
        tv1.setHighlightColor(getResources().getColor(android.R.color.transparent));
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
        spannableString.setSpan(foregroundColorSpan, 1, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        MyClickSpan myClickSpan = new MyClickSpan();
        spannableString.setSpan(myClickSpan, 8, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置此方法后，点击事件才能生效
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        tvContent.setText(spannableString);


    }

    class MyClickSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            // 处理小米弹出应用名字的问题
            widget.setBackground(null);
            Toast toast = Toast.makeText(SpannableStringActivity.this, null, Toast.LENGTH_LONG);
            toast.setText("我被点击了！！！");
            toast.show();
        }
    }

}
