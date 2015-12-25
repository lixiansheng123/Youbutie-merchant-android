package com.yuedong.youbutie_merchant_android.mouble.bmob.listener;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2015/12/18.
 */
public abstract class BmobSaveResultListener extends SaveListener {
    /**
     * 成功后返回对应的objectId
     *
     * @param result
     */
    public abstract void onResult(String result);


}
