package test.com.zh.dragcontentlayout.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.TagFlowLayout;

import org.xml.sax.XMLReader;

import java.util.HashMap;
import java.util.Map;

import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.utils.StringUtils;

/**
 * 创建日期：2019/4/9
 * 描述:
 *
 * @author: zhaoh
 */
public class SelectWordsFillBlanksView extends RelativeLayout {
    private String SPACE_TAG = "[space]";
    private String FILL_TAG = "&nbsp;&nbsp;<mytag>&nbsp;";
    private String MY_TAG_NAME = "mytag";
    private Context mContext;
    private TextView tvContent;
    private TagFlowLayout tagFlowLayout;

    private Map<ClickableSpan,Integer> map = new HashMap<>();

    public SelectWordsFillBlanksView(Context context) {
        this(context, null);
    }

    public SelectWordsFillBlanksView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectWordsFillBlanksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.layout_select_words_fill_blanks, this);
        tvContent = view.findViewById(R.id.tv_content);
    }

    public void setData(String content) {
        String replace = content.replace(SPACE_TAG, FILL_TAG);
        map.clear();
        SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) Html.fromHtml(StringUtils.remove_p_tag(StringUtils.replaceUnderline(StringUtils.replaceExpression(replace))), null, new Html.TagHandler() {
           int index =0;
            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                if (tag.equalsIgnoreCase(MY_TAG_NAME) && opening) {
                    index++;
                    MyRadiusBgSpan myRadiusBgSpan = new MyRadiusBgSpan(mContext,index);
                    output.setSpan(myRadiusBgSpan, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    NolineClickSpan nolineClickSpan = new NolineClickSpan();
                    map.put(nolineClickSpan,index);
                    output.setSpan(nolineClickSpan,output.length()-1,output.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.e("View","length=="+output.length());
                }
            }
        });
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(spannableStringBuilder);
        tvContent.setHighlightColor(ContextCompat.getColor(mContext,R.color.transparent));
    }

    class NolineClickSpan extends ClickableSpan {
        public NolineClickSpan(){

        }
        @Override
        public void onClick(View widget) {
            Log.e("span","span=="+this);
            int postion = map.get(this);
            changeSelectWords();
            Toast.makeText(tvContent.getContext(), "Hello World"+postion, Toast.LENGTH_LONG).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }
    private void changeSelectWords() {
    }

}
