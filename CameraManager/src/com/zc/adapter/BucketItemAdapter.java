package com.zc.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zc.cameramanager.R;
import com.zc.photoalbum.ImageItem;
import com.zc.photoalbum.ImageLoaderUtil;

public class BucketItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageItem> dataList;

    public BucketItemAdapter(Context mContext, List<ImageItem> dataList) {
        super();
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return null != dataList ? dataList.size() : 0;
    }

    @Override
    public ImageItem getItem(int position) {
        return null != dataList ? dataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        private ImageView iv;
        private ImageView selected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.item_image_bucket_item, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_image_item);
            holder.selected = (ImageView) convertView
                    .findViewById(R.id.iv_image_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (dataList.get(position).isSelected)
            holder.selected.setVisibility(View.VISIBLE);
        else
            holder.selected.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(dataList.get(position).thumbnailPath)) {
            ImageLoaderUtil.loadLoaclImage(holder.iv,
                    dataList.get(position).thumbnailPath);
        } else {
            ImageLoaderUtil.loadLoaclImage(holder.iv,
                    dataList.get(position).imagePath);
        }

        return convertView;
    }

}
