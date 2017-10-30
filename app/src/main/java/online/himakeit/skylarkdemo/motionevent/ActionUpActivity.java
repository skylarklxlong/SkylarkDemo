package online.himakeit.skylarkdemo.motionevent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import online.himakeit.skylarkdemo.R;

/**
 * Created by：LiXueLong 李雪龙 on 2017/10/27 14:07
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class ActionUpActivity extends AppCompatActivity {

    private static final String TAG = "ActionUpActivity";

    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionup);

        linearLayout = (LinearLayout) findViewById(R.id.ll_action_up);

        linearLayout.setOnTouchListener(new View.OnTouchListener() {

            int startX;
            int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "onTouch: down");
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        Log.e(TAG,"startX: "+startX + " startY: " + startY);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "onTouch: up");
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        Log.e(TAG,"startX: "+startX + " startY: " + startY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "onTouch: move");
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        int dx = newX - startX;
                        int dy = newY - startY;

                        Log.e(TAG,"移动的dx: "+dx + " 移动的dy: " + dy);

                        //重新初始化手指的位置
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                }
                // TODO: 2017/10/30 如果这里为false，则只能监听到ACTION_DOWN,其他的都监听不到
                return true;
//                return ActionUpActivity.super.onTouchEvent(event);
            }
        });
    }
}
