package online.himakeit.skylarkdemo.immersion_status_bar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.himakeit.skylarkdemo.R;

public class ImmersionPicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersion_pic);
        StatusBarUtils.with(this).init();
    }
}
