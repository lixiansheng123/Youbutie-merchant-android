package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.R;

/**
 * Created by Administrator on 2015/12/29.
 */
public class SimplePicConfig {
    private int loadPic;
    private int errorPic;

    public SimplePicConfig() {
        loadPic = R.drawable.bg_picture_other_default;
        errorPic = R.drawable.bg_picture_other_default;
    }

    public int getLoadPic() {
        return loadPic;
    }

    public void setLoadPic(int loadPic) {
        this.loadPic = loadPic;
    }

    public int getErrorPic() {
        return errorPic;
    }

    public void setErrorPic(int errorPic) {
        this.errorPic = errorPic;
    }

}
