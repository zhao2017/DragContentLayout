package test.com.zh.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;

import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.utils.DisplayUtils;
import test.com.zh.dragcontentlayout.utils.ListUtils;
import test.com.zh.dragcontentlayout.utils.StringUtils;
import test.com.zh.dragcontentlayout.utils.UIHandler;

/**
 * @author 文庆
 * @date 2018/11/13.
 * @description 填空题
 * 测试内容 <p>1+2=[space]  2+3=[space]<img class="wscnph" src="http://test.img.juziwl.cn/exue/20181113/f5e7836f41cb4348aa236d2af3d62580.jpg" data-mce-src="http://test.img.juziwl.cn/exue/20181113/f5e7836f41cb4348aa236d2af3d62580.jpg"></p>
 * <p>
 * 满足现在的基本用法 如果以后样式或者数据变化了 在修改
 * <p>
 * 用法：必须写监听  监听里面处理逻辑【打开键盘/当前光标的位置/是否可以提交的状态(答案都填写的时候返回true)】
 * <p>
 * 设置填空题内容：{@link #setTvContent(String text)}  接口返回的填空题数据直接丢进来
 * 得到当前输入的答案：{@link #getAnswerList()} ()}  返回list<String>
 * <p>
 * 下一空：{@link #next()}  移动光标位置
 * 输入：{@link #addText(String)}
 * 删除：{@link #delText()}
 * 不常用的：
 * 主动请求显示键盘的调用：{@link #requestInputEvent()}
 * 提交后禁止修改：{@link #submitAndEnable()} }
 * <p>
 * <p>
 * <p>
 * <p>
 * final InputView inputView = (InputView) findViewById(R.id.inputView);
 * final Button btn_position = (Button) findViewById(R.id.btn_position);
 * inputView.setOnInputClickListener(new InputView.OnInputClickListener() {
 * @Override public void getPosition(int num) {
 * btn_position.setText("当前位置：" + num);
 * }
 * @Override public void inputTextState(boolean isSubmit) {
 * //  Toast.makeText(MainActivity.this, "是否可以提交："+isSubmit, Toast.LENGTH_SHORT).show();
 * }
 * });
 * <p>
 * inputView.setTvContent(mTestStr);
 */

public class InputView extends RelativeLayout implements ReplaceSpan.OnClickListener {

    TextView tv;
    EditText et;
    // et的颜色  et宽度dp
    int et_color, et_width_dp;

    private String INPUT_TAG = "[space]";
    private String INPUT_TAG2 = "[   ]";
    private String FILL_TAG = "&nbsp;&nbsp;<edit>&nbsp;&nbsp;";
    private String FILL_TAG_NAME = "edit";

    private boolean isNeedHideSoftKeyBorad = true;


    private List<ReplaceSpan> mSpans;
    private ReplaceSpan mCheckedSpan;//当前选中的空格

    public InputView(Context context) {
        this(context, null);
    }

    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    Context context;

    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.inputView);
        et_color = ta.getColor(R.styleable.inputView_et_color, this.getResources().getColor(R.color.color_0093e8));
        int tv_color = ta.getColor(R.styleable.inputView_tv_color, this.getResources().getColor(R.color.color_333));
        int et_size = ta.getInteger(R.styleable.inputView_et_size, 24);
        int tv_size = ta.getInteger(R.styleable.inputView_tv_size, 24);
        et_width_dp = ta.getInteger(R.styleable.inputView_et_width_dp, 60);
        String input_tag = ta.getString(R.styleable.inputView_input_tag);
        ta.recycle();


        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_input_question, this);
        tv = view.findViewById(R.id.tv_content);
        et = view.findViewById(R.id.et_input);

        tv.setTextColor(tv_color);
        et.setTextColor(et_color);

        tv.setTextSize(tv_size);
        et.setTextSize(et_size);

//        if (!TextUtils.isEmpty(input_tag)) {
//            INPUT_TAG = input_tag;
//        }
        mSpans = new ArrayList<>();
        et.setVisibility(View.GONE);//et过早显示获取焦点，会弹出系统键盘
    }

    /**
     * setAnswerList 或者setTvContent -answers 有值【传入答案可以适配输入框的长度】
     *
     * @param text
     * @param answers
     */
    public void setTvContent(String text, List<String> answers) {
        setTvContent(text, true, answers);
    }

    public void setTvContent(String text) {
        setTvContent(text, true, null);
    }

    String questionTitle;
    boolean isShowIcon = true;

    /**
     * 填空题的内容显示
     * <p>
     * 直接丢进来 接口返回的数据即可
     * answers 多个回答
     * @param text
     */
    public void setTvContent(String text, boolean isShowIcon, List<String> answers) {
        try {
            //初始化一下
            questionTitle = text;
            tv.setText("");
            tv.setMovementMethod(null);

            mSpans.clear();
            tv.setMovementMethod(Method);
            String quesOptionAsksResult = text.replace(INPUT_TAG, FILL_TAG).replace(INPUT_TAG2, FILL_TAG);

            SpannableStringBuilder spanned = (SpannableStringBuilder) Html.fromHtml(StringUtils.remove_p_tag(StringUtils.replaceUnderline(StringUtils.replaceExpression(quesOptionAsksResult))), null, new Html.TagHandler() {
                int index = 0;

                @Override
                public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                    if (tag.equalsIgnoreCase(FILL_TAG_NAME) && opening) {
                        TextPaint paint = new TextPaint(tv.getPaint());
                        paint.setColor(et_color);

                        ReplaceSpan span = null;
                        if (ListUtils.isNotEmpty(answers)) {
                            Rect rect = new Rect();

                            if (index < answers.size()) {
                                paint.getTextBounds(answers.get(index), 0, answers.get(index).length(), rect);
                                int dealWidth = DisplayUtils.px2dp(rect.width());

                                int finalWidth = 20 + dealWidth > et_width_dp ? 20 + dealWidth : et_width_dp;
                                span = new ReplaceSpan(tv.getContext(), paint, finalWidth);
                            } else {
                                span = new ReplaceSpan(tv.getContext(), paint, et_width_dp);
                            }
                        } else {
                            span = new ReplaceSpan(tv.getContext(), paint, et_width_dp);
                        }


                        span.mOnClick = InputView.this;
                        span.setText("");
                        span.position = index++;
                        mSpans.add(span);
                        output.setSpan(span, output.length() - 1, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            });

            this.isShowIcon = isShowIcon;
            tv.setText(spanned);
            initEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setTvContentNoIcon(String text) {
        setTvContent(text, false, null);
    }

    // 初始化一些事件
    private void initEvent() {
        // 点击隐藏系统的输入法
//        et.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideInput(et);
//                et.requestFocus();
//            }
//        });
        hideInput(et);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !TextUtils.isEmpty(s.toString())) {
                    //给当前span设置 值
                    setLastCheckedSpanText(s.toString());
                    boolean isFinish = true;
                    for (String str : getAnswerList()) {
                        if (TextUtils.isEmpty(str)) {
                            isFinish = false;
                        }
                    }
                    if (mOnInputClickListener != null) {
                        if (isFinish) {
                            mOnInputClickListener.inputTextState(true);
                        } else {
                            mOnInputClickListener.inputTextState(false);
                        }

                    }

                } else {
                    if (mOnInputClickListener != null) {
                        mOnInputClickListener.inputTextState(false);
                    }
                    setLastCheckedSpanText("");
                }

            }
        });
        et.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();

                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    int temp = et.getOffsetForPosition(x, y);
                    et.setSelection(temp);
//                    et.requestFocus();
                    if (mOnInputClickListener != null) {
                        mOnInputClickListener.onClickInput();
                        mOnInputClickListener.getPosition(mCheckedSpan.position);
                    }

                }
                return true;
            }
        });

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

                ReplaceSpan[] link = buffer.getSpans(off, off, ReplaceSpan.class);

                if (link.length != 0) {
                    //Span的点击事件
                    if (action == MotionEvent.ACTION_DOWN) {
                        link[0].onReplaceSpanClick();

                    }
                    return true;
                }
            }
            return false;
        }
    };


    /**
     * 隐藏软键盘
     */
    public static void hideInput(EditText et_msg) {
        try {
            InputMethodManager imm = (InputMethodManager) et_msg.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showInput(){
        try {
            InputMethodManager input = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            input.showSoftInput(et,0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnClick(int position, ReplaceSpan span) {
        if (et.getVisibility() == GONE) {
            et.setVisibility(VISIBLE);
        }
        if (mOnInputClickListener != null) {
            mOnInputClickListener.onClickInput();
            //点击填空题的横线 进入点击事件
            mOnInputClickListener.getPosition(position);
        }

        mCheckedSpan = span;

        //通过rf计算出et当前应该显示的位置
        RectF rf = drawSpanRect(span);
        //设置EditText填空题中的相对位置
        setEtXY(rf);
        setSpanCheckedColor();

        //2. 再次赋值
        mCheckedSpan.setText(span.getText());
        //3. 取出里面有文字  把文字取出来给et
        et.setText(span.getText());
        et.setSelection(et.getText().toString().length());


        //   重新绘制
        mCheckedSpan.setCurPosition(position);
        et.requestFocus();
        tv.invalidate();
    }


    /**
     * 下一个填空
     */
    public synchronized void next() {
        try {
            if (mCheckedSpan == null) {
                UIHandler.getInstance().postDelayed(() -> {
                    OnClick(0, mSpans.get(0));
                }, 500);

            } else {
                if (mCheckedSpan.getPosition() == mSpans.size() - 1) {
                    OnClick(0, mSpans.get(0));
                } else {
                    int nextPosition = mCheckedSpan.getPosition() + 1;
                    OnClick(nextPosition, mSpans.get(nextPosition));
                }
            }
        } catch (Exception ex) {

        }

    }

    /**
     * 得到答案的list
     *
     * @return
     */
    public List<String> getAnswerList() {
        List<String> myAnswerList = new ArrayList<>();
        for (int i = 0; i < mSpans.size(); i++) {
            myAnswerList.add(mSpans.get(i).getText());
        }
        return myAnswerList;
    }

    /**
     * 得到答案的str
     * 逗号分隔
     */
    public String getAnswerStr() {
        String str = "";
        for (int i = 0; i < mSpans.size(); i++) {
            if (i == mSpans.size() - 1) {
                str += mSpans.get(i).getText();
            } else {
                str += mSpans.get(i).getText() + ",";
            }
        }
        return str;
    }


    /**
     * 给上一次的span设置值
     * @param text
     */
    private void setLastCheckedSpanText(String text) {
        if (mCheckedSpan != null) {
            mCheckedSpan.setText(text);
        }
    }

    /**
     * 设置选中span颜色
     */
    private void setSpanCheckedColor() {
        mCheckedSpan.setDrawTextColor(et_color);
    }

    //设置EditText填空题中的相对位置
    public void setEtXY(RectF rf) {
        //设置et w,h的值
        LayoutParams lp = (LayoutParams) et.getLayoutParams();
        lp.width = (int) (rf.right - rf.left);
        lp.height = (int) (rf.bottom - rf.top);
        //设置et 相对于tv x,y的相对位置
        lp.leftMargin = (int) (tv.getLeft() + rf.left);
        lp.topMargin = (int) (tv.getTop() + rf.top);
        et.setLayoutParams(lp);

    }

    private RectF mRf;
    private int mFontT; // 字体top
    private int mFontB;// 字体bottom

    private RectF drawSpanRect(ReplaceSpan s) {
        Layout layout = tv.getLayout();
        if (layout != null) {
            Spannable buffer = (Spannable) tv.getText();
            int l = buffer.getSpanStart(s);
            int r = buffer.getSpanEnd(s);
            int line = layout.getLineForOffset(l);
            int l2 = layout.getLineForOffset(r);
            if (mRf == null) {
                mRf = new RectF();
                Paint.FontMetrics fontMetrics = tv.getPaint().getFontMetrics();
                mFontT = (int) fontMetrics.ascent;
                mFontB = (int) fontMetrics.descent;
            }
            mRf.left = layout.getPrimaryHorizontal(l);
            mRf.right = layout.getSecondaryHorizontal(r);
            // 通过基线去校准
            line = layout.getLineBaseline(line);
            mRf.top = line + mFontT;
            mRf.bottom = line + mFontB;
        }

        return mRf;
    }


    /**
     * 增加et输入
     *
     * @param number
     */
    public void addText(String number) {
        int index = et.getSelectionStart();
        Editable editable = et.getText();
        editable.insert(index, number);
    }

    /**
     * 删除
     */
    public void delText() {
        String str = et.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            int index = et.getSelectionStart();
            Editable editable = et.getText();
            if (index != 0) {
                editable.delete(index - 1, index);
            }
        }
    }


    /**
     * 请求发生点击填空题事件
     */
    public void requestInputEvent() {
        if (mCheckedSpan == null) {
            if (ListUtils.isNotEmpty(mSpans)) {
                UIHandler.getInstance().postDelayed(() -> {
                    OnClick(0, mSpans.get(0));
                }, 500);
            }
        } else {
            if (ListUtils.isNotEmpty(mSpans)) {
                OnClick(mCheckedSpan.position, mCheckedSpan);
            }
        }

    }

    /**
     * 提交后禁止修改（tv重新绘制，et失去焦点并隐藏）
     */
    public void submitAndEnable() {
        tv.invalidate();
        et.setVisibility(View.GONE);
        tv.setMovementMethod(null);
    }


    /**
     * 返回tv
     *
     * @return
     */
    public TextView getTv() {
        return tv;
    }


    /**
     * 将空答案填充进去,不可修改
     */
    public void setAnswerList() {
        if (ListUtils.isNotEmpty(mSpans)) {
            List<String> list = new ArrayList<>();
            for (ReplaceSpan bean : mSpans) {
                list.add("");
            }
            setAnswerList(list, true);
        }

    }


    /**
     * 将答案填充进去,不可修改
     */
    public void setAnswerList(List<String> answers) {
        setAnswerList(answers, true);
    }

    /**
     * 将答案填充进去
     *
     * @param answers
     * @param notModify true 不能修改
     */
    public void setAnswerList(List<String> answers, boolean notModify) {
        setAnswerList(answers, null, 0, false, notModify);
    }

    /**
     * 将答案填充进去
     *
     * @param answers    答案str
     * @param isTrueList 答案对错
     * @param failColor  错误的颜色
     * @param isLineUse  颜色是否应用到下划线
     */
    public void setAnswerList(List<String> answers, List<Boolean> isTrueList, int failColor, boolean isLineUse) {
        setAnswerList(answers, isTrueList, failColor, isLineUse, true);
    }

    /**
     * 将答案填充进去
     *
     * @param answers    答案str
     * @param isTrueList 答案对错
     * @param failColor  错误的颜色
     * @param isLineUse  颜色是否应用到下划线
     * @param notModify  true 不能修改
     */
    private void setAnswerList(List<String> answers, List<Boolean> isTrueList, int failColor, boolean isLineUse, boolean notModify) {
        try {
            setTvContent(questionTitle, isShowIcon, answers);
            if (ListUtils.isNotEmpty(answers)) {
                for (int i = 0; i < answers.size(); i++) {
                    mSpans.get(i).setText(answers.get(i));
                    if (ListUtils.isNotEmpty(isTrueList)) {
                        mSpans.get(i).setDrawTextColor(isTrueList.get(i) ? et_color : failColor, isLineUse);
                    }
                }
            }
            if (notModify) {
                submitAndEnable();
            }
        } catch (Exception e) {
            Log.e("InputView", e.getMessage());
        }
    }

    private OnInputClickListener mOnInputClickListener;

    public void setOnInputClickListener(OnInputClickListener onInputClickListener) {
        mOnInputClickListener = onInputClickListener;
    }

    public interface OnInputClickListener {
        //得到输入框的位置
        void getPosition(int num);

        void onClickInput();

        //输入的 提交按钮的状态  是否可以提交
        void inputTextState(boolean isSubmit);
    }

    /**
     * 解决屏幕熄屏/亮屏 view重复绘制的问题
     *
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (getVisibility() == VISIBLE && visibility == View.VISIBLE && mCheckedSpan != null && mOnInputClickListener != null) {
            requestInputEvent();
        }
    }
}
