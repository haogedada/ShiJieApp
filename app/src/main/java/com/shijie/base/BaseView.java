package com.shijie.base;

/**基类
 * 提取所有view共有的方法
 * 所有vidw都继承于BaseView
 * Created by haoge on 2018/8/15.
 */

public interface BaseView {
    /**
     * 显示dialog
     */
    void showLoading();

    /**
     * 隐藏 dialog
     */

    void hideLoading();

    /**
     * 显示错误信息
     *
     * @param msg
     */
    void showError(String msg);

    /**
     * 网络错误
     */
    void showNetworkError(String errMsg);
}
