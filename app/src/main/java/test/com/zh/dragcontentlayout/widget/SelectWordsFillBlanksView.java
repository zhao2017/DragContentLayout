package test.com.zh.dragcontentlayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test.com.zh.dragcontentlayout.R;

/**
 * 创建日期：2019/4/8
 * 描述:
 *
 * @author: zhaoh
 */
public class SelectWordsFillBlanksView extends RelativeLayout {
    private Context mContext;
    private TextView tvContent;
    public SelectWordsFillBlanksView(Context context) {
        this(context,null);
    }

    public SelectWordsFillBlanksView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectWordsFillBlanksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.layout_fill_blank,null);
        tvContent = view.findViewById(R.id.tv_content);
    }
}
