package test.com.zh.dragcontentlayout.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private List<MyRadiusBgSpan> mList = new ArrayList<>();
    private Map<ClickableSpan, Integer> map = new HashMap<>();
    private int currentSpanPostion = 0;
    private MyRadiusBgSpan currentMyRadiusBgSpan;
    private String mContent = "";
    private List<String> answerList = new ArrayList<>();
    private Map<Integer, String> textMap = new HashMap<>();

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

    /**
     * 初始化原始数据没有添加内容的
     *
     * @param content
     */
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
                    mList.add(myRadiusBgSpan);
                    NolineClickSpan nolineClickSpan = new NolineClickSpan();
                    map.put(nolineClickSpan, index);
                    output.setSpan(nolineClickSpan, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.e("View", "length==" + output.length());
                }
            }
        });
        tvContent.setMovementMethod(new TouchLinkMovementMethod());
        tvContent.setText(spannableStringBuilder);
        tvContent.setHighlightColor(ContextCompat.getColor(mContext, R.color.transparent));
    }

    /**
     * 初始化数据里面，已经已经填入答案内容的
     *
     * @param content
     * @param answerList
     */
    public void setData(String content, final List<String> answerList) {
        String replace = content.replace(SPACE_TAG, FILL_TAG);
        this.mContent = replace;
        this.answerList = answerList;
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
                    if (answerList != null && answerList.size() > 0) {
                        String str = answerList.get(index - 1);
                        myRadiusBgSpan.setOriginalQuestionText(str);
                        Log.e("index", "index==" + index + ";str=" + str);
                    }
                    mList.add(myRadiusBgSpan);
                    NolineClickSpan nolineClickSpan = new NolineClickSpan();
                    map.put(nolineClickSpan, index);
                    output.setSpan(nolineClickSpan, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.e("View", "length==" + output.length());
                }
            }
        });
        tvContent.setMovementMethod(new TouchLinkMovementMethod());
        tvContent.setText(spannableStringBuilder);
        tvContent.setHighlightColor(ContextCompat.getColor(mContext, R.color.transparent));
    }


    public int getCurrentSpanPostion() {
        return currentSpanPostion;
    }

    @Override
    public void onSpanClick(int postion, MyRadiusBgSpan myRadiusBgSpan) {
        Toast toast = Toast.makeText(mContext, null, Toast.LENGTH_SHORT);
        toast.setText("postion==" + (postion - 1));
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
        if (currentSpanPostion <= mList.size() - 1) {
            fillAnswer(insertTextData, currentSpanPostion);
        }
    }

    /**
     * 填入内容后更新TextView
     *
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
                    output.setSpan(myRadiusBgSpan, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (currentSpanPostion == index - 1) {
                        myRadiusBgSpan.setQuestionText(insertTextData, currentSpanPostion);
                        textMap.put(currentSpanPostion, insertTextData);
                    }
                    if (textMap != null && textMap.size() > 0) {
                        String orgStr = textMap.get(index - 1);
                        if (currentSpanPostion != index - 1) {
                            if (answerList != null && answerList.size() > 0) {
                                // 这个地方是处理答案显示
                                String str = answerList.get(index - 1);
                                myRadiusBgSpan.setOriginalQuestionText(str);
                            } else {
                                myRadiusBgSpan.setOriginalQuestionText(orgStr);
                            }
                        }
                        Log.e("orgStr", "orgStr==" + orgStr + ";currentSpanPostion==" + currentSpanPostion);
                    }
                    mList.add(myRadiusBgSpan);
                    NolineClickSpan nolineClickSpan = new NolineClickSpan();
                    map.put(nolineClickSpan, index);
                    output.setSpan(nolineClickSpan, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.e("View", "length==" + output.length());
                }
            }
        });
        tvContent.setMovementMethod(new TouchLinkMovementMethod());
        tvContent.setText(spannableStringBuilder);
        tvContent.setHighlightColor(ContextCompat.getColor(mContext, R.color.transparent));
        if (currentSpanPostion < mList.size()) {
            next(currentSpanPostion);
        }
        for (Map.Entry<Integer, String> entry : textMap.entrySet()) {
            Log.e("map", "key==" + entry.getKey() + ";value==" + entry.getValue());
        }
    }


    /**
     * 下一空
     *
     * @param pos
     */
    private void next(int pos) {
        // 将下一个空格的颜色切换成选中的颜色，
        int nextPos = pos + 1;
        if (nextPos <= mList.size()) {
            if (nextPos > mList.size() - 1) {
                currentSpanPostion = nextPos - 1;
            } else {
                currentSpanPostion = nextPos;
            }
            Log.e("nexpos", "nextPos==" + nextPos);
            for (int i = 0; i < mList.size(); i++) {
                MyRadiusBgSpan myRadiusBgSpan = mList.get(i);
                myRadiusBgSpan.setCurrentPostion(currentSpanPostion);
            }
        }
    }

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

    public interface OnSelectWordsFillBlanksViewClickListener {

    }

}
