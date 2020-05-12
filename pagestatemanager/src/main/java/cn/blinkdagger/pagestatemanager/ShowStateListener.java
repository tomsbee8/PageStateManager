package cn.blinkdagger.pagestatemanager;

import android.view.View;

/**
 * @Author ls
 * @Date 2020/5/8
 * @Description 页面状态监听
 * @Version
 */
public interface ShowStateListener {
    /**
     * 显示加载中
     * @param loadingView
     */
    void onShowLoading(View loadingView);

    /**
     * 显示加载失败
     * @param failedView
     */
    void onShowLoadFailed(View failedView);

    /**
     * 显示加载数据为空
     * @param emptyView
     */
    void onShowLoadEmpty(View emptyView);

    /**
     * 显示自定义状态页面
     * @param stateCode 自定义状态码
     * @param loadingView
     */
    void onShowCustomState(int stateCode, View loadingView);
}
