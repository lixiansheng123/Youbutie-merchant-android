package com.yuedong.youbutie_merchant_android.utils;

import android.app.Activity;
import android.net.Uri;

/**
 * Created by Administrator on 2015/12/11.
 */
public interface CropHandler {

    void onPhotoCropped(Uri uri);

    void onCropCancel();

    void onCropFailed(String message);

    CropParams getCropParams();

    Activity getContext();
}
