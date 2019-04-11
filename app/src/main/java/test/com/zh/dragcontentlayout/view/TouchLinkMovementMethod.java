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
 * Created by yangle on 2017/10/9.
 */

public class TouchLinkMovementMethod extends LinkMovementMethod {

    private static TouchLinkMovementMethod touchLinkMovementMethod;

    private TouchLinkMovementMethod(){

    }

    public static TouchLinkMovementMethod getInstance(){
        if(touchLinkMovementMethod==null){
            touchLinkMovementMethod = new TouchLinkMovementMethod();
        }
        return touchLinkMovementMethod;
    }


    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

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
           ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            if (link.length != 0) {
                Log.e("method","------我执行了啊----");
                link[0].onClick(widget);
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }

        return false;
    }
}
