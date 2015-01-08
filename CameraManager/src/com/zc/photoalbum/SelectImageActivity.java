package com.zc.photoalbum;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zc.adapter.BucketAdapter;
import com.zc.adapter.BucketItemAdapter;
import com.zc.cameramanager.R;

public class SelectImageActivity extends Activity {
    protected static final String TAG = SelectImageActivity.class
            .getSimpleName();

    public static final String INTENT_ACTION = "BUCKET_ID";

    private GridView gv_bucket_item;

    private ListView lv_photo_album;

    private BucketItemAdapter mBucketItemAdapter;

    private BucketAdapter mBucketAdapter;

    private AlbumHelper helper;

    private List<ImageItem> selectData = new ArrayList<ImageItem>();

    private List<ImageItem> adapterData;

    private List<ImageBucket> dataList;

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        findView();
        initData();
        initBucket();
    }

    private void findView() {
        gv_bucket_item = (GridView) findViewById(R.id.gv_bucket_item);
        gv_bucket_item.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                View viewById = view.findViewById(R.id.iv_image_select);
                if (mBucketItemAdapter.getItem(position).isSelected) {
                    viewById.setVisibility(View.GONE);
                    mBucketItemAdapter.getItem(position).isSelected = false;
                    selectData.remove(mBucketItemAdapter.getItem(position));
                } else {
                    viewById.setVisibility(View.VISIBLE);
                    mBucketItemAdapter.getItem(position).isSelected = true;
                    selectData.add(mBucketItemAdapter.getItem(position));
                }
            }
        });
        findViewById(R.id.btn_select_bucket).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showPop();
                    }
                });

        findViewById(R.id.btn_select_ok).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getListImagePath();
                    }

                });
    }

    private void initBucket() {
        if (null == mPopupWindow) {
            View view = getLayoutInflater().inflate(R.layout.pop_list_bucket,
                    null);
            mPopupWindow = PopUtil.CreatePop(view, LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT, true);
            lv_photo_album = (ListView) view.findViewById(R.id.lv_photo_album);
            mBucketAdapter = new BucketAdapter(this, dataList);
            lv_photo_album.setAdapter(mBucketAdapter);
            lv_photo_album.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    showBucketImg(position);
                    dismissPop();
                }
            });
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissPop();
                }
            });
        }

    }

    private void showBucketImg(int position) {
        ImageBucket imageBucket = dataList.get(position);
        adapterData.clear();
        adapterData.addAll(imageBucket.imageList);
        mBucketItemAdapter.notifyDataSetChanged();
    }

    private void dismissPop() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private void showPop() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            dismissPop();
        }
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(findViewById(R.id.main), Gravity.TOP,
                    0, 0);
        }
    }

    private void initData() {
        helper = AlbumHelper.getHelper();
        helper.init(this);
        dataList = helper.getImagesBucketList(false);
        adapterData = new ArrayList<ImageItem>();
        adapterData.addAll(dataList.get(0).imageList);
        mBucketItemAdapter = new BucketItemAdapter(this, adapterData);
        gv_bucket_item.setAdapter(mBucketItemAdapter);
    }

    private void getListImagePath() {
        this.finish();
    }

}
