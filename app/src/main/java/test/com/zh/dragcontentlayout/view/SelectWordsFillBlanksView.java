package test.com.zh.dragcontentlayout.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.TagFlowLayout;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.utils.StringUtils;

/**
 * 创建日期：2019/4/9
 * 描述:
 *
 * @author: zhaoh
 */
public class SelectWordsFillBlanksView extends RelativeLayout implements MyRadiusBgSpan.OnSpanClickListener {
    private String SPACE_TAG = "[space]";
    private String FILL_TAG = "&nbsp;&nbsp;<mytag>&nbsp;";
    private String MY_TAG_NAME = "mytag";
    private Context mContext;
    private TextView tvContent;
    private TagFlowLayout tagFlowLayout;
    private List<MyRadiusBgSpan> mList = new ArrayList<>();
    private Map<ClickableSpan, Integer> map = new HashMap<>();
    private int currentSpanPostion = 0;
    private MyRadiusBgSpan currentMyRadiusBgSpan;
    private String mContent = "";

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
        this.mContent = replace;
        map.clear();
        mList.clear();
        tvContent.setMovementMethod(null);
        SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) Html.fromHtml(StringUtils.remove_p_tag(StringUtils.replaceUnderline(StringUtils.replaceExpression(replace))), null, new Html.TagHandler() {
            int index = 0;

            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                if (tag.equalsIgnoreCase(MY_TAG_NAME) && opening) {
                    index++;
                    MyRadiusBgSpan myRadiusBgSpan = new MyRadiusBgSpan(mContext, index);
                    output.setSpan(myRadiusBgSpan, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    NolineClickSpan nolineClickSpan = new NolineClickSpan();
                    map.put(nolineClickSpan, index);
                    mList.add(myRadiusBgSpan);
                    output.setSpan(nolineClickSpan, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.e("View", "length==" + output.length());
                }
            }
        });
        tvContent.setMovementMethod( Method);
        tvContent.setText(spannableStringBuilder);
        tvContent.setHighlightColor(ContextCompat.getColor(mContext, R.color.transparent));
    }

    public int getCurrentSpanPostion() {
        return currentSpanPostion;
    }

    @Override
    public void onSpanClick(int postion, MyRadiusBgSpan myRadiusBgSpan) {
        Toast toast = Toast.makeText(mContext, null, Toast.LENGTH_SHORT);
        toast.setText("postion==" + postion);
        toast.show();
        this.currentMyRadiusBgSpan = myRadiusBgSpan;
        // 主要是因为存的时候index++第一个初始位置是1
        myRadiusBgSpan.setCurrentPostion(postion - 1);
        this.currentSpanPostion = postion - 1;
        invalidateTv();
    }

    /**
     * 插入选项数据
     *
     * @param insertTextData
     * @param currentSpanPostion
     */
    public void setInsertTextData(String insertTextData, int currentSpanPostion) {
        /**
         * 默认是取的是第一个Span
         */
        mList.get(currentSpanPostion).setToTalSpanSize(mList.size());
        fillAnswer(insertTextData,currentSpanPostion);
    }

    /**
     *  填入内容后更新TextView
     * @param insertTextData
     * @param currentSpanPostion
     */
    private void fillAnswer(final String insertTextData, final int currentSpanPostion) {
        mList.clear();
        map.clear();
        SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) Html.fromHtml(StringUtils.remove_p_tag(StringUtils.replaceUnderline(StringUtils.replaceExpression(mContent))), null, new Html.TagHandler() {
            int index = 0;
            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                if (tag.equalsIgnoreCase(MY_TAG_NAME) && opening) {
                    index++;
                    MyRadiusBgSpan myRadiusBgSpan = new MyRadiusBgSpan(mContext, index);
                    if(currentSpanPostion==index-1){
                        myRadiusBgSpan.setQuestionText(insertTextData,currentSpanPostion);
                    }
                    output.setSpan(myRadiusBgSpan, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mList.add(myRadiusBgSpan);
                    Log.e("out","out.length="+output.length());
                }
            }
        });
        spannableStringBuilder.insert(0,"hahah");
        tvContent.setText(spannableStringBuilder);
    }

    //TextView触摸事件-->Span点击事件
    private LinkMovementMethod Method = new LinkMovementMethod() {

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer,
                                    MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    //Span的点击事件
                    if (action == MotionEvent.ACTION_DOWN) {
                        link[0].onClick(widget);
                    }
                    return true;
                }
            }
            return false;
        }
    };


    /**
     * 没有下划线的点击Span
     */
    class NolineClickSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            Log.e("span", "span==" + this);
            int postion = map.get(this);
            onSpanClick(postion, mList.get(postion - 1));
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    /**
     * 刷新TextView
     */
    private void invalidateTv() {
        tvContent.invalidate();
    }




}
