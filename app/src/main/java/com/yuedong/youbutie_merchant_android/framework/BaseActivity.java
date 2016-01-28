package com.yuedong.youbutie_merchant_android.framework;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.SerializableMap;
import com.yuedong.youbutie_merchant_android.model.UserEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.model.receive.BDPushReceiver;
import com.yuedong.youbutie_merchant_android.utils.ActivityTaskUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RequestYDHelper;
import com.yuedong.youbutie_merchant_android.utils.SPUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.utils.WindowUtils;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;

import org.json.JSONObject;

import java.util.Map;

import cn.bmob.v3.listener.GetListener;

public abstract class BaseActivity extends AppCompatActivity {
    protected RelativeLayout mMainLayout;
    protected LinearLayout mTitleLayout;
    //    protected LinearLayout mContentLayout;
    protected MultiStateView mMultiStateView;
    /**
     * 給内容区域填充一个遮罩 具体内容由子类设定
     */
//    protected LinearLayout mContentMask;
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
        initDialog();
        registNotifyMsgReceive();
    }

    protected void onCreate(Bundle savedInstanceState, boolean initSystemBar) {
        super.onCreate(savedInstanceState);
        ActivityTaskUtils.getInstance().addActivity(this);
        context = this;
        activity = this;
        initUi();
        initDialog();
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


    long lastClickTime;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            long time = System.currentTimeMillis();
            long timeD = time - lastClickTime;
            lastClickTime = time;
            if (timeD <= 200)
                return true;
        }

        return super.dispatchTouchEvent(ev);
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
        dialogStatus(false);
    }

    @SuppressWarnings("ResourceType")
    private void initUi() {
        mMainLayout = new RelativeLayout(this);
        mTitleLayout = new LinearLayout(this);
        mMultiStateView = new MultiStateView(this);
        // 统一设一个id
        mTitleLayout.setId(ID_TITLE);
        mMultiStateView.setId(ID_CONTENT);
        RelativeLayout.LayoutParams titleLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mMainLayout.addView(mTitleLayout, titleLp);
        RelativeLayout.LayoutParams contentLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentLp.addRule(RelativeLayout.BELOW, mTitleLayout.getId());
        mMainLayout.addView(mMultiStateView, contentLp);
        setContentView(mMainLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 实现沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            paddingStatusBarSpan(true);
        }
    }

    /**
     * padding状态栏的空间
     */
    public void paddingStatusBarSpan(boolean paddingStatusBarSpan) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMainLayout.setClipToPadding(paddingStatusBarSpan);
            mMainLayout.setFitsSystemWindows(paddingStatusBarSpan);
        }
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
                notifyMsg(type, map, jsonObject);
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
    protected void notifyMsg(int msgType, Map<String, Object> data, JSONObject jsonObject) {
        if (msgType == RequestYDHelper.PUSH_TYPE_MERCHANT_INVITE_MEMBER) {
            try {
                String userId = jsonObject.getString("userId");
                if (StringUtil.isNotEmpty(userId)) {
                    UserEvent.getInstance().findUserById(userId, new GetListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            String mobilePhoneNumber = user.getMobilePhoneNumber();
                            if (!TextUtils.isEmpty(mobilePhoneNumber)) {
                                String sendMobile = (String) SPUtils.get(context, Constants.SP_INVITE_ADD_MEMBER, "");
                                if (sendMobile.contains(mobilePhoneNumber)) {
                                    L.d("sendMobile:start" + sendMobile);
                                    sendMobile = sendMobile.replace(mobilePhoneNumber, "");
                                    L.d("sendMobile:end" + sendMobile);
                                    SPUtils.put(context, Constants.SP_INVITE_ADD_MEMBER, sendMobile);
                                }
                            }
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            L.d("findUserById-onFailure:" + s);
                        }
                    });

                }
            } catch (Exception e) {

            }

        }
    }

    protected abstract void initViews();

    protected abstract void initEvents();

    protected abstract void ui();

}