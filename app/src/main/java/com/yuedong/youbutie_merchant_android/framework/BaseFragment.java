package com.yuedong.youbutie_merchant_android.framework;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;

/**
 * Created by Administrator on 2015/11/27.
 *
 * @author 俊鹏
 */
public abstract class BaseFragment extends Fragment /*implements BmobQueryResListener*/ {
    private static final String TAG = "BaseFragment";
    protected RelativeLayout mMainLayout;
    protected LinearLayout mTitleLayout;
    protected MultiStateView mMultiStateView;
    //    protected LinearLayout mContentLayout;
//    protected LinearLayout mContentMask;
    private static final int ID_TITLE = 1;
    private static final int ID_CONTENT = 2;
    private static final int ID_CONTENT_MASK = 3;
    protected Handler mainHandler;
    protected BaseDialog loadDialog;
    private BaseDialog.Builder bb;
    protected BaseDialog loginDialog;
    protected BaseDialog.Builder loginDialogBuilder;
    protected BaseDialog unOpenDialog;
    protected BaseDialog.Builder unOpenDialogBuilder;
    public boolean initFinshed;

    public <T extends View> T fvById(int resId) {
        return ViewUtils.fvById(resId, mMainLayout);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i(TAG, "onCreate");
        mainHandler = new Handler(Looper.getMainLooper());
        initDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i(TAG, "onDestroy");
        initFinshed = false;
        dialogStatus(false);
    }

    protected void initDialog() {
        bb = new BaseDialog.Builder(getActivity());
        loadDialog = bb.createNetLoadingDialog();

        //-----提示登录的对话框
        loginDialogBuilder = new BaseDialog.Builder(getActivity());
        loginDialog = loginDialogBuilder.createTitleMessage2BtnDialog();
        loginDialog.setMessage("用户还没有进行登录");
        loginDialog.setTitle("提示");
        loginDialogBuilder.setPositiveButton(R.string.str_cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginDialogStatus(false);
            }
        });

        loginDialogBuilder.setNegativeButton(R.string.str_login, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginDialogStatus(false);
//                LaunchWithExitUtils.startActivity(getActivity(), LoginActivity.class);
            }
        });

        //-------------------提示未开放--------------------------------
        unOpenDialogBuilder = new BaseDialog.Builder(getActivity());
        unOpenDialog = unOpenDialogBuilder.createMessage1BtnDialog();
        unOpenDialogBuilder.setMessage("该功能未开放");
        unOpenDialogBuilder.setPositiveButton(R.string.str_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unOpenDialogStatus(false);
            }
        });


    }

    protected void unOpenDialogStatus(boolean isShow) {
        if (isShow) {
            if (unOpenDialog != null && !unOpenDialog.isShowing()) {
                unOpenDialog.show();
            }
        } else {
            if (unOpenDialog != null && unOpenDialog.isShowing()) {
                unOpenDialog.dismiss();
            }
        }
    }


    protected void loginDialogStatus(boolean isShow) {
        if (isShow) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
        } else {
            if (loginDialog != null && loginDialog.isShowing()) {
                loginDialog.dismiss();
            }
        }
    }

    protected boolean getLoginDialogIsShowing() {
        if (loginDialog == null)
            return true;
        else {
            return loginDialog.isShowing();
        }

    }

    protected void dialogStatus(boolean isShow) {
        AnimationDrawable animationDrawable = (AnimationDrawable) loadDialog.mLoaderPic.getDrawable();
        if (isShow) {
            if (loadDialog != null && !loadDialog.isShowing()) {
                animationDrawable.start();
                loadDialog.show();
            }
        } else {
            if (loadDialog != null && loadDialog.isShowing()) {
                animationDrawable.stop();
                loadDialog.dismiss();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.i(TAG, "onCreateView");
        initUi();
//        mMultiStateView.setViewForState(getContentView(container), MultiStateView.VIEW_STATE_CONTENT);
//        ViewUtils.adapterUiText(mMainLayout);
        initViews(savedInstanceState);
        initEvents();
        initFinshed = true;
        return mMainLayout;
    }

    @SuppressWarnings("ResourceType")
    private void initUi() {
        mMainLayout = new RelativeLayout(getActivity());
        mTitleLayout = new LinearLayout(getActivity());
        mMultiStateView = new MultiStateView(getActivity());
//        mContentMask = new LinearLayout(getActivity());
        // 统一设一个id
        mTitleLayout.setId(ID_TITLE);
        mMultiStateView.setId(ID_CONTENT);
//        mContentMask.setId(ID_CONTENT_MASK);
        RelativeLayout.LayoutParams titleLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mMainLayout.addView(mTitleLayout, titleLp);
        RelativeLayout.LayoutParams contentLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentLp.addRule(RelativeLayout.BELOW, mTitleLayout.getId());
        mMainLayout.addView(mMultiStateView, contentLp);
        mMainLayout.setClipToPadding(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            mMainLayout.setFitsSystemWindows(true);
    }


    /**
     * 初始化title布局
     */
    private void initTitleView(View titleView) {
        mTitleLayout.addView(titleView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Config.TITLE_HEIGHT));
    }

    protected void buildUi(View titleView, boolean enableDefaultEmptyView, boolean enbleDefaultLoadView, boolean enbleDfaultErrorView, int contentResId) {
        if (titleView != null)
            initTitleView(titleView);
        if (enableDefaultEmptyView)
            mMultiStateView.setViewForState(R.layout.empty_view, MultiStateView.VIEW_STATE_EMPTY);
        if (enbleDefaultLoadView)
            mMultiStateView.setViewForState(R.layout.loading_view, MultiStateView.VIEW_STATE_LOADING);
        if (enbleDfaultErrorView)
            mMultiStateView.setViewForState(R.layout.error_view, MultiStateView.VIEW_STATE_ERROR);
        if (contentResId != -1)
            mMultiStateView.setViewForState(contentResId, MultiStateView.VIEW_STATE_CONTENT);
    }

    protected void buildUi(View titleView, int emptyView, int loadView, int errorView, View contentView) {
        if (titleView != null)
            initTitleView(titleView);
        if (emptyView > 0)
            mMultiStateView.setViewForState(emptyView, MultiStateView.VIEW_STATE_EMPTY);
        if (loadView > 0)
            mMultiStateView.setViewForState(loadView, MultiStateView.VIEW_STATE_LOADING);
        if (errorView > 0)
            mMultiStateView.setViewForState(errorView, MultiStateView.VIEW_STATE_ERROR);
        if (contentView != null)
            mMultiStateView.setViewForState(contentView, MultiStateView.VIEW_STATE_CONTENT);
    }


    public void error(String s, boolean dialogStatus) {
        L.e("error:" + s);
        T.showLong(getActivity(), "出现错误" + s);
        dialogStatus(dialogStatus);
    }

    public void error(String s) {
        L.e("error:" + s);
        T.showLong(getActivity(), "出现错误:" + s);
    }

    protected View inflater(int resId) {
        return View.inflate(getActivity(), resId, mMainLayout);
    }


    public abstract void initViews(Bundle savedInstanceState);

    public abstract void initEvents();

}
