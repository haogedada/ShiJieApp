package com.shijie.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hjm.bottomtabbar.BottomTabBar;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.params.ProgressParams;
import com.shijie.R;
import com.shijie.utils.ActivityUtils;
import com.shijie.wedget.NetworkStateView;
import com.shijie.wedget.fragment.FourFragment;
import com.shijie.wedget.fragment.OneFragment;
import com.shijie.wedget.fragment.ThreeFragment;
import com.shijie.wedget.fragment.TwoFragment;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;


/**
 * 基类
 * 公共Activity所有activity都继承于此BaseActivity，不继承原生
 *
 * @param <P>
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView, NetworkStateView.OnRefreshListener {
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
        ActivityUtils.addActivity(this);
        setContentView(getLayoutId());
        presenter = createPresenter();
        initReceiver();
        initView();
        initData();
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
     * @param isShowTabBar
     */
    public void setIsBottomTabBar(boolean isShowTabBar){
        if(isShowTabBar){
            loadBottomBar();
        }
    }
    /**
     * Fragment中设置是否显示BottomBar，
     * @param isShow
     */
    public void setShowTabBar(boolean isShow){
        if (isShow){
            mBottomBar.getTabBar().setVisibility(View.VISIBLE);
        }else {
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
    private void loadBottomBar(){
        mBottomBar = findViewById(R.id.bottom_bar);
        mBottomBar.init(getSupportFragmentManager(), 750.0, 1334.0)
//                .setImgSize(50, 50)
//                .setFontSize(28)
                .setTabPadding(10, 6, 80)
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
                        if (position == 1)
                            mBottomBar.setSpot(1, false);
                        if (position == 2)
                            mBottomBar.setSpot(2, false);
                        if (position == 3)
                            mBottomBar.setSpot(3, false);
                    }
                })
                .setSpot(1, true)
                .setSpot(2, true)
                .setSpot(3, true);
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
     * 全局简单处理权限申请,复杂的使用 Permission对象
     */
    public void permissionCheck(int code, String permission) {
        PermissionListener listener = new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, List<String> grantedPermissions) {
                // 权限申请成功回调。
                // 这里的requestCode就是申请时设置的requestCode。
                // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
                if (requestCode == code) {
                    //TODO
                }
            }
            @Override
            public void onFailed(int requestCode, List<String> deniedPermissions) {
                // 权限申请失败回调。
                if (requestCode == code) {
                    return;
                }
            }
        };
        AndPermission.with(this)
                .requestCode(code)
                .permission(permission)
                .rationale((requestCode, rationale) ->
                        AndPermission.rationaleDialog(getApplicationContext(), rationale).show()
                )
                .callback(listener)
                .start();
    }

    /**
     * toast的简写
     * @param s
     */
    public void showToast(String s) {

        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        }
        toast.show();
    }

    @Override
    public void showError(String msg) {
        showToast(msg);
    }

    @Override
    public void onErrorCode(BaseModel model) {
       presenter.onErrorCode(model);
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
               // progressDialog.showTipsDialog("tiaos", "11", new String[]{"1", "2"});
            }

            @Override
            public void NetwordTips() {

            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    /**
     * 显示加载中的布局
     */
    public void showLoadingView() {
        networkStateView.showLoading();
    }
    public void showLoading(){
        dialogFragment = new CircleDialog.Builder()
                .setProgressText("加载中...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
//                        .setProgressDrawable(R.drawable.bg_progress_s)
                .show(getSupportFragmentManager());
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialogFragment.dismiss();
//                    }
//                }, 3000);

    }
    /**
     * 显示加载完成后的布局(即子类Activity的布局)
     */
    public void showContentView() {
        networkStateView.showSuccess();
    }

    /**
     * 显示没有网络的布局
     */
    public void showNoNetworkView() {
        networkStateView.showNoNetwork();
        networkStateView.setOnRefreshListener(this);
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
    public void onNetworkViewRefresh() {
        Log.e("调试", "onNetworkViewRefresh: 正在重新请求网络");
    }


}
