package com.srb.zoomthegrid.db.models;

import com.srb.zoomthegrid.db.BaseDbItem;
import com.srb.zoomthegrid.db.CupboardHelper;

public class Picture extends BaseDbItem {

    private int albumId;

    private int id;

    private String title;

    private String url;

    private String thumbnailUrl;

    public void insertInDb() {
        Picture picture = CupboardHelper.get(getClass(), "id=" + id);
        if (picture != null) {
            this._id = picture._id;
        }
        CupboardHelper.insertOrReplace(this);
    }

    public int getAlbumId() {
        return albumId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
