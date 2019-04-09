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
 * 创建日期：2019/4/9
 * 描述:
 *
 * @author: zhaoh
 */
public class RadiusTextSpan extends ReplacementSpan {

    private Context mContext;
    public RadiusTextSpan(Context context) {
        this.mContext = context;

    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return DisplayUtils.dip2px(15);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            paint.setStrokeWidth(DisplayUtils.dip2px(1));
            paint.setColor(ContextCompat.getColor(mContext, R.color.color_0093e8));
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
          Log.e("draw","text=="+text+";start=="+start+";end=="+end+";x=="+x+";top=="+top+";y=="+y+";bottom=="+bottom);
           float bottom1 = paint.getFontMetrics().bottom;
           float y1 = y + bottom1;
           float ascent = paint.getFontMetrics().ascent;
           float descent = paint.getFontMetrics().descent;
           // 字体高度的一半
           float halftextheight = (descent-ascent)/2;
           // y是 baseline;
          // 需要拿到字体中间位置centre的纵坐标
               // 需要计算出baseline到字体中间位置的距离。
                       //需要知道baseline到descent的位置
                       float fromBaseToDscentSpace = descent-y;
               float fromBaseToCentreSpace = halftextheight-fromBaseToDscentSpace;
               // 此时可以算出字体中间位置的纵坐标；
          float textCentreFloat = y-fromBaseToCentreSpace;
          Log.e("draw1","y=="+y+";halftextheight=="+halftextheight+";fromBaseToDscentSpace=="+fromBaseToDscentSpace+";textCentreFloat=="+textCentreFloat);
//           RectF rectF = new RectF(x,y1+DisplayUtils.dip2px(30),DisplayUtils.dip2px(30)+x,y1);
         RectF rectF = new RectF(0,DisplayUtils.dip2px(20),DisplayUtils.dip2px(20),0);
           CharSequence ellipsize = TextUtils.ellipsize(text, (TextPaint) paint, DisplayUtils.dip2px(15), TextUtils.TruncateAt.END);
           int width = (int) paint.measureText(ellipsize, 0, ellipsize.length());
            Log.e("draw","ascent=="+ascent+";descent=="+descent+";with=="+width);
             width = (DisplayUtils.dip2px(15) - width) / 2;
            canvas.drawRoundRect(rectF,DisplayUtils.dip2px(2),DisplayUtils.dip2px(2),paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ContextCompat.getColor(mContext,R.color.color_f7f7f9));
            canvas.drawRoundRect(rectF,DisplayUtils.dip2px(2),DisplayUtils.dip2px(2),paint);
    }
}
