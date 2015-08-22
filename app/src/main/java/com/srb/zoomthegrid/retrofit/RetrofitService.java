package com.srb.zoomthegrid.retrofit;

import retrofit.RestAdapter;

public class RetrofitService {

    private static ZoomApis mApis = null;

    public static ZoomApis getApis() {

        if (mApis == null) {
            mApis = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(ApiConstants.BASE_URL)
                    .build()
                    .create(ZoomApis.class);
        }
        return mApis;
    }
}
