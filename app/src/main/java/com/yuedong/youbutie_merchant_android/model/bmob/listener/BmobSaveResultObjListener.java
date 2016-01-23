package com.yuedong.youbutie_merchant_android.model.bmob.listener;

import cn.bmob.v3.listener.SaveListener;

/**
 * bmob回调类成功之后回调对应bean
 */
public abstract class BmobSaveResultObjListener<T> extends SaveListener {

    public abstract void onResult(T result);
}
