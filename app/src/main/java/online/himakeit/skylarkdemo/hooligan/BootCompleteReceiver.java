package online.himakeit.skylarkdemo.hooligan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author：LiXueLong
 * @date:2018/1/3
 * @mail1:skylarklxlong@outlook.com
 * @mail2:li_xuelong@126.com
 * @des:
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            HooliganActivity. startHooligan();
        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            HooliganActivity. killHooligan();
        }
    }
}
