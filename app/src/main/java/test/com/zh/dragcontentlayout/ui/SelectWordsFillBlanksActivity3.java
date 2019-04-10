package test.com.zh.dragcontentlayout.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.utils.DisplayUtils;
import test.com.zh.dragcontentlayout.view.SelectWordsFillBlanksView;

/**
 * 创建日期：2019/4/9
 * 描述: 选择填空的demo
 *
 * @author: zhaoh
 */
public class SelectWordsFillBlanksActivity3 extends Activity {


    @BindView(R.id.select_words_view)
    SelectWordsFillBlanksView selectWordsView;
    @BindView(R.id.taglayout)
    TagFlowLayout taglayout;
    private String content = "<p>途中的两个球相比[space][space]的撒大家京东徐返多少发奥[space]奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥吧范发[space]卡了</p>";

    private List<String> mList = new ArrayList<>();

    public static void navToActivity(Context context) {
        Intent intent = new Intent(context, SelectWordsFillBlanksActivity3.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_words_fill_blanks2);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            switch (i) {
                case 0:
                    mList.add("激动");
                    break;
                case 1:
                    mList.add("开心");
                    break;
                case 2:
                    mList.add("棒");
                    break;
                case 3:
                    mList.add("a");
                default:
                    mList.add("很好");
                    break;

            }
        }
        selectWordsView.setData(content);
        taglayout.setAdapter(new TagAdapter<String>(mList) {
            private int dp2 = DisplayUtils.dip2px(2);
            private int dp8 = DisplayUtils.dip2px(8);
            private int dp10 = DisplayUtils.dip2px(10);
            @Override
            public View getView(FlowLayout parent, int position, String str) {
                TextView textView = new TextView(parent.getContext());
                textView.setPadding(dp8, dp2, dp8, dp2);
                textView.setTextSize(12);
                textView.setText(str);
                textView.setTextColor(ContextCompat.getColor(SelectWordsFillBlanksActivity3.this,R.color.color_666));
                textView.setBackgroundResource(R.color.color_f7f7f9);
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = dp8;
                lp.rightMargin = dp10;
                textView.setLayoutParams(lp);
                return textView;
            }
        });

        taglayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(SelectWordsFillBlanksActivity3.this,"postion=="+position,Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }
}
