package com.eggsy.recyclerstaggeredstickyheader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.eggsy.recyclerstaggeredstickyheader.bean.LocalPictureDateResult;
import com.eggsy.recyclerstaggeredstickyheader.bean.LocalPictureDetailInfo;


/**
 * Created by eggsy on 16-12-13.
 */

public class MainModel {

    private static final String TAG = MainModel.class.getSimpleName();

    Context mContext;

    public MainModel(Context context) {
        this.mContext = context;
    }

    public LocalPictureDateResult loadAllLocalPictures() {
        LocalPictureDateResult result = new LocalPictureDateResult(mContext);

        try{
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = mContext.getContentResolver();

            Log.i(TAG, mImageUri.getPath());

            // query only jpeg and png image type files
            Cursor mCursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    //retrive image path
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    int width = mCursor.getInt(mCursor
                            .getColumnIndex(MediaStore.Images.Media.WIDTH));

                    int height = mCursor.getInt(mCursor
                            .getColumnIndex(MediaStore.Images.Media.HEIGHT));

                    long modifiedData = mCursor.getLong(mCursor.
                            getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));

                    long addedData = mCursor.getLong(mCursor.
                            getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

                    if (path==null || "".equals(path) || width == 0 || height == 0 || modifiedData == 0) {
                        continue;
                    }

                    LocalPictureDetailInfo lpi = new LocalPictureDetailInfo(path, width, height, addedData * 1000, modifiedData * 1000);

                    result.add(lpi);
                }

                mCursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
