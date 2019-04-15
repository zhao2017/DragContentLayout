package test.com.zh.dragcontentlayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.Log;

import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.utils.DisplayUtils;

/**
 * 〈带背景色的圆角span〉
 */
public class MyRadiusBgSpan extends ReplacementSpan {
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

    /**
     * @param radius 圆角半径
     */
    public MyRadiusBgSpan(Context context, int bgcolor, int txtColor, int radius) {
        this.mContext = context;
        mBgColor = bgcolor;
        mTxtColor = txtColor;
        mRadius = radius;
        mPaint = new Paint();
    }

    public MyRadiusBgSpan(Context context, int postion) {
        this.mContext = context;
        this.mPostion = postion;
        mPaint = new Paint();
    }

    /**
     * @param radius 圆角半径
     */
    public MyRadiusBgSpan(Context context, int bgcolor, int txtColor, int radius, int style) {
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
        if (!TextUtils.isEmpty(questionText)) {
            if (mCurrentInsertSpanPostion == mPostion - 1) {
                return dealWithHaveTextSpanSize(questionText);
            }
        } else {
            // 这个地方是对显示之前数据内容的处理
            if(!TextUtils.isEmpty(oriText)){
                return dealWithHaveTextSpanSize(oriText);
            }
            return DisplayUtils.dip2px(30);
        }

        return DisplayUtils.dip2px(30);
    }

    /**
     *  对于填充文字后Span大小的处理
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
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayUtils.dip2px(1));
        Log.e("postion", "mPostion==" + mPostion + ";currentPositon==" + currentPostion);
        if (mPostion - 1 == currentPostion) {
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_0093e8));
        } else {
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_999));
        }
        //x.y均为原字符串文字的参数
        //设置背景矩形，x为文字左边缘的x值，y为文字的baseline的y值。paint.ascent()获得baseline到文字上边缘的值，paint.descent()获得baseline到文字下边缘
        float textheight = paint.descent() - paint.ascent();
        // 计算出字体头部到要求高度的距离
        float space = (DisplayUtils.dip2px(30) - textheight) / 2;
        // 处理填空数据的逻辑
        if (!TextUtils.isEmpty(questionText)) {
            // 当文本不为空说明插入过来数据了
            dealWithTextCanvas(canvas, x, y, paint, space,questionText);
        } else {
            if(!TextUtils.isEmpty(oriText)){
                dealWithTextCanvas(canvas,x,y,paint,space,oriText);
            }else{
                RectF oval = new RectF(x, y + paint.ascent() - space, x + DisplayUtils.dip2px(30), y + paint.descent() + space);
                canvas.drawRoundRect(oval, DisplayUtils.dip2px(2), DisplayUtils.dip2px(2), mPaint);
            }
        }
        currentPostion = -1;
    }
    private void dealWithTextCanvas(Canvas canvas, float x, int y, Paint paint, float space,String text) {
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

    public interface OnSpanClickListener {
        void onSpanClick(int postion, MyRadiusBgSpan myRadiusBgSpan);
    }


}