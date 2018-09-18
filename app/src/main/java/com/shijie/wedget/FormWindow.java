package com.shijie.wedget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mylhyl.circledialog.CircleDialog;
import com.shijie.R;
import com.shijie.adapter.CheckedAdapter;
import com.shijie.utils.SharedPreferencesHelper;
import com.shijie.utils.StrJudgeUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

/**
 * 表单窗口类
 * Created by haoge on 2018/8/19.
 */

public abstract class FormWindow extends Activity implements DatePickerDialog.OnDateSetListener {

    private final int IMAGE_REQUEST_CODE = 100;
    private Context context;
    private DialogFragment dialogFragment;
    private Calendar now;
    private DatePickerDialog dpd;
    private FragmentManager fragmentManager;
    private android.app.FragmentManager fragmentManagerOfApp;
    private Activity activity;

    /**
     * @param context
     * @param fragmentManager
     * @param fragmentManagerOfApp
     */
    public FormWindow(Activity activity, Context context, FragmentManager fragmentManager, android.app.FragmentManager fragmentManagerOfApp) {
        this.context = context;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.fragmentManagerOfApp = fragmentManagerOfApp;
        //初始化日历插件
        now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                FormWindow.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
    }

    /**
     * 一些修改用户信息的弹窗
     *
     * @param index
     */
    public void modifyMsgForm(String userName, int index) {
        if (!StrJudgeUtil.isCorrectStr(userName)) {
            return;
        }
        String rightBtn = "下一步";
        String title = "请完善个人资料";
        String subTitle = null;
        String inputHint = null;
        String key = null;
        switch (index) {
            case 0:
                title = "检测到您是第一次登陆!";
                subTitle = "请完善个人资料\n设置昵称";
                inputHint = "请输入您的昵称";
                key = "nickname";
                break;
            case 2:
                subTitle = "设置个性签名";
                inputHint = "请输入您的个性签名";
                key = "sign";
                break;
            default:
                break;
        }
        if (index == 4) {
            dialogFragment.dismiss();
            final String[] items = {"拍照", "从相册选择", "小视频"};
            new CircleDialog.Builder()
                    .setCanceledOnTouchOutside(false)
                    .configDialog(params -> {
                        params.backgroundColorPress = Color.CYAN;
                        //增加弹出动画
                        params.animStyle = R.style.dialogWindowAnim;
                    })
                    .configNegative(params -> {
                        params.backgroundColorPress = Color.GRAY;
                    })
                    .setTitle("最后一步选择头像照片")
                    .configTitle(params -> {
                    })
                    .setSubTitle("请从以下中选择照片的方式进行提交\n文件大小不能超过3Mb")
                    .configSubTitle(params -> {
                    })
                    .setItems(items, (parent, view1, position1, id) -> {
                                //上传照片
                                if (items[position1].equals("从相册选择")) {
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    activity.startActivityForResult(intent, IMAGE_REQUEST_CODE);
                                }
                                Toast.makeText(context, "点击了：" + items[position1]
                                        , Toast.LENGTH_SHORT).show();
                                dialogFragment.dismiss();
                                finsh();
                            }
                    )
                    .setNegative("取消", v -> {
                        fial();
                    })
                    .show(fragmentManager);

        } else if (index == 3) {
            //时间插件设置
            String finalKey = "birthday";
            dpd.setAccentColor(Color.WHITE);
            dpd.setTitle("请选择您的出生日期");
            dpd.setOkColor(Color.GRAY);
            dpd.setCancelColor(Color.RED);
            dpd.show(fragmentManagerOfApp, "出生日期");
            dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                    //储存时间信息
                    Date birthday = new Date(year, monthOfYear, dayOfMonth);
                    new SharedPreferencesHelper(context, "user_msg_" + userName)
                            .put(finalKey, birthday);
                    Log.e("调试", "储存: " + finalKey + "|" + birthday);
                    dpd.dismiss();
                    modifyMsgForm(userName, index + 1);
                }
            });
            dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    fial();
                }
            });
        } else if (index == 1) {
            final String[] objectsR = {"男", "女", "保密"};
            final CheckedAdapter checkedAdapterR = new CheckedAdapter(context, objectsR, true);
            String finalKey1 = "sex";
            new CircleDialog.Builder()
                    .setCanceledOnTouchOutside(false)
                    .configDialog(params -> params.backgroundColorPress = Color.CYAN)
                    .setTitle("请选择你的性别")
                    .setSubTitle("只能选择一个")
                    .setItems(checkedAdapterR, (parent, view15, position15, id) ->
                            checkedAdapterR.toggle(position15, objectsR[position15]))
                    .setItemsManualClose(true)
                    .setPositive("下一步", v -> {
                                if (checkedAdapterR.getSaveChecked().size() == 0) {
                                    Toast.makeText(context
                                            , "您没有选择性别"
                                            , Toast.LENGTH_LONG).show();
                                    fial();
                                } else {
                                    //写入数据
                                    String sex = checkedAdapterR.getSaveChecked().toString();
                                    if (sex.contains("男"))
                                        sex = "男";
                                    if (sex.contains("女"))
                                        sex = "女";
                                    if (sex.contains("保密"))
                                        sex = "保密";
                                    new SharedPreferencesHelper(context, "user_msg_" + userName)
                                            .put(finalKey1, sex);
                                    Log.e("调试", "储存: " + finalKey1 + "|" + checkedAdapterR.getSaveChecked().toString());
                                    modifyMsgForm(userName, index + 1);
                                }
                            }
                    )
                    .show(fragmentManager);
        } else {
            String finalKey = key;
            dialogFragment = new CircleDialog.Builder()
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(true)
                    .setInputManualClose(true)
                    .setTitle(title)
                    .setSubTitle(subTitle)
                    .setInputHint(inputHint)
                    .autoInputShowKeyboard()
                    .setInputCounter(20)
                    .configInput(params -> {
                        params.padding = new int[]{30, 10, 10, 30};
                        params.inputBackgroundResourceId = R.drawable.bg_input;
                    })
                    .setPositiveInput(rightBtn, (text, v) -> {
                        if (TextUtils.isEmpty(text)) {
                            Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
                        } else {
                            if (0 <= index && index <= 4) {
                                //写入text数据
                                new SharedPreferencesHelper(context, "user_msg_" + userName)
                                        .put(finalKey, text);
                                Log.e("调试", "储存: " + finalKey + "|" + text);
                                dialogFragment.dismiss();
                                modifyMsgForm(userName, index + 1);
                            }
                        }
                    })
                    .show(fragmentManager);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    public abstract void finsh();

    public abstract void fial();
}
