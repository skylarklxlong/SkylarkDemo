package online.himakeit.skylarkdemo.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import online.himakeit.skylarkdemo.R;

/**
 * @author：LiXueLong
 * @date:2017/12/29
 * @mail1:skylarklxlong@outlook.com
 * @mail2:li_xuelong@126.com
 * @des:
 */
public class SingleInstanceModeActivity extends AppCompatActivity {

    private static final String TAG = "SingleInstanceMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchmode);
        Log.e(TAG, "onCreate: ");
        findViewById(R.id.btn_launch_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingleInstanceModeActivity.this, SingleTaskModeActivity.class));
            }
        });
        findViewById(R.id.btn_launch_mode_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingleInstanceModeActivity.this, StandardModeActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }
}
