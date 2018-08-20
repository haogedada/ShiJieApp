package com.shijie.utils;

import android.app.Activity;
import android.widget.Toast;

import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * Created by haoge on 2018/8/17.
 */

public abstract class Permission {
    private Activity context;
    public Permission(Activity context) {
        this.context = context;
    }
    /**
     *
     * 申请权限
     *
     * 次申请时检测到已经申请过一次该权限了,允许开发者弹窗说明申请权限的目的,
     * 获取用户的同意后再申请权限,避免用户勾选不再提示,导致不能再次申请权限。
     * @param code 权限请求码，区分权限请求
     * @param permission 权限名
     * @param setText 设置用户拒绝权限再次请求提示弹窗，该数组四个值依次为
     * 弹窗标题，弹窗详细信息，左弹窗按钮名字，有弹窗按钮名字，
     */
    public void permissionCheck(int code, String permission[],String [] setText){
        RationaleListener rationaleListener=null;
        if (!StrJudgeUtil.isCorrectInt(code)){
            Toast.makeText(context,"permissionCheck出错",Toast.LENGTH_LONG).show();
        }
        if(setText.length!=4&&setText!=null){
            Toast.makeText(context,"permissionCheck setText值出错",Toast.LENGTH_LONG).show();
        }

        PermissionListener listener = new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, List<String> grantedPermissions) {
                // 权限申请成功回调。
                // 这里的requestCode就是申请时设置的requestCode。
                // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
                if(requestCode == code) {
                    success();
                }
            }
            @Override
            public void onFailed(int requestCode, List<String> deniedPermissions) {
                // 权限申请失败回调。
                if(requestCode == code) {
                    fail();
                }
            }
        };
        if(setText==null){
            rationaleListener=(requestCode, rationale) ->
                    AndPermission.rationaleDialog(context, rationale).show();
        }else {
            rationaleListener = (requestCode, rationale) -> {
                AlertDialog.newBuilder(context)
                        .setTitle(setText[0])
                        .setMessage(setText[1])
                        .setPositiveButton(setText[2], (dialog, which) -> {
                            rationale.resume();
                        })
                        .setNegativeButton(setText[3], (dialog, which) -> {
                            rationale.cancel();
                        }).show();
            };
        }
        AndPermission.with(context)
                .requestCode(code)
                .permission(permission)
                .rationale(rationaleListener )
                .callback(listener)
                .start();
    }
       public abstract void success();
       public abstract void fail();
}
