package com.eggsy.recyclerstaggeredstickyheader;

import com.eggsy.recyclerstaggeredstickyheader.bean.LocalPictureDateResult;
import com.eggsy.recyclerstaggeredstickyheader.bean.WrapLocalPictureDetailInfo;
import com.eggsy.recyclerstaggeredstickyheader.util.Maths;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eggsy on 17-1-1.
 */

public class MainPresenter extends MainContract.Presenter<MainContract.View> {

    MainModel mMainModel;

    LocalPictureDateResult scanLocalDataResult;

    MainActivity mMainActivity;

    public MainPresenter(MainActivity context) {
        this.mMainActivity = context;
        this.mMainModel = new MainModel(mMainActivity);
        attachView(context);
    }

    public List<WrapLocalPictureDetailInfo> getLocalPictureDatas() {
        return scanLocalDataResult != null ? scanLocalDataResult.getLocalPictureInfos() : new ArrayList<WrapLocalPictureDetailInfo>();
    }

    /**
     * load local pictures
     */
    public void loadLocalPicture() {
        // you can use rxjava and rxAndroid here
        new Thread(new Runnable() {
            @Override
            public void run() {
                scanLocalDataResult = mMainModel.loadAllLocalPictures();
                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        handleLoadlocalPictureResult(scanLocalDataResult);
                        mViewProxy.refreshLocalPictures(scanLocalDataResult);
                    }
                });
            }
        }).start();
    }

    /**
     * use the binary search method find the closest title position before the current position
     *
     * @param coordinatePosition
     * @return
     */
    public int findBeforeTitlePosition(int coordinatePosition) {
        return scanLocalDataResult != null ? Maths.findRecentMinValue(scanLocalDataResult.getArrayTitlePos(), coordinatePosition) : Integer.MIN_VALUE;
    }

    /**
     * use the binary search method finds the closest title position after the current position
     *
     * @param coordinatePosition
     * @return
     */
    public int findAfterTitlePosition(int coordinatePosition) {
        return scanLocalDataResult != null ? Maths.findRecentMaxValue(scanLocalDataResult.getArrayTitlePos(), coordinatePosition) : Integer.MIN_VALUE;
    }

}
