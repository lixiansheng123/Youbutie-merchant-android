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

//
//    // 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
//    private static UMSocialService mController = UMServiceFactory
//            .getUMSocialService(Constants.DESCRIPTOR);

    // 高德网络定位
//    public static void gdNetWorkLocation(Activity context, GDNetworkLocation.IGDlocationCallbcack callbcack) {
//        GDNetworkLocation.getInstance(context).startLocation(callbcack);
//    }

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


//    /**
//     * 授权。如果授权成功，则获取用户信息</br>
//     */
//    public static void login(final Context context, final SHARE_MEDIA platform, final Callback callback) {
//        mController.doOauthVerify(context, platform, new SocializeListeners.UMAuthListener() {
//
//            @Override
//            public void onStart(SHARE_MEDIA platform) {
//            }
//
//            @Override
//            public void onError(SocializeException e, SHARE_MEDIA platform) {
//                Toast.makeText(context, "授权失败...", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onComplete(Bundle value, SHARE_MEDIA platform) {
//                String uid = value.getString("uid");
//                L.i("授权Bundler----------------->" + value.toString());
//                if (!TextUtils.isEmpty(uid)) {
//                    getUserInfo(context, value, platform, callback);
//                } else {
//                    Toast.makeText(context, "授权失败...", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA platform) {
//            }
//        });
//    }
//
//    /**
//     * 获取授权平台的用户信息</br>
//     */
//    private static void getUserInfo(final Context context, final Bundle value, SHARE_MEDIA platform, final Callback callback) {
//        mController.getPlatformInfo(context, platform, new SocializeListeners.UMDataListener() {
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onComplete(int status, Map<String, Object> info) {
//                if (status == StatusCode.ST_CODE_SUCCESSED) {
//                    String openId = "1231231";
//                    String access_token = "12312312";
//                    String expires_in = "31232132131";
//                    try {
//                        expires_in = (String) value.get("expires_in");
//                        access_token = (String) value.get("access_token");
//                        openId = (String) value.get("openid");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    info.put("expires_in", expires_in);
//                    info.put("openid", openId);
//                    info.put("access_token", access_token);
//                    L.i("授权信息infos:------->" + info.toString());
//                    callback.callback(info);
//                } else {
//                    T.showLong(context, "获取用户信息失败");
//                }
//            }
//        });
//    }
//
//    /**
//     * 打开自定义的友盟分享
//     *
//     * @param context
//     * @return
//     */
//    public static UMSocialService openCustomUMengShare(Activity context) {
//        // 添加微信平台
//        addWXPlatform(context);
//        // 添加QQ平台
//        addQQQZonePlatform(context);
//        // 添加短信平台
//        addSMS();
//        // 打开分享面板
//        mController.openShare(context, false);
//        mController.setShareContent("测试分享");
//        // 分享web页面
////        mController.setShareMedia(new UMWebPage())
//        /* / 设置分享内容
//mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
//// 设置分享图片, 参数2为图片的url地址
//mController.setShareMedia(new UMImage(getActivity(),
//                                      "http://www.umeng.com/images/pic/banner_module_social.png"));
//// 设置分享图片，参数2为本地图片的资源引用
////mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
//// 设置分享图片，参数2为本地图片的路径(绝对路径)
////mController.setShareMedia(new UMImage(getActivity(),
////                                BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));
//
//// 设置分享音乐
////UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
////uMusic.setAuthor("GuGu");
////uMusic.setTitle("天籁之音");
//// 设置音乐缩略图
////uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
////mController.setShareMedia(uMusic);
//
//// 设置分享视频
////UMVideo umVideo = new UMVideo(
////          "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
//// 设置视频缩略图
////umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
////umVideo.setTitle("友盟社会化分享!");
////mController.setShareMedia(umVideo);*/
//        return mController;
//    }
//
//
//    /**
//     * @return
//     * @功能描述 : 添加微信平台分享
//     */
//    private static void addWXPlatform(Activity context) {
//        // 注意：在微信授权的时候，必须传递appSecret
//        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//        String appId = Constants.APIKEY_WX;
//        String appSecret = Constants.APPSECRET_WX;
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
//        wxHandler.addToSocialSDK();
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//    }
//
//    /**
//     * @return
//     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
//     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
//     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
//     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
//     */
//    private static void addQQQZonePlatform(Activity context) {
//        String appId = "100424468";
//        String appKey = "c7394704798a158208a74ab60104f0ba";
//        // 添加QQ支持, 并且设置QQ分享内容的target url
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context,
//                appId, appKey);
//        qqSsoHandler.addToSocialSDK();
//        // 添加QZone平台
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, appId, appKey);
//        qZoneSsoHandler.addToSocialSDK();
//    }
//
//    /**
//     * 添加短信平台</br>
//     */
//    private static void addSMS() {
//        // 添加短信
//        SmsHandler smsHandler = new SmsHandler();
//        smsHandler.addToSocialSDK();
//    }




}
