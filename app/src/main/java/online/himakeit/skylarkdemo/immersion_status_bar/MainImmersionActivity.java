package online.himakeit.skylarkdemo.immersion_status_bar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import online.himakeit.skylarkdemo.R;

/**
 * Created by：LiXueLong 李雪龙 on 2017/10/17 15:14
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class MainImmersionActivity extends AppCompatActivity implements View.OnClickListener {

    Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersion_demo);
        findViewById(R.id.btn_immersion_normal).setOnClickListener(this);
        findViewById(R.id.btn_immersion_pic).setOnClickListener(this);
        findViewById(R.id.btn_immersion_nav).setOnClickListener(this);
        findViewById(R.id.btn_immersion_actionbar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_immersion_normal:
                mIntent = new Intent(MainImmersionActivity.this,ImmersionNormalActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_immersion_pic:
                mIntent = new Intent(MainImmersionActivity.this,ImmersionPicActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_immersion_nav:
                mIntent = new Intent(MainImmersionActivity.this,ImmersionNavActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_immersion_actionbar:
                mIntent = new Intent(MainImmersionActivity.this,ImmersionActionBarActivity.class);
                startActivity(mIntent);
                break;
        }

    }
}
