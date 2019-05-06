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
import test.com.zh.view.InputView;

/**
 * 创建日期：2019/4/1
 * 描述:
 *
 * @author: zhaoh
 */
public class FillBlanksTestActivity extends Activity {

    private static final String TAG = FillBlanksTestActivity.class.getSimpleName();
    @BindView(R.id.inputView)
    InputView inputView;

    private List<String> mList = new ArrayList<>();


private String context = "<p>张三的爸爸是[space]，妈妈是[space]</p>";

    public static void navToActivity(Context context) {
        Intent intent = new Intent(context, FillBlanksTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fill_blanks);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mList.add("测试数据" + i);
        }
        inputView.setTvContent(context);
        inputView.setOnInputClickListener(new InputView.OnInputClickListener() {
            @Override
            public void getPosition(int num) {

                Log.e("postion","postion=="+num);

            }

            @Override
            public void onClickInput() {


            }

            @Override
            public void inputTextState(boolean isSubmit) {

            }
        });
        inputView.requestInputEvent();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
