package test.com.zh.dragcontentlayout.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.com.zh.dragcontentlayout.R;
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
    private String content = "<p>途中的两个球相比[space][space]的撒大家京东徐返多少发奥[space]奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥吧范发[space]卡了</p>";

    public static void navToActivity(Context context) {
        Intent intent = new Intent(context, SelectWordsFillBlanksActivity3.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_words_fill_blanks);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        selectWordsView.setData(SelectWordsFillBlanksActivity3.this, content);
    }

}
