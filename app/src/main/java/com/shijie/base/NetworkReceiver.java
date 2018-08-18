package com.shijie.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.shijie.utils.NetworkUtil;

/**
 * Created by haoge on 2018/8/17.
 */

public abstract class NetworkReceiver extends BroadcastReceiver {
    private final static String TAG = NetworkReceiver.class.getName();

    private Activity ActivityContext;
    public NetworkReceiver(Activity context){
        this.ActivityContext=context;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
            //拿到wifi的状态值
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_NEW_STATE,0);
            Log.e(TAG,"wifiState = "+ wifiState);
            switch (wifiState){
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }
        if (!NetworkUtil.isNetworkConnected(context)){
            noNetworkConnected();
           //Toast.makeText(ActivityContext,"网络未连接",Toast.LENGTH_LONG).show();
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            int NetworkCode=0;
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI
                            || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        NetworkCode=NetworkUtil.getNetworkType(context);
                        if (NetworkCode==1){
                           // Toast.makeText(ActivityContext,"现在网络为wifi" ,Toast.LENGTH_LONG).show();
                            //Log.e(TAG, "连上wifi");
                        }else if(NetworkCode==2){
                            Toast.makeText(ActivityContext,"现在网络为数据网络，请注意你的流量" ,Toast.LENGTH_LONG).show();
                            Log.e(TAG, "连上数据网络");
                        }
                    }
                } else {
                   // NetworkCode=NetworkUtil.getNetworkType(context);
                  //  Toast.makeText(ActivityContext,"断开"+getNetworkStr(NetworkCode),Toast.LENGTH_LONG).show();
                    Log.e(TAG, "断开"+getNetworkStr(NetworkCode));
                }
            }
        }
    }
    private String getNetworkStr(int i){
        String type=null;
        if(i==1){
            type="wifi网络";
        }
        else if(i==2||i==3){
            type="数据网络";
        }else if (i==0){
            type="无网络";
        }
        return type;
    }
    public abstract void noNetworkConnected();
    public abstract void NetwordTips();
}