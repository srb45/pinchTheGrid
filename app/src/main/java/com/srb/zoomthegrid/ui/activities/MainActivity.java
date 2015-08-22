package com.srb.zoomthegrid.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.srb.zoomthegrid.R;
import com.srb.zoomthegrid.db.CupboardHelper;
import com.srb.zoomthegrid.db.DbSaveCallback;
import com.srb.zoomthegrid.db.models.Picture;
import com.srb.zoomthegrid.retrofit.RetrofitService;
import com.srb.zoomthegrid.ui.adapters.ImageGridAdapter;
import com.srb.zoomthegrid.utils.Logger;
import com.srb.zoomthegrid.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mSmallRecView, mBigRecView;
    private ProgressDialog mDialog;

    private ImageGridAdapter mGridAdapter;
    private ScaleGestureDetector mGestureDetector;

    private List<Picture> mPictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initObjects(findViewById(android.R.id.content));

        mPictures = CupboardHelper.getAll(Picture.class);
        if (mPictures.isEmpty()) {
            mDialog.show();
            getPictures();
        } else {
            mGridAdapter.setPictures(mPictures);
        }
    }

    private void initObjects(View root) {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.app_name);
        mDialog.setMessage("Fetching images...");

        mGridAdapter = new ImageGridAdapter();

        mSmallRecView = UiUtils.getView(root, R.id.MAIN_small_rec_view);
        mSmallRecView.setLayoutManager(new GridLayoutManager(this, 4));
        mSmallRecView.setAdapter(mGridAdapter);
        mSmallRecView.setAlpha(0f);
        mSmallRecView.setVisibility(View.VISIBLE);

        mBigRecView = UiUtils.getView(root, R.id.MAIN_big_rec_view);
        mBigRecView.setLayoutManager(new GridLayoutManager(this, 3));
        mBigRecView.setAdapter(mGridAdapter);
        mBigRecView.setVisibility(View.VISIBLE);
        mBigRecView.setAlpha(1f);

        UiUtils.getView(root, R.id.MAIN_root).setOnTouchListener(this);
        mSmallRecView.setOnTouchListener(this);
        mBigRecView.setOnTouchListener(this);

        mGestureDetector = new ScaleGestureDetector(this, this);
    }

    private void getPictures() {
        RetrofitService.getApis().getPictures(new Callback<List<Picture>>() {
            @Override
            public void success(List<Picture> pictures, Response response) {
                mDialog.setMessage("Saving in Database");
                if (pictures != null) {
                    pictures = pictures.subList(0, 100);
                    mPictures = pictures;

                    String mUrl = "https://placeholdit.imgix.net/~text?txtsize=70&bg=%s&txt=%s&w=150&h=150";
                    for (Picture picture : mPictures) {
                        String url = String.format(Locale.getDefault(), mUrl,
                                randomColor(), "" + mPictures.indexOf(picture));
                        picture.setThumbnailUrl(url);
                    }

                    CupboardHelper.insertOrReplaceList(mPictures, new DbSaveCallback() {
                        @Override
                        public void done() {
                            mDialog.dismiss();

                            mGridAdapter.setPictures(mPictures);
                        }
                    });
                } else {
                    mDialog.dismiss();
                    UiUtils.showToast("Problem loading images");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mDialog.dismiss();
                UiUtils.showToast("Problem loading images");
            }
        });
    }

    private Random mRandom = new Random();

    private String randomColor() {
        return Integer.toHexString(mRandom.nextInt(Integer.parseInt("FFFFFF", 16)));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getPointerCount() > 1) {
            if (mGestureDetector.onTouchEvent(event)) return true;
        } else {
            if (mVisibleGrid == GRID_SMALL) {
                return mSmallRecView.onTouchEvent(event);
            } else if (mVisibleGrid == GRID_BIG) {
                return mBigRecView.onTouchEvent(event);
            }
        }
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Logger.i(TAG, "onScale: " + detector.getFocusX() + "x" + detector.getFocusY()
                + " " + detector.getScaleFactor());
        float scale = detector.getScaleFactor();

        if (mVisibleGrid == GRID_SMALL && scale > 1 && scale < 1.33) {
            mSmallRecView.setScaleX(scale);
            mSmallRecView.setScaleY(scale);

            float alpha = (1.33f - scale) / 0.33f;
            mSmallRecView.setAlpha(alpha);
            mBigRecView.setAlpha(1 - alpha);
        } else if (mVisibleGrid == GRID_BIG && scale < 1 && scale > 0.75) {
            mBigRecView.setScaleX(scale);
            mBigRecView.setScaleY(scale);

            float alpha = (scale - 0.75f) / 0.25f;
            mSmallRecView.setAlpha(1 - alpha);
            mBigRecView.setAlpha(alpha);
        }
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Logger.i(TAG, "onScaleBegin: " + detector.getFocusX() + "x" + detector.getFocusY()
                + " " + detector.getScaleFactor());
        return true;
    }

    private final int GRID_SMALL = 1;
    private final int GRID_BIG = 2;
    private int mVisibleGrid = GRID_BIG;

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        mBigRecView.setScaleX(1);
        mBigRecView.setScaleY(1);
        mSmallRecView.setScaleX(1);
        mSmallRecView.setScaleY(1);

        float scale = detector.getScaleFactor();
        if (mVisibleGrid == GRID_SMALL && scale > 1.1f) {
//            mBigRecView.setVisibility(View.VISIBLE);
//            mSmallRecView.setVisibility(View.GONE);
            mSmallRecView.setAlpha(0f);
            mBigRecView.setAlpha(1f);
            mVisibleGrid = GRID_BIG;
        } else if (mVisibleGrid == GRID_BIG && scale < 0.9f) {
//            mBigRecView.setVisibility(View.GONE);
//            mSmallRecView.setVisibility(View.VISIBLE);
            mSmallRecView.setAlpha(1f);
            mBigRecView.setAlpha(0f);
            mVisibleGrid = GRID_SMALL;
        }
    }
}
