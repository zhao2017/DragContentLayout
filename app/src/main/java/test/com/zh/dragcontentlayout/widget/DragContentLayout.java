package test.com.zh.dragcontentlayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 创建日期：2019/3/29
 * 描述:
 *
 * @author: zhaoh
 */
public class DragContentLayout extends RelativeLayout {

    public DragContentLayout(Context context) {
        this(context,null);
    }

    public DragContentLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }




}
