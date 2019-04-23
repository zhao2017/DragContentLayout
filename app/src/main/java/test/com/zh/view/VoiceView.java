package test.com.zh.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import test.com.zh.dragcontentlayout.R;

/**
 * 创建日期：2019/4/23
 * 描述:
 *
 * @author: zhaoh
 */
public class VoiceView extends FrameLayout {
    private boolean isTeacher = false;
    private  boolean isNeedCountDown = true;
    public VoiceView(Context context) {
        this(context, null);
    }

    public VoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = View.inflate(context, R.layout.layout_speech, this);
        View bg = view.findViewById(R.id.rl_bg);
        TextView tvTime = view.findViewById(R.id.tv_time);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VoiceView, defStyleAttr, 0);
        for (int i = 0; i < typedArray.length(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.VoiceView_isTeacher:
                    isTeacher = typedArray.getBoolean(attr, false);
                    break;
                case R.styleable.VoiceView_isNeedCountDown:
                    isNeedCountDown = typedArray.getBoolean(attr,true);
                default:
                    break;

            }
        }
        typedArray.recycle();
        bg.setBackgroundResource(isTeacher ? R.drawable.shape_100_0_100_0093e8 : R.drawable.shape_100_0_100_ff954a);
        tvTime.setVisibility(isNeedCountDown?View.VISIBLE:View.GONE);
    }
}
