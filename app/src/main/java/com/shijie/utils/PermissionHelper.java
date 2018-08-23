package com.shijie.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.mylhyl.circledialog.CircleDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限处理工具类
 * Created by haoge on 2018/8/17.
 */

public abstract class PermissionHelper {
    private Activity context;
    private FragmentManager fragmentManager;
    public PermissionHelper(Activity context,FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager=fragmentManager;
    }

    /**
     * 用户拒绝授权（也有可能系统阻止授权），再次询问用户是否授权，
     */
    private Rationale mRationale = new Rationale() {
        @Override
        public void showRationale(Context context, Object data, RequestExecutor executor) {
            // 这里使用一个Dialog询问用户是否继续授权。

            ArrayList list = (ArrayList) data;
            List<String> permissionNames = Permission.transformText(context, list);
            String permissionText = TextUtils.join(",\n", permissionNames);
            new CircleDialog.Builder()
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(false)
                    .configDialog(params -> {
                        params.backgroundColor = Color.WHITE;
                        params.backgroundColorPress = Color.GRAY;
                    })
                    .setTitle("权限提示!")
                    .setText("您拒绝了："+permissionText+"权限，将会影响您软件使用体验，您确定是否继续授权？")
                    .configText(params -> {
                        params.padding = new int[]{100, 0, 100, 50};
                    })
                    .setNeutral("取消",v -> {
                        executor.cancel();
                    })
                    .setPositive("确定", v ->{
                        // 如果用户继续：
                        executor.execute();
                    })
                    .configPositive(params -> params.backgroundColorPress = Color.GRAY)
                    .show(fragmentManager);
        }
    };
    /**
     *
     * 自定义权限申请
     *
     * @param permissions 权限名
     */
    public void permissionCheck(String [] permissions) {
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(Object data) {
                        success();
                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(Object data) {
                if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                    // 这些权限被用户总是拒绝。
                    ArrayList list = (ArrayList) data;
                    List<String> permissionNames = Permission.transformText(context, list);
                    String permissionText = TextUtils.join(",\n", permissionNames);
                    // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
                    new CircleDialog.Builder()
                            .setCanceledOnTouchOutside(false)
                            .setCancelable(false)
                            .configDialog(params -> {
                                params.backgroundColor = Color.WHITE;
                                params.backgroundColorPress = Color.GRAY;
                            })
                            .setTitle("权限提示")
                            .setText("设置："+permissionText+"权限出问题了，您是否需要到权限设置里进行授权？")
                            .configText(params -> {
                                params.padding = new int[]{100, 0, 100, 50};
                            })
                            .setNegative("取消",null)
                            .setPositive("确定", v ->{
                                // 如果用户继续：
                                AndPermission.with(context)
                                        .runtime()
                                        .setting()
                                        .onComeback(new Setting.Action() {
                                            @Override
                                            public void onAction() {
                                                // 用户从设置回来了。
                                                success();
                                            }
                                        })
                                        .start();
                            })
                            .configPositive(params -> params.backgroundColorPress = Color.GRAY)
                            .show(fragmentManager);

                }
                fail();
            }
        }).start();
    }

    /**
     * 所有权限申请
     */
    public void allUsePermission(){
        String [] permissions=new String[]{
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.CAMERA,
        };
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(Object data) {
                        success();
                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(Object data) {
                if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                    // 这些权限被用户总是拒绝。
                    ArrayList list = (ArrayList) data;
                    List<String> permissionNames = Permission.transformText(context, list);
                    String permissionText = TextUtils.join(",\n", permissionNames);
                    // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
                    new CircleDialog.Builder()
                            .setCanceledOnTouchOutside(false)
                            .setCancelable(false)
                            .configDialog(params -> {
                                params.backgroundColor = Color.WHITE;
                                params.backgroundColorPress = Color.GRAY;
                            })
                            .setTitle("权限提示")
                            .setText("没有："+permissionText+"权限，您是否需要到权限设置里进行授权？")
                            .configText(params -> {
                                params.padding = new int[]{100, 0, 100, 50};
                            })
                            .setNegative("取消",null)
                            .setPositive("确定", v ->{
                                // 如果用户继续：
                                AndPermission.with(context)
                                        .runtime()
                                        .setting()
                                        .onComeback(new Setting.Action() {
                                            @Override
                                            public void onAction() {
                                                // 用户从设置回来了。
                                                success();
                                            }
                                        })
                                        .start();
                            })
                            .configPositive(params -> params.backgroundColorPress = Color.GRAY)
                            .show(fragmentManager);

                }
                fail();
            }
        }).start();
    }
       public abstract void success();
       public abstract void fail();
}
