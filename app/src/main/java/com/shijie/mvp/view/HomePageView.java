package com.shijie.mvp.view;

import com.shijie.base.BaseView;
import com.shijie.entity.ResponestBean.MySection;

import java.util.List;

/**
 * Created by haoge on 2018/8/23.
 */

public interface HomePageView extends BaseView {
    void showView(List<MySection> list);//显示视图
}
