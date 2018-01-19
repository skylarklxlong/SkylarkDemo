package online.himakeit.skylarkdemo.surfaceview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import online.himakeit.skylarkdemo.R;

/**
 * @author：LiXueLong
 * @date：2018/1/19
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class SurfaceViewActivity extends AppCompatActivity {

    SurfaceHolder holder;
    Paint paint;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfaceview);
        paint = new Paint();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // 锁定整个SurfaceView
                Canvas canvas = holder.lockCanvas();
                // 绘制背景
                Bitmap back = BitmapFactory.decodeResource(
                        SurfaceViewActivity.this.getResources()
                        , R.drawable.bg_login);
                // 绘制背景
                canvas.drawBitmap(back, 0, 0, null);
                // 绘制完成，释放画布，提交修改
                holder.unlockCanvasAndPost(canvas);
                // 重新锁一次，"持久化"上次所绘制的内容
                holder.lockCanvas(new Rect(0, 0, 0, 0));
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int cx = (int) event.getX();
                    int cy = (int) event.getY();
                    // 锁定SurfaceView的局部区域，只更新局部内容;
                    Canvas canvas = holder.lockCanvas(new Rect(cx - 50,
                            cy - 50, cx + 50, cy + 50));
                    // 保存canvas的当前状态
                    canvas.save();
                    // 旋转画布，顺时针旋转
                    canvas.rotate(30, cx, cy);
                    paint.setColor(Color.RED);
                    // 绘制红色方块
                    canvas.drawRect(cx - 40, cy - 40, cx, cy, paint);
                    // 恢复Canvas之前的保存状态
                    canvas.restore();
                    paint.setColor(Color.GREEN);
                    // 绘制绿色方块
                    canvas.drawRect(cx, cy, cx + 40, cy + 40, paint);
                    // 绘制完成，释放画布，提交修改
                    holder.unlockCanvasAndPost(canvas);
                }
                return false;
            }
        });
    }
}
