package test.com.zh.dragcontentlayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;

import test.com.zh.dragcontentlayout.R;

/**
 * 创建日期：2019/4/8
 * 描述:
 *
 * @author: zhaoh
 */
public class IconTextSpan extends ReplacementSpan {

    private Context mContext;
    private int mBgColorResId; //Icon背景颜色
    private String mText;  //Icon内文字
    private float mBgHeight; // icon背景高度
    private float mBgWith;  // icon背景宽度
    private float mRadius;  //Icon圆角半径
    private float mRightMargin;  //右边距
    private float mTextSize; //文字大小
    private int mTextColorResId; //文字颜色


    private Paint mBgPaint; //icon背景画笔
    private Paint mTextPaint; //icon文字画笔

    public IconTextSpan(Context context, int bgColorResId, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        initDefaultValue(context, bgColorResId, text);
        // 计算背景的宽度
        this.mBgWith = caculateBgWidth(text);
        initPaint();
    }

    private void initPaint() {
        // 初始化背景画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(mContext.getResources().getColor(mBgColorResId));
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        //初始化文字画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mContext.getResources().getColor(mTextColorResId));
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }


    /**
     * 设置右边距
     *
     * @param rightMarginDpValue
     */
    public void setRightMarginDpValue(int rightMarginDpValue) {
        this.mRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightMarginDpValue, mContext.getResources().getDisplayMetrics());
    }

    private float caculateBgWidth(String text) {
        if (text.length() > 1) {
            //多字，宽度=文字宽度+padding
            Rect textRect = new Rect();
            Paint paint = new Paint();
            paint.setTextSize(mTextSize);
            paint.getTextBounds(text, 0, text.length(), textRect);
            float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics());
            return textRect.width() + padding * 2;

        } else {
            //单字，宽高一致为正方形
            return mBgHeight;
        }
    }

    private void initDefaultValue(Context context, int bgColorResId, String text) {
        this.mContext = context.getApplicationContext();
        this.mBgColorResId = bgColorResId;
        this.mBgHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17f, mContext.getResources().getDisplayMetrics());
        this.mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mContext.getResources().getDisplayMetrics());
        this.mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, mContext.getResources().getDisplayMetrics());
        this.mTextColorResId = R.color.white_a;
    }

    /**
     * 设置宽度，宽度=背景宽度+右边距
     *
     * @param paint
     * @param text
     * @param start
     * @param end
     * @param fm
     * @return
     */
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return 0;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

    }
}
