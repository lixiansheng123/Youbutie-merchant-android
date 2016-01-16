package com.yuedong.youbutie_merchant_android.framework;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.SerializableMap;
import com.yuedong.youbutie_merchant_android.mouble.receive.BDPushReceiver;
import com.yuedong.youbutie_merchant_android.utils.ActivityTaskUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RequestYDHelper;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.utils.WindowUtils;

import org.json.JSONObject;

import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {
    protected RelativeLayout mMainLayout;
    protected LinearLayout mTitleLayout;
    protected LinearLayout mContentLayout;
    /**
     * 給内容区域填充一个遮罩 具体内容由子类设定
     */
    protected LinearLayout mContentMask;
    protected Handler mainHandle = new Handler(Looper.getMainLooper());
    private static final int ID_TITLE = 1;
    private static final int ID_CONTENT = 2;
    private static final int ID_CONTENT_MASK = 3;
    private FragmentManager mFManager;
    protected Fragment mDisplayContext;
    protected Context context;
    protected Activity activity;
    protected BaseDialog loadDialog;
    protected BaseDialog.Builder dialogBuilder;
    protected BaseDialog loginDialog;
    protected BaseDialog.Builder loginDialogBuilder;
    protected BaseDialog unOpenDialog;
    protected BaseDialog.Builder unOpenDialogBuilder;
    /**
     * 第一次进入界面
     */
    private boolean one = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTaskUtils.getInstance().addActivity(this);
        context = this;
        activity = this;
        initUi();
//        setShowContentView(new TabLayout(this));
        initDialog();
        initSystemBar(Config.STATUSBAR_COLOR);
        registNotifyMsgReceive();
    }

    protected void onCreate(Bundle savedInstanceState, boolean initSystemBar) {
        super.onCreate(savedInstanceState);
        ActivityTaskUtils.getInstance().addActivity(this);
        context = this;
        activity = this;
        initUi();
        initDialog();
        if (initSystemBar)
            initSystemBar(Config.STATUSBAR_COLOR);
        registNotifyMsgReceive();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (one) {
            one = false;
            initViews();
            initEvents();
            ui();
        }
    }

    // 当前状态栏背景
    private int currentStatusBarColor;

    public void initSystemBar() {
        initSystemBar(Config.STATUSBAR_COLOR);
    }

    public void initSystemBar(int statusBarBg) {
        if (currentStatusBarColor != statusBarBg) {
            currentStatusBarColor = statusBarBg;
            WindowUtils.setStatusBarColor(this, statusBarBg);
        }
    }


    private void initDialog() {
        dialogBuilder = new BaseDialog.Builder(this);
        loadDialog = dialogBuilder.createNetLoadingDialog();
        //-----提示登录的对话框
        loginDialogBuilder = new BaseDialog.Builder(this);
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
            }
        });
        //-------------------提示未开放--------------------------------
        unOpenDialogBuilder = new BaseDialog.Builder(context);
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


    public void dialogStatus(boolean isShow) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistNotifyMsgRecevie();
        ActivityTaskUtils.getInstance().reomveActivity(this);
    }

    @SuppressWarnings("ResourceType")
    private void initUi() {
        mMainLayout = new RelativeLayout(this);
        mTitleLayout = new LinearLayout(this);
        mContentLayout = new LinearLayout(this);
        mContentMask = new LinearLayout(this);
        // 统一设一个id
        mTitleLayout.setId(ID_TITLE);
        mContentLayout.setId(ID_CONTENT);
        mContentMask.setId(ID_CONTENT_MASK);
        RelativeLayout.LayoutParams titleLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mMainLayout.addView(mTitleLayout, titleLp);
        RelativeLayout.LayoutParams contentLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        contentLp.addRule(RelativeLayout.BELOW, mTitleLayout.getId());
        mMainLayout.addView(mContentLayout, contentLp);
        RelativeLayout.LayoutParams contentMaskLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        contentMaskLp.addRule(RelativeLayout.BELOW, mTitleLayout.getId());
        mMainLayout.addView(mContentMask, contentMaskLp);
        setContentView(mMainLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMainLayout.setClipToPadding(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            mMainLayout.setFitsSystemWindows(true);
    }

    /**
     * 增加内容填充区域
     *
     * @param view
     */
    public void setContentMaskView(View view) {
        mContentMask.removeAllViews();
        mContentMask.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置显示的内容
     *
     * @param resId
     */
    public void setShowContentView(int resId) {
        if (resId < 0)
            return;
        setShowContentView(ViewUtils.inflaterView(context, resId, mContentLayout));
    }

    public void setShowContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 初始化title布局
     *
     * @param titleView
     */
    protected void initTitleView(View titleView) {
        mTitleLayout.removeAllViews();
        if (titleView.getParent() != null) {
            ViewGroup parent = (ViewGroup) titleView.getParent();
            parent.removeView(titleView);
        }
        mTitleLayout.addView(titleView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Config.TITLE_HEIGHT));
    }


    /**
     * 切换显示的Fragment
     *
     * @param from 当前Fragment
     * @param to   要切换的Fragment
     */
    public void switchContent(Fragment from, Fragment to, int containerId) {
        if (mFManager == null)
            mFManager = getSupportFragmentManager();
        if (from == null) {
            from = getDefaultFrag();
        }
        if (mDisplayContext != to) {
            mDisplayContext = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) { // 有没有将这碎片加入到当前Activity
                transaction.hide(from).add(containerId, to, to.getClass().getSimpleName());
                transaction.show(to).commit();
                    /*
                     * 事物commit只有异步处理 这是需要时间的 调用FragfmentManager
                     * 的executePendingTransactions可以让其马上执行，但是只能在主线程调用
                     */
                mFManager.executePendingTransactions();
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示toFragment
                mFManager.executePendingTransactions();
            }
        }
    }


    /**
     * 加载fragment
     *
     * @param fragment
     * @param containerId
     * @param isAddToBack
     */
    public void addFragment(Fragment fragment, int containerId, boolean isAddToBack, String fragmentTag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // Replace whatever is in thefragment_container view with this fragment,
        // and add the transaction to the backstack
        if (fragmentTag != null)
            transaction.add(containerId, fragment, fragmentTag);
        else
            transaction.add(containerId, fragment);
        if (isAddToBack)
            transaction.addToBackStack(null);
        // 提交修改
        transaction.commit();
    }

    public void defaultFinished() {
        LaunchWithExitUtils.back(this);
    }

    public void back() {
        LaunchWithExitUtils.back(this);
    }

    protected Fragment getDefaultFrag() {
        return null;
    }


    public <T extends View> T fvById(int resId) {
        return ViewUtils.fvById(resId, mMainLayout);
    }

    public void error(String s, boolean dialogStatus) {
        L.e("error:" + s);
        T.showLong(context, "出现错误" + s);
        dialogStatus(dialogStatus);
    }

    public void error(String s) {
        L.e("error:" + s);
        T.showLong(context, "出现错误:" + s);
    }

    private class NotifyMsgReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                SerializableMap serializableMap = (SerializableMap) intent.getSerializableExtra(Constants.KEY_BEAN);
                Map<String, Object> map = serializableMap.getMap();
                String json = (String) map.get(BDPushReceiver.CUSTOM_CONTENT);
                JSONObject jsonObject = new JSONObject(json);
                int type = jsonObject.getInt("type");
                notifyMsg(type, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private NotifyMsgReceive notifyMsgReceive;

    private void registNotifyMsgReceive() {
        notifyMsgReceive = new NotifyMsgReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_NOTIFY);
        registerReceiver(notifyMsgReceive, intentFilter);
    }

    private void unRegistNotifyMsgRecevie() {
        if (notifyMsgReceive != null) {
            unregisterReceiver(notifyMsgReceive);
            notifyMsgReceive = null;
        }
    }

    /**
     * 接收到通知了
     */
    protected void notifyMsg(int msgType, Map<String, Object> data) {
    }

    protected abstract void initViews();

    protected abstract void initEvents();

    protected abstract void ui();

}