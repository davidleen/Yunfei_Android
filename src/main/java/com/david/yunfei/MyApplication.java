package com.david.yunfei;

import android.app.Application;

/**
 *
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HttpUrl.init(this);
    }
}
