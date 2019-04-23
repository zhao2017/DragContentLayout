package test.com.zh.dragcontentlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import test.com.zh.dragcontentlayout.ui.SelectWordsFillBlanksMultipleActivity;
import test.com.zh.dragcontentlayout.ui.SpannableStringActivity;
import test.com.zh.dragcontentlayout.ui.VoiceTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNextClick(View view) {
        VoiceTestActivity.navToActivity(MainActivity.this);
    }

    public void onClickStringSpannable(View view) {
        SpannableStringActivity.navToActivity(MainActivity.this);
    }

    public void onSelectWordsClick(View view) {
        SelectWordsFillBlanksMultipleActivity.navToActivity(MainActivity.this);
    }
}
