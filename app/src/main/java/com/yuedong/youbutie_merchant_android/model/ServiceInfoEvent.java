package com.yuedong.youbutie_merchant_android.model;

import com.yuedong.youbutie_merchant_android.model.bmob.bean.ServiceInfo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class ServiceInfoEvent implements BaseEvent {
    private static ServiceInfoEvent INSTANCE;

    private ServiceInfoEvent() {
    }

    public static ServiceInfoEvent getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceInfoEvent.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceInfoEvent();
                }
            }
        }
        return INSTANCE;
    }

    public void findAllServiceInfo(final FindListener<ServiceInfo> listener) {
        listener.onStart();
        BmobQuery<ServiceInfo> query = new BmobQuery<ServiceInfo>();
        query.findObjects(context, new FindListener<ServiceInfo>() {
            @Override
            public void onSuccess(List<ServiceInfo> list) {
                listener.onSuccess(list);
                listener.onFinish();
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
                listener.onFinish();
            }
        });
    }
}
