package com.yuedong.youbutie_merchant_android.model;

import android.content.Context;

import com.yuedong.youbutie_merchant_android.app.App;

/**
 * Created by Administrator on 2015/12/23.
 */
public interface BaseEvent {
     Context context = App.getInstance().getAppContext();
     static final String OBJECT_ID = "objectId";

}
