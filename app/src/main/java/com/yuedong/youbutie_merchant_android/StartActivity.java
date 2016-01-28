package com.yuedong.youbutie_merchant_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.model.CarEvent;
import com.yuedong.youbutie_merchant_android.model.ServiceInfoEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.ServiceInfo;
import com.yuedong.youbutie_merchant_android.model.db.CarDao;
import com.yuedong.youbutie_merchant_android.model.db.ServiceInfoDao;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.SPUtils;
import com.yuedong.youbutie_merchant_android.utils.WindowUtils;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class StartActivity extends BaseActivity {
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowUtils.hideSystemBar(this);
        super.onCreate(savedInstanceState);
        buildUi(null, false, false, false, R.layout.activity_start);
    }

    @Override
    protected void initViews() {
        rootView = fvById(R.id.id_root);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {
        initData();
    }

    private void initData() {
        List<ServiceInfo> all1 = ServiceInfoDao.getInstance().findAll();
        final List<Car> cars = CarDao.getInstance().findAll();
        if (!CommonUtils.listIsNotNull(all1)) {
            L.d("启动页获取服务信息-------------------->>");
            // 获取服务信息
            ServiceInfoEvent.getInstance().findAllServiceInfo(new FindListener<ServiceInfo>() {

                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(final List<ServiceInfo> list) {
//                    L.d();
                    if (CommonUtils.listIsNotNull(list))
                        App.getInstance().getExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                ServiceInfoDao.getInstance().saveAll(list);
                                task2(cars);
                            }
                        });
                }

                @Override
                public void onError(int i, String s) {
                    error(s);
                    defaultFinished();
                }
            });
        } else {
            task2(cars);
        }
    }


    private void task2(List<Car> all2) {
        if (!CommonUtils.listIsNotNull(all2)) {
            L.d("启动页获取获取小车信息-------------------->>");
            // 获取小车信息
            CarEvent.getInstance().getAllCar(new FindListener<Car>() {
                @Override
                public void onSuccess(final List<Car> list) {

                    App.getInstance().getExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            CarDao.getInstance().saveAll(list);
                            jump(0);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    error(s);
                    defaultFinished();
                }
            });
        } else {
            jump(1000);
        }
    }

    public void jump(int delay) {
        mainHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 启动动画
                AnimationSet animationSet = new AnimationSet(true);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(1000);
                animationSet.addAnimation(scaleAnimation);
                animationSet.setFillAfter(true);
                rootView.startAnimation(animationSet);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        boolean guide = (boolean) SPUtils.get(context, Constants.SP_GUIDE, false);
                        Class<? extends Activity> cls = null;
                        if (guide) {
                            if (App.getInstance().isLogin())
                                cls = MainActivity.class;
                            else
                                cls = LoginActivity.class;
                        } else
                            cls = GuideActivity.class;
                        LaunchWithExitUtils.startActivity(activity, cls);
                        defaultFinished();
                        overridePendingTransition(0, 0);
                        defaultFinished();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }, delay);
    }
}
