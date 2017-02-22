package com.eggsy.recyclerstaggeredstickyheader;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eggsy.recyclerstaggeredstickyheader.adapter.LocalPicturesAdapter;
import com.eggsy.recyclerstaggeredstickyheader.bean.WrapLocalPictureDetailInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.eggsy.recyclerstaggeredstickyheader.bean.WrapLocalPictureDetailInfo.DATA_TYPE_CONTENT;
import static com.eggsy.recyclerstaggeredstickyheader.bean.WrapLocalPictureDetailInfo.DATA_TYPE_TITLE;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        LocalPicturesAdapter.OnRecyclerViewItemClickListener, MainContract.View {

    @BindView(R.id.srl_root)
    SwipeRefreshLayout mRootLayout;

    @BindView(R.id.rv_content)
    RecyclerView mContentView;

    @BindView(R.id.header_one)
    LinearLayout mHeaderOneView;

    LocalPicturesAdapter mAdapter;

    MainPresenter mMainPresenter;

    int mColumns = 3;

    private StaggeredGridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            onPrepareData();
            onPrepareView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * prepare the datas which this activity needed
     *
     * @throws Exception
     */
    public void onPrepareData() throws Exception {
        mMainPresenter = new MainPresenter(this);
    }

    /**
     * prepare the views instance which this activity needed
     *
     * @throws Exception
     */
    public void onPrepareView() throws Exception {
        ButterKnife.bind(this);

        mRootLayout.setRefreshing(true);
        mRootLayout.setOnRefreshListener(this);

        mLayoutManager = new StaggeredGridLayoutManager(mColumns, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new LocalPicturesAdapter(this, mMainPresenter.getLocalPictureDatas(), mColumns);
        mAdapter.setOnItemClickListener(this);


        mContentView.setLayoutManager(mLayoutManager);
        mContentView.setAdapter(mAdapter);
        mContentView.setHasFixedSize(true);
        // set the scroll listener at RecyclerView
        mContentView.addOnScrollListener(onScrollListener);

        // begin load the local picture
        mMainPresenter.loadLocalPicture();
    }

    /**
     * LocalPicturesAdapter item click callback
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        if (mMainPresenter.getLocalPictureDatas() != null && mMainPresenter.getLocalPictureDatas().size() > position && position >= 0) {
            WrapLocalPictureDetailInfo wpdi = mMainPresenter.getLocalPictureDatas().get(position);
            if (wpdi != null && wpdi.getDataType() == DATA_TYPE_CONTENT) {
                if (wpdi.getPath() != null && !"".equals(wpdi.getPath())) {
                    // print the view position and image local path
                    Toast.makeText(MainActivity.this, position + " , " + wpdi.getPath(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * SwipeRefreshLayout pull down refresh callback
     */
    @Override
    public void onRefresh() {
        // refresh the local pictures
        mMainPresenter.loadLocalPicture();
    }

    @Override
    public void refreshLocalPictures() {
        mAdapter.notifyDataSetChanged();
        mRootLayout.setRefreshing(false);
    }

    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        // get the recyclerview first visable item's position,return the size same as span count
        int mFirstVisiblePosition[] = new int[mColumns];
        // get the recyclerview last visable item's position,return the size same as span count
        int mLastVisiblePosition[] = new int[mColumns];

        // sticky head view height
        private int mStickyHeadHeight = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy == 0) {
                // the first time show the recyclerview items
                mLayoutManager.findFirstVisibleItemPositions(mFirstVisiblePosition);
                int titlePosision = getMinVisablePosition(mFirstVisiblePosition);
                if (titlePosision >= 0) {
                    // get the item object
                    WrapLocalPictureDetailInfo wrapLocalPictureDetailInfo = mAdapter.getItemObject(titlePosision);

                    if (wrapLocalPictureDetailInfo != null) {
                        // set the first title view text content
                        ((TextView) mHeaderOneView.getChildAt(0)).setText(wrapLocalPictureDetailInfo.getDataTitle());
                        mHeaderOneView.setVisibility(View.VISIBLE);
                        // set the current title position into view tag object
                        mHeaderOneView.setTag(titlePosision);
                        // retrive the title view height,and set into variable
                        mStickyHeadHeight = mHeaderOneView.getMeasuredHeight();
                    }
                }
            } else if (dy != 0) {  //  pull down the recyclerview then dy<0 , pull up the recyclerview then dy>0

                mLayoutManager.findFirstVisibleItemPositions(mFirstVisiblePosition);
                int minVisablePosition = getMinVisablePosition(mFirstVisiblePosition);

                mLayoutManager.findLastVisibleItemPositions(mLastVisiblePosition);
                int maxVisablePosition = getMaxVisablePosition(mLastVisiblePosition);

                if (minVisablePosition < 0) {
                    return;
                }
                /**
                 * get one title position before current minimum visable item position
                 */
                int beforeFirstItemTitlePosition = mMainPresenter.findBeforeTitlePosition(minVisablePosition);
                /**
                 * get the title position after current minimum visable item position
                 */
                int afterFirstItemTitlePosition = mMainPresenter.findAfterTitlePosition(minVisablePosition);

                // when next title item position after the current minimum visable exist,and not equals current item position
                if (afterFirstItemTitlePosition != Integer.MIN_VALUE && afterFirstItemTitlePosition != minVisablePosition) {
                    // determine whether next title item after the current minimum visable position is visable in the recyclerview
                    if (afterFirstItemTitlePosition <= maxVisablePosition) {
                        // it means that next title item is visable in the recycleview now
                        // find that suitable next title view
                        View nextTitleView = findView(afterFirstItemTitlePosition);
                        if (nextTitleView != null) {
                            float yxis = nextTitleView.getY();
                            // if next title view is scroll into first title view's area
                            if (yxis <= mStickyHeadHeight) {
                                // then fix the first title scroll y
                                mHeaderOneView.scrollTo(0, (int) (mStickyHeadHeight - yxis));
                            } else {
                                // others,next title away from the first title view
                                // then fix first title view scroll y
                                mHeaderOneView.scrollTo(0, 0);
                            }
                            // set visable to the header view always
                            mHeaderOneView.setVisibility(View.VISIBLE);
                        }
                    }
                }


                /**
                 * Determine whether need to change the title
                 */
                // when title item before the current maxinum visable position exist,and position not equals the current header view's tag
                if (beforeFirstItemTitlePosition != Integer.MIN_VALUE && mHeaderOneView.getTag() != null && (int) mHeaderOneView.getTag() != beforeFirstItemTitlePosition) {
                    /**
                     * it means that should change the title content
                     */
                    ((TextView) mHeaderOneView.getChildAt(0)).setText("");
                    // always show the title item that before the current maxinum visable position
                    WrapLocalPictureDetailInfo wpdi = mMainPresenter.getLocalPictureDatas().get(beforeFirstItemTitlePosition);
                    // set new title content
                    ((TextView) mHeaderOneView.getChildAt(0)).setText(wpdi.getDataTitle());
                    if (dy > 0) {
                        // if user pull up the recyclerview,fix the scroll y.Sometimes,because of the next title item may be not visable in current recyclerview,
                        // so,we should force set header view visable once again
                        mHeaderOneView.setVisibility(View.VISIBLE);
                        mHeaderOneView.scrollTo(0, 0);
                    }
                    // set title position in the tag
                    mHeaderOneView.setTag(beforeFirstItemTitlePosition);
                    // retrive the sticky head height
                    mStickyHeadHeight = mHeaderOneView.getMeasuredHeight();
                }
            }
        }

        /**
         * find the title views in the current visable recyclerview
         *
         * @param firstPosition
         * @param lastPosition
         * @return
         */
        public ArrayList<View> findTitleViews(int firstPosition, int lastPosition) {
            ArrayList<View> views = new ArrayList<>();
            for (int i = firstPosition; i <= lastPosition; i++) {
                if (mAdapter.getItemViewType(i) == DATA_TYPE_TITLE) {
                    views.add(mLayoutManager.findViewByPosition(i));
                    break;
                }
            }
            return views;
        }

        /**
         * retrive the title view by position
         *
         * @param position
         * @return
         */
        public View findView(int position) {
            return mLayoutManager.findViewByPosition(position);
        }

        /**
         * get mininum position in int array
         *
         * @param firstVisiblePosition
         * @return
         */
        public int getMinVisablePosition(int[] firstVisiblePosition) {
            int minValue = Integer.MAX_VALUE;
            if (firstVisiblePosition != null) {
                for (int i = 0; i < firstVisiblePosition.length; i++) {
                    minValue = firstVisiblePosition[i] < minValue ? firstVisiblePosition[i] : minValue;
                }
            }
            return minValue;
        }

        /**
         * get maxinum visable position in int array
         *
         * @param lastVisiblePosition
         * @return
         */
        public int getMaxVisablePosition(int[] lastVisiblePosition) {
            int maxValue = Integer.MIN_VALUE;
            if (lastVisiblePosition != null) {
                for (int i = 0; i < lastVisiblePosition.length; i++) {
                    maxValue = lastVisiblePosition[i] > maxValue ? lastVisiblePosition[i] : maxValue;
                }
            }
            return maxValue;
        }
    };
}
