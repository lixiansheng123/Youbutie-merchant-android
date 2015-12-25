package com.yuedong.youbutie_merchant_android.framework;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.utils.CropHandler;
import com.yuedong.youbutie_merchant_android.utils.CropHelper;
import com.yuedong.youbutie_merchant_android.utils.CropParams;
import com.yuedong.youbutie_merchant_android.utils.FileUtils;

import java.io.File;

/**
 * Created by Administrator on 2015/12/11.
 */
public class BasePhotoCropActivity extends BaseActivity implements CropHandler {
    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onPhotoCropped(Uri uri) {
    }

    @Override
    public void onCropCancel() {
    }

    @Override
    public void onCropFailed(String message) {
    }

    @Override
    public CropParams getCropParams() {
        return null;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        FileUtils.deleteFile(new File(Config.DIR_CROP_PIC));
        super.onDestroy();
    }
}
