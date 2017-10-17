package online.himakeit.skylarkdemo.immersion_status_bar;

import android.content.Intent;
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
public class MainImmersionFragment extends BaseFragment implements View.OnClickListener {

    View mRootView;
    Intent mIntent;

    @Override
    public View initViews() {
        mRootView = View.inflate(getContext(), R.layout.fragment_immersion,null);
        return mRootView;
    }

    @Override
    public void initData() {

        mRootView.findViewById(R.id.btn_immersion_normal).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_immersion_pic).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_immersion_nav).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_immersion_actionbar).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_immersion_normal:
                mIntent = new Intent(getActivity(),ImmersionNormalActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_immersion_pic:
                mIntent = new Intent(getActivity(),ImmersionPicActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_immersion_nav:
                mIntent = new Intent(getActivity(),ImmersionNavActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_immersion_actionbar:
                mIntent = new Intent(getActivity(),ImmersionActionBarActivity.class);
                startActivity(mIntent);
                break;
        }

    }
}
