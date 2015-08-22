package com.srb.zoomthegrid.retrofit;

import com.srb.zoomthegrid.db.models.Picture;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

public interface ZoomApis {

    @GET(ApiConstants.PHOTOS)
    void getPictures(Callback<List<Picture>> callback);
}
