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
import android.widget.TextView;

import org.xml.sax.XMLReader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.utils.StringUtils;
import test.com.zh.dragcontentlayout.view.ReplaceSpan;

/**
 * 创建日期：2019/4/9
 * 描述: 选择填空的demo
 *
 * @author: zhaoh
 */
public class SelectWordsFillBlanksActivity extends Activity {

    @BindView(R.id.tv_content)
    TextView tvContent;
    private String content = "<p>途中的两个球相比[space]的撒大家京东[space]卡了</p>";
    private String SPACE_TAG = "[space]";
    private String FILL_TAG = "&nbsp;&nbsp;<edit>&nbsp;&nbsp;&nbsp;";
    private String FILL_TAG_NAME = "edit";
    private List<ReplaceSpan> mSpans;
    public static void navToActivity(Context context){
        Intent intent = new Intent(context,SelectWordsFillBlanksActivity.class);
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
                        if(tag.equalsIgnoreCase(FILL_TAG_NAME)&&opening){
                            TextPaint textPaint = new TextPaint(tvContent.getPaint());
                            textPaint.setColor(ContextCompat.getColor(SelectWordsFillBlanksActivity.this,R.color.colorPrimary));
                            ReplaceSpan span =new ReplaceSpan(tvContent.getContext(),textPaint,60);
                            output.setSpan(span, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                });
          tvContent.setText(spannableStringBuilder);
    }
}
