package com.eggsy.recyclerstaggeredstickyheader;

import com.eggsy.recyclerstaggeredstickyheader.bean.LocalPictureDateResult;
import com.eggsy.recyclerstaggeredstickyheader.bean.WrapLocalPictureDetailInfo;
import com.eggsy.recyclerstaggeredstickyheader.util.Maths;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by eggsy on 17-1-1.
 */

public class MainPresenter extends MainContract.Presenter<MainContract.View> {

    MainModel mMainModel;

    /**
     * pictures collection,include title and picture details
     */
    List<WrapLocalPictureDetailInfo> mLocalPictureInfos;

    MainActivity mMainActivity;

    /**
     * save the positions of title in the data collections
     */
    int[] arrayTitlePos;

    public MainPresenter(MainActivity context) {
        this.mMainActivity = context;
        this.mMainModel = new MainModel(mMainActivity);
        mLocalPictureInfos = new ArrayList<>();
        attachView(context);
    }

    public List<WrapLocalPictureDetailInfo> getLocalPictureDatas() {
        return mLocalPictureInfos;
    }

    /**
     * load local pictures
     */
    public void loadLocalPicture() {
        // you can use rxjava and rxAndroid here
        new Thread(new Runnable() {
            @Override
            public void run() {
                final LocalPictureDateResult scanLocalDataResult = mMainModel.loadAllLocalPictures();
                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleLoadlocalPictureResult(scanLocalDataResult);
                    }
                });
            }
        }).start();
    }

    /**
     * merge title and image content details into one list collection
     *
     * @param scanLocalDataResult
     */
    protected void handleLoadlocalPictureResult(LocalPictureDateResult scanLocalDataResult) {

        if (scanLocalDataResult != null && scanLocalDataResult.getAddDatePictureMap() != null
                && scanLocalDataResult.getAddDatePictureMap().size() > 0) {
            List<WrapLocalPictureDetailInfo> paths = new ArrayList<>();
            arrayTitlePos = new int[scanLocalDataResult.getAddDatePictureMap().keySet().size()];
            int titlePositionIndex = 0;
            for (Map.Entry<String, ArrayList<WrapLocalPictureDetailInfo>> entry : scanLocalDataResult.getAddDatePictureMap().entrySet()) {
                if (entry.getValue() != null && entry.getValue().size() > 0) {
                    // add title position in the data collection
                    arrayTitlePos[titlePositionIndex] = paths.size();
                    titlePositionIndex++;
                    paths.addAll(entry.getValue());
                }
            }
            mLocalPictureInfos.clear();
            mLocalPictureInfos.addAll(paths);
        }
        // notify activity refresh the adapter
        mViewProxy.refreshLocalPictures();
    }

    /**
     * use the binary search method find the closest title position before the current position
     *
     * @param coordinatePosition
     * @return
     */
    public int findBeforeTitlePosition(int coordinatePosition) {
        return Maths.findRecentMinValue(arrayTitlePos, coordinatePosition);
    }

    /**
     * use the binary search method finds the closest title position after the current position
     *
     * @param coordinatePosition
     * @return
     */
    public int findAfterTitlePosition(int coordinatePosition) {
        return Maths.findRecentMaxValue(arrayTitlePos, coordinatePosition);
    }

}
