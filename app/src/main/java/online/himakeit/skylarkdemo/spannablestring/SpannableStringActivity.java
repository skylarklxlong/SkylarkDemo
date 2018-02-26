package online.himakeit.skylarkdemo.spannablestring;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import online.himakeit.skylarkdemo.R;
import online.himakeit.skylarkdemo.about.WebActivity;

/**
 * @author：LiXueLong
 * @date:2018/2/26-8:15
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：SpannableStringActivity http://blog.csdn.net/u012702547/article/details/49895157
 */
public class SpannableStringActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTvMsg, mTvClickMsg, mTvFgMsg, mTvDelLineMsg, mTvUnderLineMsg,
            mTvPicMsg, mTvScaleMsg, mTvStyleMsg, mTvSubscriptMsg, mTvUrlMsg;
    Button mBtnAddText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable_string);

        mTvMsg = (TextView) findViewById(R.id.tv_spannable);
        mBtnAddText = (Button) findViewById(R.id.btn_spannable_text);
        mTvClickMsg = (TextView) findViewById(R.id.tv_spannable_01);
        mTvFgMsg = (TextView) findViewById(R.id.tv_spannable_02);
        mTvDelLineMsg = (TextView) findViewById(R.id.tv_spannable_03);
        mTvUnderLineMsg = (TextView) findViewById(R.id.tv_spannable_04);
        mTvPicMsg = (TextView) findViewById(R.id.tv_spannable_05);
        mTvScaleMsg = (TextView) findViewById(R.id.tv_spannable_06);
        mTvStyleMsg = (TextView) findViewById(R.id.tv_spannable_07);
        mTvSubscriptMsg = (TextView) findViewById(R.id.tv_spannable_08);
        mTvUrlMsg = (TextView) findViewById(R.id.tv_spannable_09);

        mBtnAddText.setOnClickListener(this);

        setBgForText();

        setClickForText();

        setFgForText();

        setDelLineText();

        setUnderLineText();

        setPicText();

        setScaleText();

        setStyleText();

        setSubscriptText();

        setUrlText();
    }

    /**
     * 设置超链接
     */
    private void setUrlText() {
        SpannableString spannableString = new SpannableString("打电话，发短信，发邮件，打开网页");
        spannableString.setSpan(new URLSpan("tel:13477048980"), 0, 3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new URLSpan("smsto:13477048980"), 4, 7,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new URLSpan("mailto:898309569@qq.com"), 8, 11,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new URLSpan("http://himakeit.online"), 12, 16,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvUrlMsg.setText(spannableString);
        mTvUrlMsg.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 上下标的使用
     */
    private void setSubscriptText() {
        SpannableString spannableString = new SpannableString("(x1 + x2)2 = x12+x22+2x1x2");
        // 设置下标
        spannableString.setSpan(new SubscriptSpan(), 2, 3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置下标字体大小
        spannableString.setSpan(new AbsoluteSizeSpan(30), 2, 3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SubscriptSpan(), 7, 8,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30), 7, 8,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SubscriptSpan(), 14, 15,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30), 14, 15,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SubscriptSpan(), 18, 19,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30), 18, 19,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SubscriptSpan(), 23, 24,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30), 23, 24,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SubscriptSpan(), 25, 26,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30), 25, 26,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置上标
        spannableString.setSpan(new SuperscriptSpan(), 9, 10,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30), 9, 10,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SuperscriptSpan(), 15, 16,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30), 15, 16,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SuperscriptSpan(), 19, 20,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30), 19, 20,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvSubscriptMsg.setText(spannableString);
    }

    /**
     * 设置字体粗体样式
     */
    private void setStyleText() {
        SpannableString spannableString = new SpannableString("字体样式，粗体、斜体等");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 5, 7,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvStyleMsg.setText(spannableString);
    }

    /**
     * 基于X轴的缩放
     */
    private void setScaleText() {
        SpannableString spannableString = new SpannableString("基于X轴缩放");
        /**
         * ScaleXSpan中的参数大于1表示横向扩大，小于1大于0表示缩小，等于1表示正常显示
         */
        spannableString.setSpan(new ScaleXSpan(2), 0, spannableString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvScaleMsg.setText(spannableString);
    }

    /**
     * 在TextView中设置图片
     */
    private void setPicText() {
        SpannableString spannableString = new SpannableString("在TextView中设置图片");
        spannableString.setSpan(new DynamicDrawableSpan() {
            @Override
            public Drawable getDrawable() {
                Drawable drawable = getResources().getDrawable(R.mipmap.skylark);
                drawable.setBounds(0, 0, 150, 150);
                return drawable;
            }
        }, 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvPicMsg.setText(spannableString);
    }

    /**
     * 设置下划线效果
     */
    private void setUnderLineText() {
        SpannableString spannableString = new SpannableString("设置下划线效果");
        spannableString.setSpan(new UnderlineSpan(), 0,
                spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvUnderLineMsg.setText(spannableString);
    }

    /**
     * 设置删除线效果
     */
    private void setDelLineText() {
        SpannableString spannableString = new SpannableString("设置删除线效果");
        spannableString.setSpan(new StrikethroughSpan(), 0,
                spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvDelLineMsg.setText(spannableString);
    }

    /**
     * 设置文本颜色
     */
    private void setFgForText() {
        SpannableString spannableString = new SpannableString("设置文本颜色");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF3030")), 0,
                spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvFgMsg.setText(spannableString);
    }

    /**
     * 给文本设置点击事件
     */
    private void setClickForText() {
        SpannableString spannableString = new SpannableString("点我试试看^_^");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(WebActivity.newTntent(SpannableStringActivity.this, "http://himakeit.online", "LiXueLong'Blog"));
            }
        }, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvClickMsg.setText(spannableString);
        mTvClickMsg.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 设置TextView的背景颜色
     */
    private void setBgForText() {
        /**
         * 定义SpannableString对象，传入的参数即为TextView要显示的文字
         */
        SpannableString spannableString = new SpannableString("设置背景颜色");
        /**
         * 第一个参数传入要设置的背景颜色，
         * 第二个、第三个参数是要让那一段文字设置
         * 第四个参数：
         * SPAN_INCLUSIVE_EXCLUSIVE 前面包括，后面不包括，即在文本前插入新的文本会应用该样式，而在文本后插入新文本不会应用该样式
         * SPAN_INCLUSIVE_INCLUSIVE 前面包括，后面包括，即在文本前插入新的文本会应用该样式，而在文本后插入新文本也会应用该样式
         * SPAN_EXCLUSIVE_EXCLUSIVE 前面不包括，后面不包括
         * SPAN_EXCLUSIVE_INCLUSIVE 前面不包括，后面包括
         */
        spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#FFD700")),
                0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvMsg.setText(spannableString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_spannable_text:
                mTvMsg.append("\n我是新添加的文字！");
                break;
            default:
        }
    }
}
