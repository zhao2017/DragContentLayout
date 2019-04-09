package test.com.zh.dragcontentlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import test.com.zh.dragcontentlayout.ui.SelectWordsFillBlanksActivity2;
import test.com.zh.dragcontentlayout.ui.SpannableStringActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNextClick(View view) {
        BottomSheetDemoActivity.navToActivity(MainActivity.this);
    }

    public void onClickStringSpannable(View view) {
        SpannableStringActivity.navToActivity(MainActivity.this);
    }

    public void onSelectWordsClick(View view) {
        SelectWordsFillBlanksActivity2.navToActivity(MainActivity.this);
    }
}
