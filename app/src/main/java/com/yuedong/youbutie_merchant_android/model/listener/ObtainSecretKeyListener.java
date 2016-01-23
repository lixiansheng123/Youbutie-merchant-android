package com.yuedong.youbutie_merchant_android.model.listener;

/**
 * 获取悦动SecretKey监听器
 */
public interface ObtainSecretKeyListener {
    void start();

    void end();

    void succeed(String secretKey);

    void fail(int code, String error);
}
