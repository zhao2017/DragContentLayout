package test.com.zh.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;


/**
 * 自定义Span，用来绘制填空题答案的底
 */

public class ReplaceSpan extends ReplacementSpan {

    private final Context context;
    private String text = "";//保存的String

    private final Paint mPaint;


    private int textWidth;//单词的宽度

    public OnClickListener mOnClick;
    public int position = 0;//回调中的对应Span的ID
    public int curPosition = -1;//当前的位置


    // 下划线是否应用此颜色
    private boolean isLineUse;
    // 传入的颜色
    private int textColor;

    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }

    public ReplaceSpan(Context context, Paint paint, int textWidth) {
        this.context = context;
        mPaint = paint;
        this.textWidth = (int) ((context.getResources().getDisplayMetrics().density) * textWidth + 0.5f);
    }

    public void setDrawTextColor(int res) {
        mPaint.setColor(res);
    }


    public void setDrawTextColor(int res, boolean isLineUse) {
        mPaint.setColor(res);
        this.isLineUse = isLineUse;
        this.textColor = res;
    }

    public void setText(String text) {
        this.text = text;
    }

    // 得到保存在span里面的文字
    public String getText() {
        return text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        //返回自定义Span宽度
        return textWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {

        float bottom1 = paint.getFontMetrics().bottom;
        float y1 = y + bottom1;

        CharSequence ellipsize = TextUtils.ellipsize(this.text, (TextPaint) paint, textWidth, TextUtils.TruncateAt.END);
        int width = (int) paint.measureText(ellipsize, 0, ellipsize.length());

        width = (textWidth - width) / 2;
        //当前位置不绘制底部span
        if (curPosition != -1 && curPosition == position) {
            canvas.drawText("", 0, 0, x + width, (float) y, mPaint);
        } else {
            canvas.drawText(ellipsize, 0, ellipsize.length(), x + width, (float) y, mPaint);
        }

        //需要填写的单词下方画线
        //这里bottom-1，是为解决有时候下划线超出canvas
        Paint linePaint = new Paint();

        linePaint.setColor(isLineUse ? textColor : Color.parseColor("#333333"));
        linePaint.setStrokeWidth(2);
        canvas.drawLine(x, y1, x + textWidth, y1, linePaint);
        curPosition = -1;
    }


    public void onReplaceSpanClick() {
        if (mOnClick != null) {
            mOnClick.OnClick(position, this);
        }
    }

    public interface OnClickListener {
        void OnClick(int position, ReplaceSpan span);
    }


}