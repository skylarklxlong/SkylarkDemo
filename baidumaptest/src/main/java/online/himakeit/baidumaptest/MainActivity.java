package online.himakeit.baidumaptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author：LiXueLong
 * @date:2018/2/28-8:43
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：MainActivity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_01).setOnClickListener(this);
        findViewById(R.id.btn_02).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_01:
                startActivity(new Intent(MainActivity.this, BaiduMapTest1Activity.class));
                break;
            case R.id.btn_02:
                startActivity(new Intent(MainActivity.this, BaiduMapTest2Activity.class));
                break;
            default:
                break;
        }
    }
}
