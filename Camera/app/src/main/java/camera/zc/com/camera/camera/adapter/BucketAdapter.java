package camera.zc.com.camera.camera.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.ImageLoaderUtil;
import com.pajk.hm.sdk.android.R;
import com.pingan.papd.camera.album.ImageBucket;

import java.util.List;

public class BucketAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageBucket> dataList;
    private int selectOptions;
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

    class ViewHolder {
        ImageView mImg;
        ImageView mSelectBucket;
        TextView mName;
        TextView mCount;
    }

    public void setSelectOptions(int selectOptions){
        this.selectOptions = selectOptions;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_image_bucket,
                    null);
            holder.mImg = (ImageView) convertView.findViewById(R.id.image);
            holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.mCount = (TextView) convertView.findViewById(R.id.count);
            holder.mSelectBucket = (ImageView) convertView.findViewById(R.id.iv_select_bucket);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if (!TextUtils.isEmpty(dataList.get(position).bucketName)) {
            holder.mName.setText(dataList.get(position).bucketName);
        }
        if (null != dataList.get(position).imageList) {
            if (!TextUtils
                    .isEmpty(dataList.get(position).imageList.get(0).thumbnailPath)) {
                ImageLoaderUtil.loadLocalImage(holder.mImg,
                        dataList.get(position).imageList.get(0).thumbnailPath);
            } else {
                ImageLoaderUtil.loadLocalImage(holder.mImg,
                        dataList.get(position).imageList.get(0).imagePath);
            }
        }
        if (position == selectOptions){
            holder.mSelectBucket.setVisibility(View.VISIBLE);
        }else{
            holder.mSelectBucket.setVisibility(View.GONE);
        }

        holder.mCount.setText(String.valueOf(dataList.get(position).count)+"张");

        return convertView;
    }
}
