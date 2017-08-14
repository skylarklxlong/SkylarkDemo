package online.himakeit.skylarkdemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import online.himakeit.skylarkdemo.R;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/14 17:02
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public abstract class BaseActivity extends AppCompatActivity {
    private FrameLayout mFLContentView;
    private TextView mTVTitle;
    private TextView mTVTime;
    private ImageView mIVBack;
    private RelativeLayout main_top_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        mTVTitle = (TextView) findViewById(R.id.tv_title);
        mTVTime = (TextView) findViewById(R.id.tv_time);
        mIVBack = (ImageView) findViewById(R.id.iv_back);
        main_top_title = (RelativeLayout) findViewById(R.id.main_top_title);
        mFLContentView = (FrameLayout) findViewById(R.id.base_activity_container);
        mFLContentView.addView(View.inflate(this, getContentView(), null));

        mIVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTVTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/8/14 暂时先不用
//                setmTVTimeClickListener();
            }
        });

        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取中间内容显示区
     *
     * @return
     */
    protected abstract int getContentView();
    /**
     * 初始化事件
     */
    protected abstract void init();
    /**
     * 最右端的点击事件
     */
//    protected abstract void setmTVTimeClickListener();

    public void setmTVTitle(String mString){
        if (mTVTitle != null){
            mTVTitle.setText(mString);
        }
    }
    public void setmTVTime(String mString){
        if (mTVTime != null){
            mTVTime.setText(mString);
        }
    }

}
