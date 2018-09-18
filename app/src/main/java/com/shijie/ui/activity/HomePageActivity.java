package com.shijie.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shijie.R;
import com.shijie.adapter.HomePageAdapter;
import com.shijie.base.BaseActivity;
import com.shijie.entity.ResponestBean.MySection;
import com.shijie.mvp.presenter.HomePagePresenter;
import com.shijie.mvp.view.HomePageView;

import java.util.List;

public class HomePageActivity extends BaseActivity<HomePagePresenter> implements HomePageView {
    private final static String TAG="HomePageActivity";

    private RecyclerView mRecyclerView;
    //private List<MySection> mData;

    @Override
    protected HomePagePresenter createPresenter() {
        return new HomePagePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_page;
    }

    @Override
    protected void initData() {
        presenter.getHomePage(4);
    }

    @Override
    protected void initView() {
        setIsBottomTabBar(true);
        //setShowTabBar(true);
        mRecyclerView = findViewById(R.id.recycler_view);
        //对齐卡片布局
        //  mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        //瀑布式卡片布局
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }


    @Override
    public void showNetworkError(String errMsg) {
        super.showNetworkError(errMsg);
    }



    @Override
    public void reconnectNetwork() {
        initData();
    }

    @Override
    public void showView(List<MySection> mData) {
        HomePageAdapter homePageAdapter = new HomePageAdapter(R.layout.item_homepage, R.layout.def_section_head, mData);
        homePageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MySection mySection = mData.get(position);
                if (mySection.isHeader) {
                    Toast.makeText(HomePageActivity.this, mySection.header, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomePageActivity.this, mySection.t.getVideoTitle(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(HomePageActivity.this, PlayerVideoActivity.class);
                    intent.putExtra("videoId", mySection.t.getVideoId());
                    startActivity(intent);
                }
            }
        });
        homePageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(HomePageActivity.this, "onItemChildClick" + position, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(homePageAdapter);
    }
}
