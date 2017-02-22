package com.eggsy.recyclerstaggeredstickyheader.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.eggsy.recyclerstaggeredstickyheader.R;
import com.eggsy.recyclerstaggeredstickyheader.bean.WrapLocalPictureDetailInfo;

import java.io.File;
import java.util.List;

import static com.eggsy.recyclerstaggeredstickyheader.bean.WrapLocalPictureDetailInfo.DATA_TYPE_CONTENT;
import static com.eggsy.recyclerstaggeredstickyheader.bean.WrapLocalPictureDetailInfo.DATA_TYPE_TITLE;

/**
 * Created by eggsy on 17-1-6.
 */

public class LocalPicturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    Activity mContext;

    List<WrapLocalPictureDetailInfo> mImagePaths;

    int columnNums = 1;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public LocalPicturesAdapter(Activity context, List<WrapLocalPictureDetailInfo> imagePaths, int columnNums) {
        this.mContext = context;
        this.mImagePaths = imagePaths;
        this.columnNums = columnNums;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        if (viewType == DATA_TYPE_TITLE) {
            view = mContext.getLayoutInflater().inflate(R.layout.item_title_local_picture,null);
            viewHolder = new LocalPicturesTitleHolder(view);
        } else if (viewType == DATA_TYPE_CONTENT) {
            view = mContext.getLayoutInflater().inflate(R.layout.item_content_local_picture,null);
            viewHolder = new LocalPicturesContentHolder(view);
        } else {
            view = mContext.getLayoutInflater().inflate(R.layout.item_content_local_picture,null);
            viewHolder = new LocalPicturesContentHolder(view);
        }
        if (view != null) {
            view.setOnClickListener(this);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == DATA_TYPE_TITLE) {
            LocalPicturesTitleHolder holder = (LocalPicturesTitleHolder) viewHolder;
            holder.itemView.setTag(position);
            if (holder.mTvTitle != null) {
                WrapLocalPictureDetailInfo pictureDetailInfo = mImagePaths.get(position);
                StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);
                holder.itemView.setLayoutParams(layoutParams);
                holder.mTvTitle.setText(pictureDetailInfo.getDataTitle());
            }
        } else if (viewHolder.getItemViewType() == DATA_TYPE_CONTENT) {
            LocalPicturesContentHolder holder = (LocalPicturesContentHolder) viewHolder;
            holder.itemView.setTag(position);
            if (holder.mImageView != null) {
                WrapLocalPictureDetailInfo pictureDetailInfo = mImagePaths.get(position);

                // get the current position imageview layout params
                ViewGroup.LayoutParams params = holder.mImageView.getLayoutParams();
                // get screen size
                int[] screenSize = getDiaplsySize(mContext);

                if(params==null){
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                // resize the width and height
                params.width = screenSize[0] / columnNums;
                params.height = params.width * pictureDetailInfo.getSrcHeight() / pictureDetailInfo.getSrcWidth();
                holder.mImageView.setLayoutParams(params);

                // use glide load the image into ImageView
                Glide.with(mContext).load(new File(pictureDetailInfo.getPath())).into(holder.mImageView);
            }
        }
    }

    public int[] getDiaplsySize(Activity context){
        Point point = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(point);
        return new int[]{point.x,point.y};
    }

    @Override
    public int getItemCount() {
        return mImagePaths == null ? 0 : mImagePaths.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mImagePaths.get(position).getDataType();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public WrapLocalPictureDetailInfo getItemObject(int position) {
        if (mImagePaths != null) {
            return mImagePaths.get(position);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            int position = (int) v.getTag();
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position);
            }
        }
    }

    /**
     * internal interface can expose item click for invoked class
     */
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
