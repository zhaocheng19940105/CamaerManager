package camera.zc.com.camera.camera.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.utils.ImageLoaderUtil;
import com.pajk.hm.sdk.android.R;
import com.pingan.papd.camera.album.ImageItem;

import java.util.List;

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
            WindowManager wm = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = width/4 - mContext.getResources().getDimensionPixelSize(R.dimen.image_height_padding);
            holder.iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
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
            Log.d("zc", "down thumbnailPath");
            ImageLoaderUtil.loadLocalImage(holder.iv,
                    dataList.get(position).thumbnailPath);
        } else {
            Log.d("zc", "down bigPath");
//         Bitmap bm= MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(),Long.parseLong(dataList.get(position).imageId), MediaStore.Images.Thumbnails.MINI_KIND,options);
//         holder.iv.setImageBitmap(bm);
            ImageLoaderUtil.loadLocalImage(holder.iv,
                    dataList.get(position).imagePath);
        }

        return convertView;
    }

}
