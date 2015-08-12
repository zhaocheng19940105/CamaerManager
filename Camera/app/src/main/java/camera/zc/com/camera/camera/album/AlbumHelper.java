package camera.zc.com.camera.camera.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.pajk.hm.sdk.android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * Select multiple pictures pictures selection mechanismalbum thumbnails album
 * picture album number each album of pictures
 * 
 * @author zhaocheng
 * 
 */
public class AlbumHelper {
    final String TAG = getClass().getSimpleName();
    final String ALL_KEY = "ALL_PHOTO";
    Context context;
    ContentResolver cr;
    /**
     * The thumbnail list
     */
    HashMap<String, String> thumbnailList = new HashMap<String, String>();

    /**
     * album list
     */
    List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();

    private static AlbumHelper instance;

    private AlbumHelper() {
    }

    public static AlbumHelper getHelper() {
        if (instance == null) {
            instance = new AlbumHelper();
        }
        return instance;
    }

    /**
     * init
     * 
     * @param context
     */
    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
        }
    }

    /**
     * get Thumbnail
     */
    private void getThumbnail() {
        String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
                Thumbnails.DATA};
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
                null, null, null);
        getThumbnailColumnData(cursor);
    }

    /**
     * Thumbnail images from the database
     * 
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int image_id;
            String image_path;
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            do {
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                // Do something with the values.
                // Log.i(TAG, _id + " image_id:" + image_id + " path:"
                // + image_path + "---");
                // HashMap<String, String> hash = new HashMap<String, String>();
                // hash.put("image_id", image_id + "");
                // hash.put("path", image_path);
                // thumbnailList.add(hash);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    /**
     * Get the original image
     */
    void getAlbum() {
        String[] projection = { Albums._ID, Albums.ALBUM, Albums.ALBUM_ART,
                Albums.ALBUM_KEY, Albums.ARTIST, Albums.NUMBER_OF_SONGS };
        Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null,
                null, null);
        getAlbumColumnData(cursor);

    }

    /**
     * The original image from the local database
     * 
     * @param cur
     */
    private void getAlbumColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            String album;
            String albumArt;
            String albumKey;
            String artist;
            int numOfSongs;

            int _idColumn = cur.getColumnIndex(Albums._ID);
            int albumColumn = cur.getColumnIndex(Albums.ALBUM);
            int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
            int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
            int artistColumn = cur.getColumnIndex(Albums.ARTIST);
            int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                album = cur.getString(albumColumn);
                albumArt = cur.getString(albumArtColumn);
                albumKey = cur.getString(albumKeyColumn);
                artist = cur.getString(artistColumn);
                numOfSongs = cur.getInt(numOfSongsColumn);

                // Do something with the values.
//                Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt
//                        + "albumKey: " + albumKey + " artist: " + artist
//                        + " numOfSongs: " + numOfSongs + "---");
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("_id", _id + "");
                hash.put("album", album);
                hash.put("albumArt", albumArt);
                hash.put("albumKey", albumKey);
                hash.put("artist", artist);
                hash.put("numOfSongs", numOfSongs + "");
                albumList.add(hash);

            } while (cur.moveToNext());

        }
    }

    /**
     * Whether to create a photo collections
     */
    boolean hasBuildImagesBucketList = false;

    /**
     * Get photo collections
     */
    void buildImagesBucketList() {
        long startTime = System.currentTimeMillis();

        getThumbnail();

        String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
                Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
                Media.SIZE, Media.BUCKET_DISPLAY_NAME, Media.DATE_TAKEN };
        // Get a cursor
        Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
                Media.DATE_TAKEN+" DESC");
        ImageBucket allPhoto = new ImageBucket();
        allPhoto.count = cur.getCount();
        allPhoto.bucketName = context.getResources().getString(R.string.all_photo);
        allPhoto.imageList = new ArrayList<ImageItem>();
        if (cur.moveToFirst()) {
            // Obtain the specified column index
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cur
                    .getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
            int photoDateIndex = cur.getColumnIndex(Media.DATE_TAKEN);
            do {
                String _id = cur.getString(photoIDIndex);
                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
                String picasaId = cur.getString(picasaIdIndex);
                long time = cur.getLong(photoDateIndex);
//                Log.i(TAG, _id + ", bucketId: " + bucketId + ", picasaId: "
//                        + picasaId + " name:" + name + " path:" + path
//                        + " title: " + title + " size: " + size + " bucket: "
//                        + bucketName + "---"+"time---->"+time);

                ImageBucket bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new ImageBucket();
                    bucketList.put(bucketId, bucket);
                    bucket.imageList = new ArrayList<ImageItem>();
                    bucket.bucketName = bucketName;
                }
                bucket.count++;
                ImageItem imageItem = new ImageItem();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageItem.imageCreateTime=time;
                imageItem.thumbnailPath = thumbnailList.get(_id);
                bucket.imageList.add(imageItem);
                allPhoto.imageList.add(imageItem);
            } while (cur.moveToNext());
        }

        bucketList.put(ALL_KEY, allPhoto);
        Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet()
                .iterator();
        while (itr.hasNext()) {
            Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) itr
                    .next();
            ImageBucket bucket = entry.getValue();
//            Log.d(TAG, entry.getKey() + ", " + bucket.bucketName + ", "
//                    + bucket.count + " ---------- ");
            for (int i = 0; i < bucket.imageList.size(); ++i) {
                ImageItem image = bucket.imageList.get(i);
//                Log.d(TAG, "----- " + image.imageId + ", " + image.imagePath
//                        + ", " + image.thumbnailPath);
            }
        }
        hasBuildImagesBucketList = true;
        long endTime = System.currentTimeMillis();
//        Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
    }

    public List<ImageBucket> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            bucketList.clear();
            buildImagesBucketList();
        }
        List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
        Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet()
                .iterator();
        while (itr.hasNext()) {
            Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) itr
                    .next();
            if (ALL_KEY.equals(entry.getKey()))
                tmpList.add(0, entry.getValue());
            else
                tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    String getOriginalImagePath(String image_id) {
        String path = null;
//        Log.i(TAG, "---(^o^)----" + image_id);
        String[] projection = { Media._ID, Media.DATA };
        Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection,
                Media._ID + "=" + image_id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(Media.DATA));

        }
        return path;
    }

}
