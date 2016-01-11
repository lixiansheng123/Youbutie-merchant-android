package com.yuedong.youbutie_merchant_android;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.CarEvent;
import com.yuedong.youbutie_merchant_android.mouble.ServiceInfoEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.ServiceInfo;
import com.yuedong.youbutie_merchant_android.mouble.db.CarDao;
import com.yuedong.youbutie_merchant_android.mouble.db.ServiceInfoDao;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


    }

    @Override
    protected void initViews() {

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
                Class cls = null;
                if (App.getInstance().isLogin())
                    cls = MainActivity.class;
                else
                    cls = LoginActivity.class;
                LaunchWithExitUtils.startActivity(activity, cls);
                defaultFinished();
            }
        }, delay);
    }
}
