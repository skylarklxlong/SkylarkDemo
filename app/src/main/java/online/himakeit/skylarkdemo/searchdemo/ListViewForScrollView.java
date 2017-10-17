package online.himakeit.skylarkdemo.searchdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/9 16:48
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:一个可以兼容ScrollView的ListView
 */
public class ListViewForScrollView extends ListView {
    public ListViewForScrollView(Context context) {
        super(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // TODO: 2017/8/9 只需要重写这一个方法就可以了，重写后可以使ListView在ScrollView中展开
    /**
     * 传入一个固定值expandSpec来绘制ListView的高。
     * http://blog.csdn.net/liaoinstan/article/details/50509122
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
