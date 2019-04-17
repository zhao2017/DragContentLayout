package test.com.zh.dragcontentlayout.view;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * 触摸事件
 *
 * @author :多选填空题进行的一个点击事件的处理
 */

public class MultipleTouchLinkMovementMethod extends LinkMovementMethod {
    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        //  ACTION_DOWN             = 0;
        //  ACTION_CANCEL           = 3;
        //  public static final int ACTION_UP               = 1;
        // ACTION_MOVE             = 2
        Log.e("event", "event.action==" + event.getAction());
        if (action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();
            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            Log.e("event", "x==" + x + ";y==" + y + ";line==" + line + ";off==" + off);
            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            if (link.length != 0) {
                Log.e("method", "------我执行了啊----");
                link[0].onClick(widget);
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }
        return false;
    }
}
