package test.com.zh.dragcontentlayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.style.ReplacementSpan;

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
        return DisplayUtils.dip2px(30);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayUtils.dip2px(1));
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_0093e8));
        RectF oval = new RectF(x, y + paint.ascent() - DisplayUtils.dip2px(5), x + DisplayUtils.dip2px(30), y + paint.descent() + DisplayUtils.dip2px(5));
        //x.y均为原字符串文字的参数
        //设置背景矩形，x为文字左边缘的x值，y为文字的baseline的y值。paint.ascent()获得baseline到文字上边缘的值，paint.descent()获得baseline到文字下边缘
        canvas.drawRoundRect(oval, mRadius, mRadius, mPaint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
        //文字 -- 绘制的文字要比原文字小 , 默认小 4sp
        int originalSize = px2sp((int) paint.getTextSize());
        paint.setTextSize(originalSize - 4);
        paint.setColor(mTxtColor);
        int padding = (int) (mSize - paint.measureText(text.subSequence(start, end).toString()));
        canvas.drawText(text, start, end, x + padding / 2, y, paint);//绘制文字

    }

    /**
     * dp转px * @param dp * @return
     */
    public int px2sp(int pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}