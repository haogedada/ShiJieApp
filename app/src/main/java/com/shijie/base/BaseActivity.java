package com.shijie.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hjm.bottomtabbar.BottomTabBar;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.params.ProgressParams;
import com.shijie.R;
import com.shijie.ui.activity.IntroActivity;
import com.shijie.ui.fragment.mainFragment.FourFragment;
import com.shijie.ui.fragment.mainFragment.OneFragment;
import com.shijie.ui.fragment.mainFragment.ThreeFragment;
import com.shijie.ui.fragment.mainFragment.TwoFragment;
import com.shijie.utils.ActivityUtils;
import com.shijie.utils.PermissionHelper;
import com.shijie.utils.SharedPreferencesHelper;
import com.shijie.wedget.NetworkStateView;


/**
 * 基类
 * 公共Activity所有activity都继承于此BaseActivity，不继承原生
 *
 * @param <P>
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements  NetworkStateView.OnRefreshListener,BaseView {
    public Context context;
    public Toast toast;
    protected P presenter;
    private NetworkStateView networkStateView;
    private NetworkReceiver networkReceiver;
    private BottomTabBar mBottomBar;
    private DialogFragment dialogFragment;

    protected abstract P createPresenter();

    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化视图界面
     */
    protected abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        fristStartUp();
        ActivityUtils.addActivity(this);
        setContentView(getLayoutId());
        presenter = createPresenter();
        initReceiver();
        initData();
        initView();

    }

    /**
     * 第一次启动
     */
    private void fristStartUp() {
        boolean isFirst = (boolean) new SharedPreferencesHelper(this, "first_start_up").getSharedPreference("isFirst", true);
        if (isFirst) {
            new SharedPreferencesHelper(this, "first_start_up").put("isFirst", false);
            Intent intent = new Intent(context, IntroActivity.class);
            startActivity(intent);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = getLayoutInflater().inflate(R.layout.activity_base, null);
        //设置填充activity_base布局
        super.setContentView(view);
        //去除标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //去掉信息栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 沉浸效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            view.setFitsSystemWindows(true);
        }
        //加载子类Activity的布局
        initDefaultView(layoutResID);
    }

    /**
     * Activity中设置是否加载BottomBar，要在初始化中设置
     *
     * @param isShowTabBar
     */
    public void setIsBottomTabBar(boolean isShowTabBar) {
        if (isShowTabBar) {
            loadBottomBar();
        }
    }

    /**
     * Fragment中设置是否显示BottomBar，
     *
     * @param isShow
     */
    public void setShowTabBar(boolean isShow) {
        if (isShow) {
            mBottomBar.getTabBar().setVisibility(View.VISIBLE);
        } else {
            mBottomBar.getTabBar().setVisibility(View.GONE);
        }
    }

    /**
     * 初始化默认布局的View
     *
     * @param layoutResId 子View的布局id
     */
    private void initDefaultView(int layoutResId) {
        networkStateView = (NetworkStateView) findViewById(R.id.nsv_state_view);
        FrameLayout container = (FrameLayout) findViewById(R.id.fl_activity_child_container);
        View childView = LayoutInflater.from(this).inflate(layoutResId, null);
        container.addView(childView, 0);
    }

    /**
     * 加载BottomBar
     */
    private void loadBottomBar() {
        mBottomBar = findViewById(R.id.bottom_bar);
        boolean index1= (boolean) new SharedPreferencesHelper(context,"spot_state").getSharedPreference("1",true);
        boolean index2= (boolean) new SharedPreferencesHelper(context,"spot_state").getSharedPreference("2",true);
        boolean index3= (boolean) new SharedPreferencesHelper(context,"spot_state").getSharedPreference("3",true);
        mBottomBar.init(getSupportFragmentManager(), 750.0, 1334.0)
//                .setImgSize(50, 50)
//                .setFontSize(28)
                .setTabPadding(10, 6, 10)
//                .setChangeColor(Color.parseColor("#2784E7"),Color.parseColor("#282828"))
                .addTabItem("首页", R.mipmap.ic_common_tab_index_select, R.mipmap.ic_common_tab_index_unselect, OneFragment.class)
                .addTabItem("热门", R.mipmap.ic_common_tab_hot_select, R.mipmap.ic_common_tab_hot_unselect, TwoFragment.class)
                .addTabItem("朋友", R.mipmap.ic_common_tab_friend_select, R.mipmap.ic_common_tab_friend_unselect, ThreeFragment.class)
                .addTabItem("我的", R.mipmap.ic_common_tab_user_select, R.mipmap.ic_common_tab_user_unselect, FourFragment.class)
                //           .isShowDivider(true)
//                .setDividerColor(Color.parseColor("#373737"))
//                .setTabBarBackgroundColor(Color.parseColor("#FFFFFF"))
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name, View view) {
                        if (position == 1) {
                          //TODO 跳转到activity
                            new SharedPreferencesHelper(context,"spot_state").put("1",false);
                            mBottomBar.setSpot(1, false);
                        }
                        if (position == 2) {
                            //TODO 跳转到activity
                            new SharedPreferencesHelper(context,"spot_state").put("2",false);
                            mBottomBar.setSpot(2, false);
                        }
                        if (position == 3) {
                            //TODO 跳转到activity
                            new SharedPreferencesHelper(context,"spot_state").put("3",false);
                            mBottomBar.setSpot(3, false);
                        }
                    }
                })
                .setSpot(1, index1)
                .setSpot(2, index2)
                .setSpot(3, index3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
        ActivityUtils.removeActivity(this);
    }

    /**
     * 全局处理权限申请,使用默认弹窗，子类还需要重写两个方法
     * permissionSuccess()，permissionFail()
     */
    public void permissionApplication(String[] permissions) {
        new PermissionHelper(this, getSupportFragmentManager()) {
            @Override
            public void success() {
                permissionSuccess();
            }

            @Override
            public void fail() {
                permissionFail();
            }
        }.allUsePermission();
    }

    /**
     * 权限申请成功
     */
    public void permissionSuccess() {

    }

    /**
     * 权限申请失败
     */
    public void permissionFail() {

    }

    /**
     * toast的简写
     *
     * @param s
     */
    public void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        }
        toast.show();
    }

    /**
     * 显示报错信息
     * @param msg
     */
    @Override
    public void showError(String msg) {
        showToast(msg);
    }

    /**
     * 初始化广播
     */
    private void initReceiver() {
        networkReceiver = new NetworkReceiver(this) {
            @Override
            public void noNetworkConnected() {
                //网络未连接
                showNoNetworkView();
            }

            @Override
            public void NetwordTips() {
                //TODO 网络提示

            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    /**
     * 显示加载中dialong
     */
    public void showLoading() {
        dialogFragment = new CircleDialog.Builder()
                .setProgressText("加载中...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(getSupportFragmentManager());
    }

    public void hideLoading() {
        if (dialogFragment.isCancelable()) {
            dialogFragment.dismiss();
        }
    }

    /**
     * 显示没有网络的布局
     */
    public void showNoNetworkView() {
        networkStateView.showNoNetwork();
        networkStateView.setOnRefreshListener(this);
    }

    /**
     * 弹出是否重新发起请求
     * @param errMsg
     */
    @Override
    public void showNetworkError(String errMsg) {
        new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .configDialog(params -> {
                    params.backgroundColor = Color.WHITE;
                    params.backgroundColorPress = Color.GRAY;
                })
                .setTitle("网络异常!!")
                .setTitleColor(Color.RED)
                .setText(errMsg + "!\n" + "您需要尝试重新连接吗？")
                .configText(params -> {
                    params.padding = new int[]{100, 0, 100, 50};
                })
                .setNegative("取消", null)
                .setPositive("确定", v ->
                      //重新发起请求方法
                        reconnectNetwork())
                .configPositive(params -> params.backgroundColorPress = Color.GRAY)
                .show(getSupportFragmentManager());

    }

    /**
     * 显示没有数据的布局
     */
    public void showEmptyView() {
        networkStateView.showEmpty();
        networkStateView.setOnRefreshListener(this);
    }

    /**
     * 显示数据错误，网络错误等布局
     */
    public void showErrorView() {
        networkStateView.showError();
        networkStateView.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        onNetworkViewRefresh();
    }

    /**
     * 重新请求网络
     */
    public abstract void onNetworkViewRefresh();
    /**
     * 重新发起请求接口
     */
    public abstract void reconnectNetwork();
}
