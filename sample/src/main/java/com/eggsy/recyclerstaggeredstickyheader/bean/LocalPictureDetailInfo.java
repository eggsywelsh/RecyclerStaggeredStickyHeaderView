package com.eggsy.recyclerstaggeredstickyheader.bean;

/**
 * Created by eggsy on 17-1-8.
 */

public class LocalPictureDetailInfo {

    private String path;

    private int srcWidth;

    private int srcHeight;

    private long addedDate;

    private long modifiedDate;

    public LocalPictureDetailInfo(String path, int srcWidth, int srcHeight, long addedDate, long modifiedDate) {
        this.path = path;
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;
        this.addedDate = addedDate;
        this.modifiedDate = modifiedDate;
    }

    public String getPath() {
        return path;
    }

    public int getSrcWidth() {
        return srcWidth;
    }

    public int getSrcHeight() {
        return srcHeight;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }
}
