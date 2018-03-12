package online.himakeit.skylarkdemo.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import online.himakeit.skylarkdemo.R;

/**
 * @author：LiXueLong
 * @date:2018/3/12-18:48
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：HandlerActivity
 */
public class HandlerActivity extends AppCompatActivity {

    TextView mTvMsg;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        mTvMsg = findViewById(R.id.tv_handler);
        mHandler = new MyHandler(this);
        mHandler.sendEmptyMessageDelayed(0, 4000);
    }

    public void setTextView(String msg) {
        if (mTvMsg != null) {
            mTvMsg.setText(msg);
        }
    }

    private static class MyHandler extends Handler {

        WeakReference<HandlerActivity> mActivityWeakReference;

        public MyHandler(HandlerActivity activity) {
            mActivityWeakReference = new WeakReference<HandlerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            HandlerActivity handlerActivity = mActivityWeakReference.get();
            if (handlerActivity != null) {
                handlerActivity.setTextView("修改数据了");
            }
        }
    }
}
