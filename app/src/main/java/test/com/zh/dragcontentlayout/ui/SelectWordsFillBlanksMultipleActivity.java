package test.com.zh.dragcontentlayout.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.com.zh.dragcontentlayout.R;
import test.com.zh.dragcontentlayout.utils.DisplayUtils;
import test.com.zh.dragcontentlayout.view.MultipleSelectWordsFillBlanksView;

/**
 * 创建日期：2019/4/9
 * 描述: 选择填空的demo
 *
 * @author: zhaoh
 */
public class SelectWordsFillBlanksMultipleActivity extends Activity {

    @BindView(R.id.select_words_view)
    MultipleSelectWordsFillBlanksView selectWordsView;
    @BindView(R.id.taglayout)
    TagFlowLayout taglayout;
    private String content = "<p>rule<br/>[space]<br/>rule<br/>[space]<br/></p>";

    private List<String> mList = new ArrayList<>();

    private List<String> answerList = new ArrayList<>();

    public static void navToActivity(Context context) {
        Intent intent = new Intent(context, SelectWordsFillBlanksMultipleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_words_fill_blanks2);
        ButterKnife.bind(this);
        initData1();
        initData();
    }

    private void initData1() {
        for (int j = 0; j < 4; j++) {
            answerList.add("j" + j);
        }
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    mList.add("work");
                    break;
                case 1:
                    mList.add("milk");
                    break;
                case 2:
                    mList.add("hi");
                    break;
                case 3:
                    mList.add("hade");
                default:
                    mList.add("playbasketball");
                    break;

            }

        }
        selectWordsView.setData(content);
//        selectWordsView.setData(content, answerList);
        taglayout.setAdapter(new TagAdapter<String>(mList) {
            private int dp2 = DisplayUtils.dip2px(2);
            private int dp8 = DisplayUtils.dip2px(8);
            private int dp10 = DisplayUtils.dip2px(10);

            @Override
            public View getView(FlowLayout parent, int position, String str) {
                TextView textView = new TextView(parent.getContext());
                textView.setPadding(dp8, dp2, dp8, dp2);
                textView.setTextSize(20);
                textView.setText(str);
                textView.setTextColor(ContextCompat.getColor(SelectWordsFillBlanksMultipleActivity.this, R.color.color_666));
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
                int currentSpanPostion = selectWordsView.getCurrentSpanPostion();
             /*   Toast toast = Toast.makeText(SelectWordsFillBlanksActivity3.this, null, Toast.LENGTH_SHORT);
                toast.setText("span的位置为=" + currentSpanPostion);
                toast.show();*/
                String currentQuestion = mList.get(position);
                Log.e("currentQuestion", "currentQuestion==" + currentQuestion);
                selectWordsView.setInsertTextData(currentQuestion, currentSpanPostion);
                return false;
            }
        });
        selectWordsView.setOnSingleSelectWordsFillBlanksClickListener(new MultipleSelectWordsFillBlanksView.OnSelectWordsFillBlanksViewClickListener() {
            @Override
            public void singleSelectWordsFillBlanksState(boolean isComplete) {
            }
        });

    }
}