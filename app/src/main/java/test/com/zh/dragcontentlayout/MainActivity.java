package test.com.zh.dragcontentlayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import test.com.zh.dragcontentlayout.ui.FillBlanksTestActivity;
import test.com.zh.dragcontentlayout.ui.SelectWordsFillBlanksMultipleActivity;
import test.com.zh.dragcontentlayout.ui.SpannableStringActivity;
import test.com.zh.dragcontentlayout.utils.ToastUtils;

public class MainActivity extends AppCompatActivity {

    private List<String> mList = new ArrayList<>();
   private String[]  permissons = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermissons();


    }

    private void initPermissons() {
        mList.clear();
        for (int i = 0; i < permissons.length; i++) {
            if(ContextCompat.checkSelfPermission(this,permissons[i])!= PackageManager.PERMISSION_GRANTED){
                mList.add(permissons[i]);
            }
        }
        if(mList.size()>0){
            // /有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this,permissons,100);
        }else{
            ToastUtils.showToast("权限已全部通过");
        }

    }

   /* public void onNextClick(View view) {
        VoiceTestActivity.navToActivity(MainActivity.this);
    }*/
    public void onNextClick(View view) {
        FillBlanksTestActivity.navToActivity(MainActivity.this);
    }

    public void onClickStringSpannable(View view) {
        SpannableStringActivity.navToActivity(MainActivity.this);
    }

    public void onSelectWordsClick(View view) {
        SelectWordsFillBlanksMultipleActivity.navToActivity(MainActivity.this);
    }
}
