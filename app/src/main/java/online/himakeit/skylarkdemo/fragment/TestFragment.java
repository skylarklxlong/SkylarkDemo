package online.himakeit.skylarkdemo.fragment;

import android.view.View;

import online.himakeit.skylarkdemo.R;
import online.himakeit.skylarkdemo.base.BaseFragment;

/**
 * Created by：LiXueLong 李雪龙 on 2017/9/5 9:58
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class TestFragment extends BaseFragment {

    View mRootView;

    @Override
    public View initViews() {
        mRootView = View.inflate(getContext(), R.layout.fragment_test,null);
        return mRootView;
    }

    @Override
    public void initData() {

    }
}
