package com.srb.zoomthegrid.retrofit;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class BodyGenerator {

    JSONObject mJson = new JSONObject();

    public BodyGenerator() {

    }

    public BodyGenerator put(String key, String value) {
        try {
            mJson.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BodyGenerator put(String key, boolean value) {
        try {
            mJson.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BodyGenerator put(String key, long value) {
        try {
            mJson.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BodyGenerator put(String key, double value) {
        try {
            mJson.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BodyGenerator put(String key, Object value) {
        try {
            mJson.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public TypedInput toTypedInput() {
        return new TypedByteArray("application/json", mJson.toString().getBytes());
    }

}
