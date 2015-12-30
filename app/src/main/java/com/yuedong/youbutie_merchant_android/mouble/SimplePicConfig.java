package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.R;

/**
 * Created by Administrator on 2015/12/29.
 */
public class SimplePicConfig {
    private int loadPic;
    private int errorPic;

    public SimplePicConfig() {
        loadPic = R.mipmap.ic_launcher;
        errorPic = R.mipmap.ic_launcher;
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
