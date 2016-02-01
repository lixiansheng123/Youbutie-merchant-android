package com.yuedong.youbutie_merchant_android.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.yuedong.youbutie_merchant_android.app.Config;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;

/**
 * <p>
 * 第三方AIP的一些方法
 * </p>
 *
 * @author 俊鹏
 */
public class ApiUtils {
    private static String appName = "油补贴";
    // 高德客户端包名
    private static final String gdPackage = "com.autonavi.minimap";

    /**
     * 高德导航 跳转到高德APP上进行导航
     *
     * @param styleType 0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵
     */
    public static void gdGps(Context context, String lat, String lon, String poiName, String styleType) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            StringBuilder sb = new StringBuilder();
            sb.append("androidamap://navi?sourceApplication=").append(appName)//
                    .append("&poiname=")//
                    .append(poiName)//
                    .append("&lat=")//
                    .append(lat)//
                    .append("&lon=")//
                    .append(lon)//
                    .append("&dev=1")//
                    .append("&style=")//
                    .append(styleType);
            intent.setData(Uri.parse(sb.toString()));
            intent.setPackage(gdPackage);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            T.showLong(context, "导航失败请安装高德地图客户端");
        }

    }

    /**
     * 打开手机带有导航功能的APP
     *
     * @param context
     * @param latitude
     * @param longitude
     */
    public static void gps(Context context, double latitude, double longitude) {
        Uri uri = Uri.parse("geo:" + latitude + "," + longitude);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(it);
        } catch (ActivityNotFoundException e) {
            T.showLong(context, "你尚未安装导航软件，请先安装导航软件");
        }
    }

    /**
     * 上传单个文件  先进行压缩
     *
     * @param context
     * @param filePath
     * @param listener
     */
    public static void uplodaFile(Context context, String filePath, final com.yuedong.youbutie_merchant_android.model.bmob.listener.UploadListener listener) {
        if (StringUtil.isNotEmpty(filePath) && new File(filePath).exists()) {
            listener.onStart();
            String compressFile = ImageZoomUtils.compressImageToFile(filePath, Config.DIR_UPLOAD_PIC, 100);
            BmobProFile.getInstance(context).upload(compressFile, new UploadListener() {
                @Override
                public void onSuccess(String s, String s1, BmobFile bmobFile) {
                    listener.onSuccess(s, s1, bmobFile);
                    listener.onEnd();
                }

                @Override
                public void onProgress(int i) {
                    listener.onProgress(i);
                }

                @Override
                public void onError(int i, String s) {
                    listener.onError(i, s);
                    listener.onEnd();
                }
            });
        } else {
            T.showShort(context, "上传非法文件");
        }
    }

}
