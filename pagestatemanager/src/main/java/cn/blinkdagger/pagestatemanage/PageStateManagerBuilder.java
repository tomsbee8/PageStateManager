package cn.blinkdagger.pagestatemanage;

import android.app.Activity;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author ls
 * @Date 2020/5/11
 * @Description
 * @Version
 */
public class PageStateManagerBuilder {

    private Activity mActivity;
    private Fragment mFragment;

    private Integer mLoadingLayoutId = null;
    private Integer mFailedLayoutId = null;
    private Integer mEmptyLayoutId = null;
    private Integer mContentViewId = null;
    private boolean mShowLoadingWhenCreate = false;      // 是否在初始化的时候，显示正在加载中
    private ShowStateListener mStateListener;
    private HashMap<Integer, Integer> customLayoutMap = new HashMap<>();  // 存放所有状态的布局【key值为状态码,value为状态对应的布局Id】


    PageStateManagerBuilder(Activity activity) {
        this.mActivity = activity;
    }

    PageStateManagerBuilder(Fragment fragment) {
        this.mFragment = fragment;
    }

    public PageStateManagerBuilder setLoadingLayout(int loadingLayoutId) {
        this.mLoadingLayoutId = loadingLayoutId;
        return this;
    }

    public PageStateManagerBuilder setFailedLayout(int failedLayoutId) {
        this.mFailedLayoutId = failedLayoutId;
        return this;
    }

    public PageStateManagerBuilder setEmptyLayout(int emptyLayoutId) {
        this.mEmptyLayoutId = emptyLayoutId;
        return this;
    }

    public PageStateManagerBuilder setContentViewId(int contentViewId) {
        this.mContentViewId = contentViewId;
        return this;
    }

    public PageStateManagerBuilder setShowLoadingWhenCreate(boolean mShowLoadingWhenCreate) {
        this.mShowLoadingWhenCreate = mShowLoadingWhenCreate;
        return this;
    }

    public PageStateManagerBuilder setShowStateListener(ShowStateListener mStateListener) {
        this.mStateListener = mStateListener;
        return this;
    }

    public PageStateManagerBuilder addCustomStateLayout(@IntRange(from = 0) int stateCode, @LayoutRes int stateLayoutId) {
        customLayoutMap.put(stateCode, stateLayoutId);
        return this;
    }

    public PageStateManager get() {
        PageStateManager helper = new PageStateManager();
        helper.setShowLoadingWhenCreate(this.mShowLoadingWhenCreate);
        if (this.mEmptyLayoutId != null) {
            helper.setStateEmptyLayout(this.mEmptyLayoutId);
        }
        if (this.mFailedLayoutId != null) {
            helper.setStateFailedLayout(this.mFailedLayoutId);
        }
        if (this.mLoadingLayoutId != null) {
            helper.setStateLoadingLayout(this.mLoadingLayoutId);
        }
        helper.setShowStateListener(this.mStateListener);
        for (Map.Entry<Integer, Integer> entry : customLayoutMap.entrySet()) {
            helper.addState(entry.getKey(), entry.getValue());
        }

        if (this.mActivity != null) {
            if (mContentViewId != null) {
                helper.init(mActivity, this.mContentViewId);
            } else {
                helper.init(mActivity);
            }
        } else if (this.mFragment != null) {
            if (mContentViewId != null) {
                helper.init(mFragment, this.mContentViewId);
            } else {
                helper.init(mFragment);
            }
        } else {
            throw new IllegalArgumentException("context is illegal, can't init the PageStateManager");
        }
        return helper;
    }
}
