package test.com.zh.dragcontentlayout.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

/**
 * 创建日期：2019/4/8
 * 描述:
 *
 * @author: zhaoh
 */
public class MyReplacementSpan extends ReplacementSpan {
    /**
     * Returns the width of the span(返回值就是Span替换文字后所占的宽度 )
     */
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return 0;
    }

    /**
     * Draws the span into the canvas.
     * 在TextView绘制时被调用，与此同时，会把canvas，text，paint以及一堆坐标传给我们，我们覆盖这个方法，就可以在特定位置画一些我们想画的东西了。
     */
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

    }
}
