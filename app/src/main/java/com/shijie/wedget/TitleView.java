package com.shijie.wedget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shijie.R;

/**
 * Created by ludoven on 2018/8/15.
 */

public class TitleView extends RelativeLayout {
    private Button mButton;  //返回按钮
    private TextView mTextView; //标题

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //加载布局
        LayoutInflater.from(context).inflate(R.layout.title_bar, this);

        //获取控件
        mButton = findViewById(R.id.bt_title);
        mTextView = findViewById(R.id.tv_title);
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setLeftButtonListener(OnClickListener listener) {
        mButton.setOnClickListener(listener);
    }

    // 设置标题的方法
    public void setTitleText(String title) {
        mTextView.setText(title);
    }

}
