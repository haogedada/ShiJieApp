package com.shijie.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shijie.entity.ResponestBean.HomePageBean;

import java.util.List;

/**
 * Created by haoge on 2018/8/23.
 */

public class HomePageAdapter extends BaseSectionQuickAdapter<HomePageBean, BaseViewHolder> {

    public HomePageAdapter(int layoutResId, List data){
        super(layoutResId,data);

    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageBean item) {

    }

    @Override
    protected void convertHead(BaseViewHolder helper, HomePageBean item) {

    }


}
