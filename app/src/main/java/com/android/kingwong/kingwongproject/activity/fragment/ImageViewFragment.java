package com.android.kingwong.kingwongproject.activity.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.android.kingwong.appframework.Activity.BaseFragment;
import com.android.kingwong.kingwongproject.R;

import butterknife.BindView;

/**
 * Created by KingWong on 2017/10/19.
 *
 */

public class ImageViewFragment extends BaseFragment {

    @BindView(R.id.imgView)
    ImageView imgView;

    private String type = "";

    @Override
    public int getContentViewId() {
        return R.layout.fragment_imageview;
    }

    @Override
    public void OnCreate(Bundle savedInstanceState) {
        initData();
        initView();
    }

    @Override
    public void loadData() {}

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    private void initView() {}

    private void initData() {

    }
}
