package com.shijie.wedget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mylhyl.circledialog.CircleDialog;
import com.shijie.R;
import com.shijie.adapter.CheckedAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

/**
 * 表单窗口类
 * Created by haoge on 2018/8/19.
 */

public abstract class FormWindow implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Context context;
    private DialogFragment dialogFragment;
    private Calendar now;
    private DatePickerDialog dpd;
    private FragmentManager fragmentManager;
    private android.app.FragmentManager fragmentManagerOfApp;

    /**
     * FormWindow构造函数
     *
     * @param context
     * @param fragmentManager
     * @param fragmentManagerOfApp
     */
    public FormWindow(Context context, FragmentManager fragmentManager, android.app.FragmentManager fragmentManagerOfApp) {
        this.context = context;
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
    public void modifyMsgForm(int index) {
        String rightBtn = "下一步";
        String title="请完善个人资料";
        String subTitle = null;
        String inputHint = null;
        String key = null;
        switch (index) {
            case 0:
                title = "检测到您是第一次登陆!";
                subTitle = "请完善个人资料\n设置昵称";
                inputHint = "请输入您的昵称";
                key = "nikeName";
                break;
            case 1:
                key = "sex";
                break;
            case 2:
                key = "imgfile";
                break;
            case 3:
                key = "birthday";
                break;
            case 4:
                subTitle = "设置个性签名";
                inputHint = "请输入您的个性签名";
                rightBtn = "完成";
                key = "sign";
                break;
            default:
                break;
        }
        if (index == 2) {
            key = "imgfile";
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
                    .setTitle("选择头像照片")
                    .configTitle(params -> {
                    })
                    .setSubTitle("请从以下中选择照片的方式进行提交")
                    .configSubTitle(params -> {
                    })
                    .setItems(items, (parent, view1, position1, id) -> {
                                //上传照片
                                Toast.makeText(context, "点击了：" + items[position1]
                                        , Toast.LENGTH_SHORT).show();
                              modifyMsgForm(index + 1);
                            }
                    )
                    .setNegative("取消", v -> {
                       fial();
                    })
                    .show(fragmentManager);

        } else if (index == 3) {
            //时间插件设置
            dpd.setAccentColor(Color.WHITE);
            dpd.setTitle("请选择您的出生日期");
            dpd.setOkColor(Color.GRAY);
            dpd.setCancelColor(Color.RED);
            dpd.show(fragmentManagerOfApp, "Datepickerdialog");
            dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                    //储存时间信息
                    Date birthday = new Date(year, monthOfYear, dayOfMonth);
                    Log.e("调试", "选择时间1 " + birthday);
                    dpd.dismiss();
                    modifyMsgForm(index + 1);
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
            new CircleDialog.Builder()
                    .setCanceledOnTouchOutside(false)
                    .configDialog(params -> params.backgroundColorPress = Color.CYAN)
                    .setTitle("请选择你的性别")
                    .setSubTitle("只能选择一个")
                    .setItems(checkedAdapterR, (parent, view15, position15, id) ->
                            checkedAdapterR.toggle(position15, objectsR[position15]))
                    .setItemsManualClose(true)
                    .setPositive("下一步", v -> {
                        if(checkedAdapterR.getSaveChecked().size()==0){
                            Toast.makeText(context
                                    , "您没有选择性别"
                                    , Toast.LENGTH_LONG).show();
                            fial();
                        }else {
                            //写入数据
                            Toast.makeText(context
                                    , "选择了：" + checkedAdapterR.getSaveChecked().toString()
                                    , Toast.LENGTH_SHORT).show();
                            modifyMsgForm(index + 1);
                        }
                            }
                    )
                    .show(fragmentManager);
        } else {
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
                                if (index == 4) {
                                    //调用回调方法
                                    success();
                                    dialogFragment.dismiss();
                                    //调用model操作

                                } else {
                                    dialogFragment.dismiss();
                                    modifyMsgForm(index + 1);
                                }
                            }
                        }
                    })
                    .show(fragmentManager);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    public abstract void success();
    public abstract void fial();
}
