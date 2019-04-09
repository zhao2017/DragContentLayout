package test.com.zh.dragcontentlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 创建日期：2019/4/9
 * 描述:
 *
 * @author: zhaoh
 */
public class SelectWordsFillBlanksView extends RelativeLayout {
    public SelectWordsFillBlanksView(Context context) {
        this(context,null);
    }

    public SelectWordsFillBlanksView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectWordsFillBlanksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
