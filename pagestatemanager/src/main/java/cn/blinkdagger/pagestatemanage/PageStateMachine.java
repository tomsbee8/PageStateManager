package cn.blinkdagger.pagestatemanage;

import android.app.Activity;

import androidx.fragment.app.Fragment;

/**
 * @Author ls
 * @Date 2020/5/8
 * @Description 页面状态管理
 * @Version
 */
public class PageStateMachine {

    public static PageStateManagerBuilder with(Activity activity) {
        return new PageStateManagerBuilder(activity);
    }
    public static PageStateManagerBuilder with(Fragment fragment) {
        return new PageStateManagerBuilder(fragment);
    }
}
