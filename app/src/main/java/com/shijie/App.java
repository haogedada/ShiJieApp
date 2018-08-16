package com.shijie;

import android.app.Application;

/**
 * Created by ZHT on 2017/4/15.
 */

public class App extends Application {
    private static App mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    public static App getApplication() {
        return mContext;
    }
}
