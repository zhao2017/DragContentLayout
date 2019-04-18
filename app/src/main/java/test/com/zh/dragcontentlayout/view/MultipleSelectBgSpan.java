package test.com.zh.dragcontentlayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.utils.DisplayUtils;

/**
 * 多选题的span
 */
public class MultipleSelectBgSpan extends ReplacementSpan {
    private int mSize;
    private int mBgColor;
    private int mTxtColor;
    private int mRadius;
    private Paint mPaint;
    private Context mContext;
    public static final int STYLE_FILL = 0;//填充
    public static final int STYLE_STROCK = 1;//扫边。扫边颜色默认和字体颜色一致
    private int mStyle = STYLE_FILL;
    private int mPostion = 0;
    private int currentPostion = 0;
    private String questionText;
    private int mCurrentInsertSpanPostion = 0;
    private float topPadding = DisplayUtils.dip2px(5);
    private float leftPadding = DisplayUtils.dip2px(7);
    private String oriText = "";

    private List<String> mList = new ArrayList<>();

    /**
     * @param radius 圆角半径
     */
    public MultipleSelectBgSpan(Context context, int bgcolor, int txtColor, int radius) {
        this.mContext = context;
        mBgColor = bgcolor;
        mTxtColor = txtColor;
        mRadius = radius;
        mPaint = new Paint();
    }

    public MultipleSelectBgSpan(Context context, int postion, List<String> mQuestionList) {
        this.mContext = context;
        this.mPostion = postion;
        mPaint = new Paint();
        this.mList = mQuestionList;
    }

    /**
     * @param radius 圆角半径
     */
    public MultipleSelectBgSpan(Context context, int bgcolor, int txtColor, int radius, int style) {
        this.mContext = context;
        mBgColor = bgcolor;
        mTxtColor = txtColor;
        mRadius = radius;
        mStyle = style;
        mPaint = new Paint();
    }


    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        paint.setTextSize(paint.getTextSize() - 4);
        mSize = (int) (paint.measureText(text, start, end));
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        // 我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        // 可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        // 这个地方就是对Span的大小做动态改变的逻辑
        return DisplayUtils.getScreenWidth() / 2 - DisplayUtils.dip2px(15);
    }

    /**
     * 对于填充文字后Span大小的处理
     *
     * @param str
     * @return
     */
    private int dealWithHaveTextSpanSize(String str) {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DisplayUtils.sp2px(16));
        float textWith = textPaint.measureText(str);
        if ((textWith + 2 * leftPadding) >= DisplayUtils.dip2px(30)) {
            //表示要显示的字体的宽度超过了正常的边框的界限的时候
            return (int) (textWith + 2 * leftPadding);
        } else {
            //不超过边框的处理
            return DisplayUtils.dip2px(30);
        }
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, final Paint paint) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_0093e8));
        mPaint.setStrokeWidth(DisplayUtils.dip2px(1));
        int index = 0;
        Log.e("ondraw", "text==" + text + ";start==" + start + ";end==" + end + ";x==" + x + ";y==" + y);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        if (mPostion == 1) {
            for (int i = 0; i < mList.size(); i++) {
                RectF rectF = new RectF(DisplayUtils.dip2px(1) + i * (DisplayUtils.dip2px(8) + DisplayUtils.dip2px(30)), y - DisplayUtils.dip2px(6) - DisplayUtils.dip2px(30),
                        DisplayUtils.dip2px(30) + i * (DisplayUtils.dip2px(8) + DisplayUtils.dip2px(30)), y - DisplayUtils.dip2px(6));
                canvas.drawRoundRect(rectF, DisplayUtils.dip2px(2), DisplayUtils.dip2px(2), mPaint);
            }
            // 超屏幕的逻辑处理
            if((DisplayUtils.dip2px(30)*mList.size()+(mList.size()-1)*DisplayUtils.dip2px(8))-DisplayUtils.dip2px(30)>DisplayUtils.getScreenWidth()){

                canvas.drawLine(x,y,DisplayUtils.getScreenWidth(),y,mPaint);
                canvas.drawLine(x,y+textHeight,DisplayUtils.getScreenWidth(),y+textHeight,mPaint);

            }else{
                if((DisplayUtils.dip2px(30)*mList.size()+(mList.size()-1)*DisplayUtils.dip2px(8))>DisplayUtils.getScreenWidth()/2){
                    canvas.drawLine(0,y,DisplayUtils.getScreenWidth(),y,mPaint);
                }else{
                    canvas.drawLine(0, y, DisplayUtils.getScreenWidth() / 2 - DisplayUtils.dip2px(15), y, mPaint);
                }
            }

        } else {
            RectF rectF = new RectF(DisplayUtils.dip2px(1), y - DisplayUtils.dip2px(6) - DisplayUtils.dip2px(30),
                    DisplayUtils.dip2px(30), y - DisplayUtils.dip2px(6));
            canvas.drawRoundRect(rectF, DisplayUtils.dip2px(2), DisplayUtils.dip2px(2), mPaint);
        }
    }

    private void dealWithTextCanvas(Canvas canvas, float x, int y, Paint paint, float space, String text) {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(ContextCompat.getColor(mContext, R.color.color_0093e8));
        textPaint.setTextSize(DisplayUtils.sp2px(16));
        float textWith = textPaint.measureText(text);
        // 此处是要插入的位置
//                canvas.drawText(questionText,x,y,textPaint);
        // 需要根据文字的大小进行方框的重新绘制
        if ((textWith + 2 * leftPadding) >= DisplayUtils.dip2px(30)) {
            //表示要显示的字体的宽度超过了正常的边框的界限的时候
            canvas.drawText(text, x + leftPadding, y, textPaint);
            RectF rectF = new RectF(x, y + paint.ascent() - space, x + textWith + 2 * leftPadding, y + paint.descent() + space);
            canvas.drawRoundRect(rectF, DisplayUtils.dip2px(2), DisplayUtils.dip2px(2), mPaint);
        } else {
            //不超过边框的处理
            float moveSpace = (DisplayUtils.dip2px(30) - textWith) / 2;
            canvas.drawText(text, x + moveSpace, y, textPaint);
            RectF oval = new RectF(x, y + paint.ascent() - space, x + DisplayUtils.dip2px(30), y + paint.descent() + space);
            canvas.drawRoundRect(oval, DisplayUtils.dip2px(2), DisplayUtils.dip2px(2), mPaint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
        }
    }

    /**
     * dp转px * @param dp * @return
     */
    public int px2sp(int pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public void setCurrentPostion(int currentPostion) {
        this.currentPostion = currentPostion;
    }


    public int getCurrentPostion() {
        return currentPostion;
    }

    public void setQuestionText(String questionText, int currentInsertSpanPostion) {
        this.questionText = questionText;
        this.mCurrentInsertSpanPostion = currentInsertSpanPostion;
    }

    public void setOriginalQuestionText(String oriText) {
        this.oriText = oriText;
    }

    public String getQuestionText() {
        return questionText;
    }


    private OnSpanClickListener mOnSpanClickListener;

    public void setOnMultiSelectSpanClickListener(OnSpanClickListener mOnSpanClickListener) {
        this.mOnSpanClickListener = mOnSpanClickListener;
    }

    public interface OnSpanClickListener {
        void onSpanClick(int postion, MultipleSelectBgSpan myRadiusBgSpan);

        void onSpanItemClick(int index);
    }

}