package camera.zc.com.camera.camera.album;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.pajk.hm.sdk.android.R;
import com.pingan.activity.BaseActivity;
import com.pingan.papd.camera.CameraManager;
import com.pingan.papd.camera.adapter.BucketAdapter;
import com.pingan.papd.camera.adapter.BucketItemAdapter;

import org.akita.util.MessageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SelectImageActivity extends BaseActivity {
    protected static final String TAG = SelectImageActivity.class
            .getSimpleName();


    public static final String INTENT_ACTION = "BUCKET_ID";

    private GridView gv_bucket_item;

    private ListView lv_photo_album;

    private TextView tv_select_bucket;

    private BucketItemAdapter mBucketItemAdapter;

    private BucketAdapter mBucketAdapter;

    private AlbumHelper helper;

    private List<ImageItem> selectData = new ArrayList<ImageItem>();

    private List<ImageItem> adapterData;

    private List<ImageBucket> dataList;

    private int maxSelect = 0;

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        if (getIntent() != null) {
            maxSelect = getIntent().getIntExtra(
                    CameraManager.MAX_SELECT_ACTION, 0);
        }
        setTitle("相机胶卷");
        showBackView();
        setRightText("完成", selectOK, Color.parseColor("#4FC1E9"));
        findView();
        initData();
        initBucket();
    }

    private void findView() {
        gv_bucket_item = (GridView) findViewById(R.id.gv_bucket_item);
        gv_bucket_item.setOnScrollListener(new PauseOnScrollListener(
                ImageLoader.getInstance(), true, true));
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

                    if (selectData.size() >= maxSelect)
                        MessageUtil.showShortToast(SelectImageActivity.this, "最多选择" + maxSelect + "图片");
                    else {
                        viewById.setVisibility(View.VISIBLE);
                        mBucketItemAdapter.getItem(position).isSelected = true;
                        selectData.add(mBucketItemAdapter.getItem(position));
                     }
                }
            }
        });
        findViewById(R.id.ll_select_bucket).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPop();
                    }
                });
        tv_select_bucket = (TextView) findViewById(R.id.tv_select_bucket);
    }

    OnClickListener selectOK = new OnClickListener() {

        @Override
        public void onClick(View v) {
            getListImagePath();
        }

    };

    private void initBucket() {
        if (null == mPopupWindow) {
            View view = getLayoutInflater().inflate(R.layout.pop_list_bucket,
                    null);
            mPopupWindow = PopUtil.CreatePop(view, LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT, true);
            lv_photo_album = (ListView) view.findViewById(R.id.lv_photo_album);
            mBucketAdapter = new BucketAdapter(this, dataList);
            mBucketAdapter.setSelectOptions(0);
            lv_photo_album.setAdapter(mBucketAdapter);
            lv_photo_album.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    showBucketImg(position);
                    mBucketAdapter.setSelectOptions(position);
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
        tv_select_bucket.setText(imageBucket.bucketName);
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
        helper.init(getApplicationContext());
        dataList = helper.getImagesBucketList(true);
        for (ImageBucket imageBucket : dataList) {
           Collections.sort(imageBucket.imageList, new Comparator<ImageItem>() {
               @Override
               public int compare(ImageItem lhs, ImageItem rhs) {
                   if (lhs.imageCreateTime > rhs.imageCreateTime)
                       return -1;
                   else if (lhs.imageCreateTime == rhs.imageCreateTime)
                       return 0;
                   else
                       return 1;
               }
           });
        }
        adapterData = new ArrayList<ImageItem>();
        adapterData.addAll(dataList.get(0).imageList);
        mBucketItemAdapter = new BucketItemAdapter(this, adapterData);
        gv_bucket_item.setAdapter(mBucketItemAdapter);
    }

    private void getListImagePath() {
        Intent intent = getIntent();
        intent.putParcelableArrayListExtra(CameraManager.MAX_SELECT_ACTION, (ArrayList<? extends Parcelable>) selectData);
        setResult(RESULT_OK, intent);
        this.finish();
    }

}
