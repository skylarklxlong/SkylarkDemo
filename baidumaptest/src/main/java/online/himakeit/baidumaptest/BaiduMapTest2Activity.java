package online.himakeit.baidumaptest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

/**
 * @author：LiXueLong
 * @date:2018/2/28-10:59
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：BaiduMapTest2Activity
 */
public class BaiduMapTest2Activity extends AppCompatActivity {

    private static final String TAG = "BaiduMapTest2Activity";
    private Button requestLocButton;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private TextView driver_city;
    /**
     * 是否首次定位
     */
    boolean isFirstLoc = true;

    /**
     * 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
     * 如果不处理touch事件，则无需继承，直接使用MapView即可
     * 地图控件
     */
    private TextureMapView mMapView = null;
    private BaiduMap mBaidumap;
    /**
     * 定位相关
     */
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 在使用SDK各组件之前初始化context信息，传入ApplicationContext
         * 注意该方法要再setContentView方法之前实现
         */
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map_test2);
        requestLocButton = (Button) findViewById(R.id.driver_change);
        driver_city = (TextView) findViewById(R.id.driver_city);

        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
        requestLocButton.setText("罗盘");
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        mBaidumap.setMyLocationConfigeration(
                                new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
                        break;
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        mBaidumap.setMyLocationConfigeration(
                                new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        mBaidumap.setMyLocationConfigeration(
                                new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
                        break;
                    default:
                        break;
                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);

        /**
         * 初始化地图
         */
        inintmap();
    }

    public void inintmap() {
        /**
         * 地图初始化
         */
        mMapView = (TextureMapView) findViewById(R.id.driver_mTexturemap);
        mBaidumap = mMapView.getMap();
        /**
         * 开启定位图层
         */
        mBaidumap.setMyLocationEnabled(true);
        /**
         * 定位初始化
         */
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        /**
         * 打开gps
         */
        option.setOpenGps(true);
        /**
         * 设置坐标类型
         */
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        /**
         * 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        /**
         * 可选，设置是否需要地址信息，默认不需要
         */
        option.setIsNeedAddress(true);
        /**
         * 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
         */
        option.setIsNeedLocationPoiList(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    public void MyToast(String s) {
        Toast.makeText(BaiduMapTest2Activity.this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            /**
             * map view 销毁后不在处理新接收的位置
             */
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    /**
                     * 此处设置开发者获取到的方向信息，顺时针0-360
                     */
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaidumap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                MyToast("当前所在位置：" + location.getAddrStr());
                Log.e(TAG, "当前所在位置：" + location.getAddrStr());
                driver_city.setText(location.getCity());
            }
        }
    }
}
