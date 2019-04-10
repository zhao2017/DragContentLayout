package test.com.zh.dragcontentlayout.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.XMLReader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.utils.DisplayUtils;
import test.com.zh.dragcontentlayout.utils.StringUtils;
import test.com.zh.dragcontentlayout.view.MyRadiusBgSpan;
import test.com.zh.dragcontentlayout.view.ReplaceSpan;

/**
 * 创建日期：2019/4/9
 * 描述: 选择填空的demo
 *
 * @author: zhaoh
 */
public class SelectWordsFillBlanksActivity2 extends Activity {

    @BindView(R.id.tv_content)
    TextView tvContent;
    private String content = "<p>途中的两个球相比[space][space]的撒大家京东徐返多少发奥[space]奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥吧范发[space]卡了</p>";
    private String SPACE_TAG = "[space]";
    private String FILL_TAG = "&nbsp;&nbsp;<mytag>&nbsp;";
    private String FILL_TAG_NAME = "mytag";
    private List<ReplaceSpan> mSpans;

    public static void navToActivity(Context context) {
        Intent intent = new Intent(context, SelectWordsFillBlanksActivity2.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_words_fill_blanks);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String replace = content.replace(SPACE_TAG, FILL_TAG);
        SpannableStringBuilder spannableStringBuilder =
                (SpannableStringBuilder) Html.fromHtml(StringUtils.remove_p_tag(StringUtils.replaceUnderline(StringUtils.replaceExpression(replace))), null, new Html.TagHandler() {
                    @Override
                    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                        if (tag.equalsIgnoreCase(FILL_TAG_NAME) && opening) {
                            TextPaint textPaint = new TextPaint(tvContent.getPaint());
                            MyRadiusBgSpan radiusTextSpan = new MyRadiusBgSpan(SelectWordsFillBlanksActivity2.this, R.color.color_f7f7f9, R.color.color_0093e8, DisplayUtils.dip2px(2));
                            output.setSpan(radiusTextSpan, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            NolineClickSpan nolineClickSpan = new NolineClickSpan();
                            output.setSpan(nolineClickSpan,output.length()-1,output.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                });
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(spannableStringBuilder);
        tvContent.setHighlightColor(ContextCompat.getColor(SelectWordsFillBlanksActivity2.this,R.color.transparent));
    }

    class NolineClickSpan extends ClickableSpan{
        @Override
        public void onClick(View widget) {
            Toast.makeText(SelectWordsFillBlanksActivity2.this,"Hello World",Toast.LENGTH_LONG).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }
}
