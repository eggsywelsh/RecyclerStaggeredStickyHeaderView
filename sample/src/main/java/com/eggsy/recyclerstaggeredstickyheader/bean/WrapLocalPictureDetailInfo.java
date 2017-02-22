package com.eggsy.recyclerstaggeredstickyheader.bean;

import android.support.annotation.NonNull;

/**
 * Created by eggsy on 17-2-15.
 */

public class WrapLocalPictureDetailInfo {

    public static final int DATA_TYPE_TITLE = 1;
    public static final int DATA_TYPE_CONTENT = 2;

    @NonNull
    private int dataType = DATA_TYPE_CONTENT;

    private String dataTitle;

    private LocalPictureDetailInfo localPictureDetailInfo;

    public WrapLocalPictureDetailInfo(LocalPictureDetailInfo localPictureDetailInfo) {
        this(localPictureDetailInfo, "");
    }

    public WrapLocalPictureDetailInfo(LocalPictureDetailInfo localPictureDetailInfo, String dataTitle) {
        this(localPictureDetailInfo, dataTitle, DATA_TYPE_CONTENT);
    }

    public WrapLocalPictureDetailInfo(LocalPictureDetailInfo localPictureDetailInfo, String dataTitle, int dataType) {
        this.localPictureDetailInfo = localPictureDetailInfo;
        this.dataTitle = dataTitle;
        this.dataType = dataType;
    }

    public WrapLocalPictureDetailInfo(String dataTitle) {
        this(dataTitle, DATA_TYPE_CONTENT);
    }

    public WrapLocalPictureDetailInfo(String dataTitle, int dataType) {
        this.dataTitle = dataTitle;
        this.dataType = dataType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public String getPath() {
        return localPictureDetailInfo != null ? localPictureDetailInfo.getPath() : "";
    }

    public int getSrcWidth() {
        return localPictureDetailInfo != null ? localPictureDetailInfo.getSrcWidth() : -1;
    }

    public int getSrcHeight() {
        return localPictureDetailInfo != null ? localPictureDetailInfo.getSrcHeight() : -1;
    }

    public long getAddedDate() {
        return localPictureDetailInfo != null ? localPictureDetailInfo.getAddedDate() : -1;
    }

    public long getModifiedDate() {
        return localPictureDetailInfo != null ? localPictureDetailInfo.getModifiedDate() : -1;
    }


}
