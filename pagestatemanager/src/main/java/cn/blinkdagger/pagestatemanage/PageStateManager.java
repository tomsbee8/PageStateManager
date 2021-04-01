package cn.blinkdagger.pagestatemanage;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author ls
 * @Date 2020/4/22
 * @Description 页面状态管理
 * @Version
 */
 public class PageStateManager {

    private final String TAG = "StateHelper";

    // 默认状态
    private static final String STATE_LOADING = "loading";     // 加载中
    private static final String STATE_LOAD_EMPTY = "empty";    // 空数据
    private static final String STATE_LOAD_SUCCESS = "success";// 加载成功
    private static final String STATE_LOAD_FAILED = "error";   // 加载出错

    private HashMap<Integer, View> stateViewMap = new HashMap<>();        // 存放所有布局和视图【key值为布局Id,value为生成的View】
    private HashMap<String, Integer> stateLayoutIdMap = new HashMap<>();  // 存放所有状态的布局【key值为状态码,value为状态对应的布局Id】
    private View mTargetView = null;                     // 加载内容的视图
    private ViewGroup mContainerView = null;           // 存放内容View和各状态View的容器

    private boolean mShowLoadingWhenCreate = false;      // 是否在初始化的时候，显示正在加载中
    private boolean mEnableStateViewScroll = false;      // 是否允许状态布局滚动

    private ShowStateListener mStateListener;

    protected PageStateManager(){}

    /**
     * 在AppCompatActivity 中添加多状态View
     *
     * @param activity
     */
    protected void init(final Activity activity) {
        if (activity == null) {
            return;
        }
        mTargetView = activity.findViewById(android.R.id.content);
        init(activity, activity.getLayoutInflater());
    }

    /**
     * 在AppCompatActivity 中添加多状态View
     *
     * @param activity
     * @param contentViewId
     */
    protected void init(final Activity activity, @IdRes int contentViewId) {
        if (activity == null) {
            return;
        }
        mTargetView = activity.findViewById(contentViewId);
        init(activity, activity.getLayoutInflater());
    }


    /**
     * 在Fragment 添加多状态View
     *
     * @param fragment
     */
    protected void init(final Fragment fragment) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }
        mTargetView = fragment.getView();
        init(fragment.getActivity(), fragment.getLayoutInflater());
    }

    /**
     * 在Fragment 中添加多状态View
     *
     * @param fragment
     * @param contentViewId
     */
    protected void init(final Fragment fragment, @IdRes int contentViewId) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }
        if (fragment.getView() == null) {
            Log.e(TAG, "Init pageState view failed in fragment ,maybe the fragment has not finish creating view");
            return;
        }
        mTargetView = fragment.getView().findViewById(contentViewId);
        init(fragment.getActivity(), fragment.getLayoutInflater());
    }

    private void init(Context context, LayoutInflater originInflater) {

        if(mTargetView == null){
            new IllegalArgumentException("PageStateStateMachine will not working, because targetView cannot be found , illegal content view Id was set up").printStackTrace();
            return;
        }

        if (mTargetView.getParent() != null) {
            ViewGroup parentView = (ViewGroup) mTargetView.getParent();
            int originIndex = parentView.indexOfChild(mTargetView);
            ViewGroup.LayoutParams originLayoutParams = mTargetView.getLayoutParams();
            parentView.removeView(mTargetView);

            mContainerView = new FrameLayout(context);

            if(mEnableStateViewScroll){
                NestedScrollView  scrollParentView = new NestedScrollView(context);
                scrollParentView.removeAllViews();
                scrollParentView.addView(mContainerView);
                parentView.addView(scrollParentView, originIndex, originLayoutParams);
            }else{
                mContainerView.addView(mTargetView, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                parentView.addView(mContainerView, originIndex, originLayoutParams);
            }

            AsyncLayoutInflater newInflater = new AsyncLayoutInflater(originInflater.getContext());
            for (Map.Entry<String, Integer> entry : stateLayoutIdMap.entrySet()) {
                int stateLayoutId = entry.getValue();
                if (!stateViewMap.containsKey(stateLayoutId)) {
                    newInflater.inflate(stateLayoutId, null, new AsyncLayoutInflater.OnInflateFinishedListener() {
                        @Override
                        public void onInflateFinished(@NonNull View stateView, int resId, @Nullable ViewGroup parent) {
                            stateView.setVisibility(View.INVISIBLE);
                            mContainerView.addView(stateView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                            stateViewMap.put(resId, stateView);
                        }
                    });
                }
            }
            parentView.post(new Runnable() {
                @Override
                public void run() {
                    if (mShowLoadingWhenCreate) {
                        showLoading();
                    } else {
                        showContent();
                    }
                }
            });
        } else {
            throw new IllegalStateException("target view does not have a parent view");
        }
    }

    private void setStateViewScrollEnable(Boolean scrollEnable) {

    }


    private void showStateView(String state) {

        if (mTargetView == null) {
            throw new IllegalStateException("cannot get content view when show state view ");
        }

        if (state.equals(STATE_LOAD_SUCCESS)) {
            mTargetView.setVisibility(View.VISIBLE);
            for (Map.Entry<Integer, View> entry : stateViewMap.entrySet()) {
                entry.getValue().setVisibility(View.GONE);
            }
        } else {
            // 需要显示的状态布局Id
            Integer needShowLayoutId = 0;
            for (Map.Entry<String, Integer> entry : stateLayoutIdMap.entrySet()) {
                String stateName = entry.getKey();
                if (stateName.equals(state)) {
                    needShowLayoutId = entry.getValue();
                }
            }

            if (needShowLayoutId != 0) {
                mTargetView.setVisibility(View.GONE);
                for (Map.Entry<Integer, View> entry : stateViewMap.entrySet()) {
                    Integer stateLayoutId = entry.getKey();
                    View stateView = entry.getValue();
                    if (stateLayoutId.equals(needShowLayoutId)) {
                        stateView.setVisibility(View.VISIBLE);
                        onStateListenerWork(state, stateView);
                    } else {
                        stateView.setVisibility(View.GONE);
                    }
                }
            } else {
                Log.e(TAG, "Show PageState view failed ,because you did not set the layout of [" + state +"] State");
            }
        }
    }

    private void onStateListenerWork(String state, View theStateView) {
        if (mStateListener != null && !TextUtils.isEmpty(state)) {
            switch (state) {
                case STATE_LOADING:
                    mStateListener.onShowLoading(theStateView);
                    break;
                case STATE_LOAD_FAILED:
                    mStateListener.onShowLoadFailed(theStateView);
                    break;
                case STATE_LOAD_EMPTY:
                    mStateListener.onShowLoadEmpty(theStateView);
                    break;
                default:
                    mStateListener.onShowCustomState(Integer.parseInt(state), theStateView);
                    break;
            }
        }
    }

    public void showContent() {
        showStateView(STATE_LOAD_SUCCESS);
    }

    public void showLoadFailed() {
        showStateView(STATE_LOAD_FAILED);
    }

    public void showLoading() {
        showStateView(STATE_LOADING);
    }

    public void showLoadEmpty() {
        showStateView(STATE_LOAD_EMPTY);
    }

    public void showCustomStateView(int stateCode){
        showStateView(String.valueOf(stateCode));
    }

    public void showNothing() {
        mContainerView.removeAllViews();
        mContainerView.setVisibility(View.GONE);
    }

    protected void setStateLoadingLayout(@LayoutRes int loadingLayoutId) {
        stateLayoutIdMap.put(STATE_LOADING, loadingLayoutId);
    }

    protected void setStateFailedLayout(@LayoutRes int failedLayoutId) {
        stateLayoutIdMap.put(STATE_LOAD_FAILED, failedLayoutId);
    }

    protected void setStateEmptyLayout(@LayoutRes int emptyLayoutId) {
        stateLayoutIdMap.put(STATE_LOAD_EMPTY, emptyLayoutId);
    }

    protected void addState(@IntRange(from = 0) int stateCode, @LayoutRes int stateLayoutId) {
        stateLayoutIdMap.put(String.valueOf(stateCode), stateLayoutId);
    }

    protected void setShowStateListener(ShowStateListener mStateListener) {
        this.mStateListener = mStateListener;
    }

    protected void setShowLoadingWhenCreate(boolean mShowLoadingWhenCreate) {
        this.mShowLoadingWhenCreate = mShowLoadingWhenCreate;
    }
}
