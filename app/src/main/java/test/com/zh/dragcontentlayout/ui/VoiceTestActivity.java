package test.com.zh.dragcontentlayout.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.com.zh.dragcontentlayout.R;
import test.com.zh.view.VoiceView;

/**
 * 创建日期：2019/4/1
 * 描述:
 *
 * @author: zhaoh
 */
public class VoiceTestActivity extends Activity {

    private static final String TAG = VoiceTestActivity.class.getSimpleName();
    @BindView(R.id.voice)
    VoiceView voice;

    private List<String> mList = new ArrayList<>();

    private String voiceUrl = "http://test.video.juziwl.cn/exuetea/android/20190423/61e3d84d8d684e28ae28f4b230feee97.mp4";
    private String voiceUrl2 = "http://test.video.juziwl.cn/exuetea/android/20190424/8607fd453d6945fc990b1a445d698e62.mp4";

    private long time = 10;
    private long time2 = 2;

    public static void navToActivity(Context context) {
        Intent intent = new Intent(context, VoiceTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mList.add("测试数据" + i);
        }
        voice.setVoiceUrlAndLengh(voiceUrl,time);
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private Context mContext;
        private List<String> mList;

        public MyAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View inflate = View.inflate(mContext, R.layout.layout_item, null);
            MyViewHolder myViewHolder = new MyViewHolder(inflate);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            holder.tvConent.setText(mList.get(i));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvConent;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvConent = itemView.findViewById(R.id.tv_content);
            }
        }
    }


    private void initBottomSheet() {
        LinearLayout view = findViewById(R.id.ll_bottom_sheet);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);
        bottomSheetBehavior.setPeekHeight(300);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
              /*  public static final int STATE_DRAGGING = 1;   过渡状态
                public static final int STATE_SETTLING = 2;  视图从脱离手指自由滑动到最终停下的这一小段时间
                public static final int STATE_EXPANDED = 3;
                public static final int STATE_COLLAPSED = 4;
                public static final int STATE_HIDDEN = 5;
                public static final int STATE_HALF_EXPANDED = 6;*/
                switch (i) {
                    case 1:
                        Log.i(TAG, "STATE_DRAGGING 过渡");
                        break;
                    case 2:
                        Log.i(TAG, "STATE_SETTLING 当手指抬起之后");
                        break;
                    case 3:
                        Log.i(TAG, "STATE_EXPANDED 展开");
                        break;
                    case 4:
                        Log.i(TAG, "STATE_COLLAPSED 收起");
                        break;
                    case 5:
                        Log.i(TAG, "STATE_HIDDEN 隐藏");
                        break;
                    case 6:
                        Log.i(TAG, "STATE_HALF_EXPANDED ");
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onSlide(View view, float slideOffset) {
                Log.i(TAG, "v==" + slideOffset);
            }
        });

    }
}
