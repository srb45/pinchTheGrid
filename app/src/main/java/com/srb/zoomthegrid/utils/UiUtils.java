package com.srb.zoomthegrid.utils;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.srb.zoomthegrid.R;
import com.srb.zoomthegrid.global.MyApp;


public class UiUtils {

    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(View parentView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) parentView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            parentView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = parentView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusView = activity.getCurrentFocus();
        if (focusView != null)
            inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
    }

    public static void showToast(String data) {
        Toast.makeText(MyApp.getContext(), data, Toast.LENGTH_SHORT).show();
    }

    public static void displayImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            Picasso.with(MyApp.getContext())
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
