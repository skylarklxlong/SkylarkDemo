package online.himakeit.baidumaptest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
/**
 * @author：LiXueLong
 * @date:2018/2/28-10:52
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：BaiduMapTest1Activity
 */
public class BaiduMapTest1Activity extends AppCompatActivity {

    private static final String TAG = "BaiduMapTest1Activity";

    MapView mapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_baidu_map_test1);
        mapView = findViewById(R.id.map_view);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    0x001);
        }

    }

    private void initLocation() {
        /**
         * 获取LocationManager实列
         */
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /**
         * 通过GPS定位
         * 精确度比较高，但是非常耗电
         */
        String gpsProvider = LocationManager.GPS_PROVIDER;
        /**
         * 通过网络定位
         * 精准度较差，但耗电量比较少
         */
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        /**
         * 很少使用，被动定位
         */
        String passiveProvider = LocationManager.PASSIVE_PROVIDER;
        /**
         * Location对象中包含了经度、纬度、海拔等一系列的位置信息
         */
        Location location = locationManager.getLastKnownLocation(networkProvider);

        Log.e(TAG, "Latitude: " + location.getLatitude() + "Longitude: " + location.getLongitude());
        Toast.makeText(this, "当前位置坐标为：" + "\nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();

        /**
         * 第一个采纳数是位置提供器的类型，
         * 第二个参数是监听位置变化的时间间隔，以毫秒为单位，
         * 第三个参数是监听位置变化的距离间隔，以米为单位，
         * 第四个参数则是LocationListener监听器
         */
        locationManager.requestLocationUpdates(networkProvider, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e(TAG, "onLocationChanged: ");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e(TAG, "onStatusChanged: ");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e(TAG, "onProviderEnabled: ");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.e(TAG, "onProviderDisabled: ");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x001) {
            if (grantResults.length == 2 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION
                    && permissions[1] == Manifest.permission.ACCESS_COARSE_LOCATION) {
                initLocation();
            } else {
                Toast.makeText(this, "not permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        MapView.setMapCustomEnable(false);
        mapView = null;
    }
}
