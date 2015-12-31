package com.yuedong.youbutie_merchant_android.app;


import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.utils.FileUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.io.File;

/**
 * Created by Administrator on 2015/11/27.
 *
 * @author 俊鹏
 */
public class Config {
    // 去标题去底部去状态栏高度1529
    // 去标题去状态栏高度1691

    // 状态栏背景默认颜色
    public static final int STATUSBAR_COLOR = R.color.greyf0eeeb;
    // 设计的尺寸
    public static final int DESIGN_W = 1080;
    public static final int DESIGN_H = 1920;
    // 默认我的位置
    public static final Double DEFAULT_LATITUDE = 23.1304880000;
    public static final Double DEFAULT_LONGITUDE = 113.3684990000;
    public static final int TITLE_HEIGHT = ViewUtils.getViewDisplaySize(159, ViewUtils.ViewEnum.H);
    /**
     * 一页数据的数目
     */
    public static final int PAGER_SIZE = 10;
    /**
     * 一页评论的数目
     */
    public static final int COMMENT_PAGER_SIZE = 2;

    // 正则判断为数字
    public static final String REGEX_NUM = "^[0-9]*$";
    // 判断手机号码正则
    public static final String REGEX_TEL = "[1]\\d{10}";
    // 判断手机号码正则2 较为严谨的
//    public static final String REGEX2_TEL = "[1][358]\\\\d{9}";

    // sp
    public static final String SP_NAME_USER = "sp_file_user";

    // file--
    public static final String BASE_CACHE_DIR = FileUtils.getDiskCacheDir(App.getInstance().getAppContext()) + File.separator + "cache" + File.separator;
    public static final String DIR_CROP_PIC = BASE_CACHE_DIR + "cropPic";
    public static final String DIR_UPLOAD_PIC = BASE_CACHE_DIR + "uploadPic";


    // pic
    public static final int load_default_pic = R.mipmap.ic_launcher;
    public static final int load_error_pic = R.mipmap.ic_launcher;

    static {
        createDir(DIR_CROP_PIC);
        createDir(DIR_UPLOAD_PIC);
    }

    public static void createDir(String dir) {
        File dirF = new File(dir);
        if (!dirF.exists())
            dirF.mkdirs();
    }
}
