package com.yuedong.youbutie_merchant_android.model;

import android.content.Context;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.utils.T;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;

/**
 * Created by Administrator on 2015/12/23.
 */
public interface BaseEvent {
    Context context = App.getInstance().getAppContext();
    static final String OBJECT_ID = "objectId";


}
