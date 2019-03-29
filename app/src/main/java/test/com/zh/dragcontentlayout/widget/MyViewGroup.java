package test.com.zh.dragcontentlayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建日期：2019/3/29
 * 描述:  需求：我们定义一个ViewGroup，内部可以传入0到4个childView，分别依次显示在左上角，右上角，左下角，右下角。
 *
 * @author: zhaoh
 */
public class MyViewGroup extends ViewGroup {

    public MyViewGroup(Context context) {
        this(context,null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            int cl = 0, ct = 0, cr = 0, cb = 0;
            switch (i) {
                case 0:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin;
                    break;
                case 1:
                    cl = getWidth() - cWidth - cParams.leftMargin
                            - cParams.rightMargin;
                    ct = cParams.topMargin;

                    break;
                case 2:
                    cl = cParams.leftMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - cWidth - cParams.leftMargin
                            - cParams.rightMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;

            }
            cr = cl + cWidth;
            cb = cHeight + ct;
            childView.layout(cl, ct, cr, cb);

        }
    }
    /**
     *  计算所有的childView的宽度和高度，然后根据childView的计算结果设置自己的宽高。
     *
     *  onLayout方法中调用getMeasuredWidth方法外其它的地方用getWidth方法就行了。
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式 父级传递过来的
         */
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWith = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算所有childView的宽和高
        measureChildren(widthMeasureSpec,heightMeasureSpec);

         /*
        记录如果是wrap_content是设置的宽和高
                */
        int width = 0;
        int height = 0;
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        // 用于计算左边两个childView的高度
        int lHeight = 0;
        // 用于计算右边两个childView的高度，最终高度取二者之间大值
        int rHeight = 0;
        // 用于计算上边两个childView的宽度
        int tWidth = 0;
        // 用于计算下面两个childiew的宽度，最终宽度取二者之间大值
        int bWidth = 0;

        /**
         *  getWidth方法是在layout方法完成后才有的值
         *  getMeasuredWidth在measure后就有了值
         */
        for (int i = 0; i <cCount ; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            if(i==0||i==1){
                tWidth+=cWidth+cParams.leftMargin+cParams.rightMargin;
            }

            if(i==2||i==3){
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 0 || i == 2)
            {
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }

            if (i == 1 || i == 3)
            {
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }

        }
        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);
        setMeasuredDimension((withMode==MeasureSpec.EXACTLY?sizeWith:width),(heightMode==MeasureSpec.EXACTLY?sizeHeight:height));
    }





    /**
     * 重写父类的该方法，返回MarginLayoutParams的实例，这样就为我们的ViewGroup指定了其LayoutParams为MarginLayoutParams。
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
}
