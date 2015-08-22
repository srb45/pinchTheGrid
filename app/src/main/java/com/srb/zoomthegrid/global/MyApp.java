package com.srb.zoomthegrid.global;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;
import com.srb.zoomthegrid.db.DataBaseHelper;


public class MyApp extends Application {

    public static final String TAG = MyApp.class.getSimpleName();

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        DataBaseHelper.initDB(this);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.loggingEnabled(true);
        Picasso.setSingletonInstance(builder.build());
    }

    public static Context getContext() {
        return mContext;
    }
}
