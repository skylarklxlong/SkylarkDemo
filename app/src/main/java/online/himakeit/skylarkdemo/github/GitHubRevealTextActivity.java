package online.himakeit.skylarkdemo.github;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import online.himakeit.skylarkdemo.R;
import online.himakeit.skylarkdemo.github.reveal.RevealTextView;
import online.himakeit.skylarkdemo.github.shimmer.Shimmer;
import online.himakeit.skylarkdemo.github.shimmer.ShimmerTextView;
import online.himakeit.skylarkdemo.github.typetext.TypeTextView;

/**
 * Created by：LiXueLong 李雪龙 on 2017/11/13 13:59
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class GitHubRevealTextActivity extends AppCompatActivity {

    ShimmerTextView shimmerTextView;
    RevealTextView revealTextView;
    TypeTextView typeTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_revealtextview);

        enableLayoutFullScreen();

        shimmerTextView = (ShimmerTextView) findViewById(R.id.shimmertextview);
        revealTextView = (RevealTextView) findViewById(R.id.revealtextview);
        typeTextView = (TypeTextView) findViewById(R.id.typeTextView);

        typeTextView.setOnTypeViewListener(new TypeTextView.OnTypeViewListener() {
            @Override
            public void onTypeStart() {

            }

            @Override
            public void onTypeOver() {

            }
        });

        typeTextView.start("心动是等你的留言，渴望是常和你见面，甜蜜是和你小路流连，温馨是看着你清澈的双眼，爱你的感觉真的妙不可言！");

        shimmerTextView.setText("0.0");

        revealTextView.setAnimationDuration(1000);
        revealTextView.setLoop(true);
        revealTextView.setAnimatedText("0.0");

        new Shimmer().start(shimmerTextView);
    }

    protected void enableLayoutFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
