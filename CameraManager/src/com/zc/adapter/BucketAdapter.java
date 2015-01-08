package com.zc.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.cameramanager.R;
import com.zc.photoalbum.ImageBucket;
import com.zc.photoalbum.ImageLoaderUtil;

public class BucketAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageBucket> dataList;

    public BucketAdapter(Context mContext, List<ImageBucket> dataList) {
        super();
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return null != dataList ? dataList.size() : 0;
    }

    @Override
    public ImageBucket getItem(int position) {
        return null != dataList ? dataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHoder {
        ImageView mImg;
        TextView mName;
        TextView mCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder = null;
        if (convertView == null) {
            hoder = new ViewHoder();
            convertView = View.inflate(mContext, R.layout.item_image_bucket,
                    null);
            hoder.mImg = (ImageView) convertView.findViewById(R.id.image);
            hoder.mName = (TextView) convertView.findViewById(R.id.name);
            hoder.mCount = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(hoder);
        } else
            hoder = (ViewHoder) convertView.getTag();
        if (!TextUtils.isEmpty(dataList.get(position).bucketName)) {
            hoder.mName.setText(dataList.get(position).bucketName);
        }
        if (null != dataList.get(position).imageList) {
            if (!TextUtils
                    .isEmpty(dataList.get(position).imageList.get(0).thumbnailPath)) {
                ImageLoaderUtil.loadLoaclImage(hoder.mImg,
                        dataList.get(position).imageList.get(0).thumbnailPath);
            } else {
                ImageLoaderUtil.loadLoaclImage(hoder.mImg,
                        dataList.get(position).imageList.get(0).imagePath);
            }
        }

        hoder.mCount.setText(String.valueOf(dataList.get(position).count));

        return convertView;
    }
}
