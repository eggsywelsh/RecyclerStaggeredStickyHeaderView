package com.eggsy.recyclerstaggeredstickyheader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.eggsy.recyclerstaggeredstickyheader.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by eggsy on 17-1-6.
 */

public class LocalPicturesContentHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_local_picture)
    ImageView mImageView;

    public LocalPicturesContentHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

}
