package online.himakeit.skylarkdemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/11 16:26
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        return initViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated: " );
        initData();
        Log.e(TAG, "onActivityCreated: initData() finish");
    }

    public abstract View initViews();

    public abstract void initData();
}
