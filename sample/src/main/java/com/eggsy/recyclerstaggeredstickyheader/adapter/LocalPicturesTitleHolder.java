package com.eggsy.recyclerstaggeredstickyheader.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eggsy.recyclerstaggeredstickyheader.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by eggsy on 17-1-6.
 */

public class LocalPicturesTitleHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    public LocalPicturesTitleHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

}
