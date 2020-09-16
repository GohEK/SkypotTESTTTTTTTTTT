package com.Skypot.app.chche;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ACache.init(this);
    }
}
