package com.zhuchao.android.tpk50ds.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.zhuchao.android.tpk50ds.R;
import com.zhuchao.android.tpk50ds.data.App;
import com.zhuchao.android.tpk50ds.utils.AppMain;
import com.zhuchao.android.tpk50ds.utils.GlideMgr;

public class AppAdapter extends BaseAdapter {

    private SparseArray<App> apps = new SparseArray<>();

    private int listMode = 0;

    public Context mContext;

    public AppAdapter(Context context) {
        mContext = context;
    }

    /**
     * 0 GridView
     * 1 ListView
     * */
    public void setListMode(int listMode) {
        this.listMode = listMode;
    }

    public void setApps(SparseArray<App> apps) {

        if (this.apps != null) {
            this.apps.clear();
            this.apps = null;
        }
        if (apps != null) {
            this.apps = apps.clone();
        }
    }

    @Override
    public int getCount() {
        return this.apps != null ? this.apps.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (this.listMode) {
                case 0:
                    convertView = LayoutInflater.from(AppMain.ctx()).inflate(R.layout.apps_item, null);
                    viewHolder.iv = convertView.findViewById(R.id.app_icon);
                    viewHolder.tv = convertView.findViewById(R.id.app_title);
                    break;
                case 1:
                    convertView = LayoutInflater.from(AppMain.ctx()).inflate(R.layout.chooseactivity_item, null);
                    viewHolder.iv = convertView.findViewById(R.id.activity_icon);
                    viewHolder.tv = convertView.findViewById(R.id.activity_title);
                    break;
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        App app = apps.get(position);
        try {
            Log.d("xiaolp","app = "+app+" viewHolder = "+viewHolder);
            GlideMgr.loadNormalDrawableImg(mContext, app.getIcon(), viewHolder.iv);
            viewHolder.tv.setText(app.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
//        viewHolder.iv.setImageDrawable(app.getIcon());

        return convertView;
    }

    public void release() {
        if (apps != null) {
            apps.clear();
            apps = null;
        }
    }
}
