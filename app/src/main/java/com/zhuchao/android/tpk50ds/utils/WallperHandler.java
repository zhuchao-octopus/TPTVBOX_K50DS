package com.zhuchao.android.tpk50ds.utils;

import android.content.Context;

import com.zhuchao.android.tpk50ds.R;

import java.util.List;


/**
 * Created by Oracle on 2018/1/13.
 */

public class WallperHandler {

    private static final String TAG = WallperHandler.class.getSimpleName();
    private boolean loadOk;
    private boolean loading;
    private List<String> imgUrls;
    private Context context;

    public WallperHandler(Context context) {
        loadOk = false;
        loading = false;
        this.context = context;
    }

    public void release() {
        context = null;
        if (imgUrls != null) {
            imgUrls.clear();
        }
        imgUrls = null;
        onWallperUpdateListener = null;
    }

    public void scanWallper() {
        if (!loading) {
            loading = true;
            new ScanTask().start();
        }
    }

    public List<String> getImgUrls() {
        return loadOk ? imgUrls : null;
    }

    public boolean dataOk() {
        return loadOk;
    }

    private String[] loadDef() {
        String[] imgArr = new String[]{
                String.format("res://com.zhuchao.android.tianpu/%d", R.drawable.iv7),
                String.format("res://com.zhuchao.android.tianpu/%d", R.drawable.ad2),
                String.format("res://com.zhuchao.android.tianpu/%d", R.drawable.ad3),
        };
        return imgArr;
    }

    class ScanTask extends Thread {

        @Override
        public void run() {

            String[] imgArr = null;
        }
    }
    public interface OnWallperUpdateListener {
        void wallperUpdate();
    }

    private OnWallperUpdateListener onWallperUpdateListener;

    public void setOnWallperUpdateListener(OnWallperUpdateListener listener) {
        onWallperUpdateListener = listener;
    }
}
