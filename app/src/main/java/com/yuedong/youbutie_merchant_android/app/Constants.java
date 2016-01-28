package com.yuedong.youbutie_merchant_android.app;

/**
 * APP常量
 */
public class Constants {
    //第三方开发key
    // bmob测试版key
//    public static final String APIKEY_BMOB = "643ed252af1996e42a33869912924261";
    // bmob数据版key
    public static final String APIKEY_BMOB = "d8cda36c4f3eae00e078f027e0264999";
    // 悦动推送apikey
    public static final String APIKEY_PUSH_YD = "android6ce073d5af79fa36986dadd3cd0e8a53";
    // 悦动推送secretKey
    public static final String SECRETKEY_PUSH_YD = "36986dadd3cd0e8a";
    // 百度推送apikey
    public static final String APIKEY_PUSH_BAIDU = "DmIK1PR9h4khYiO9SdUGfGdt";

    //------------------------------sp---------------------------
    public static final String SP_INVITE_REGIST = "sp_invite_regist";
    public static final String SP_INVITE_ADD_MEMBER = "sp_invite_add_member";
    //--------------------------标识---------------------------------
    public static final String KEY_TEXT = "key_text";
    public static final String KEY_TEXT2 = "key_text2";
    public static final String KEY_BEAN = "key_bean";
    public static final String KEY_LIST = "key_list";
    public static final String KEY_BOO = "key_boolean";
    public static final String KEY_INT = "key_int";
    public static final String KEY_INT2 = "key_int2";
    public static final String KEY_ACTION = "key_action";

    //-----tag
    public static final String ORDERMANAGER_FM_TAG = "tag_orderMnager_fm";

    //----------sp
    public static final String SP_GUIDE = "sp_guide";

    public static final int REQUESTCODE_ORDER_EVALUATE = 0x109;
    public static final int REQUESTCODE_COLLECTION = 0x110;
    public static final int REQUESTCODE_RECEIVE_ORDER = 0x111;
    public static final int REQUESTCODE_SELECT_CAR = 0x112;
    public static final int REQUESTCODE_INPUT_AD_TITLE = 0x113;
    public static final int REQUESTCODE_ADD_AD = 0x114;
    public static final int REQEUSTCODE_EDIT_SERVICE_LIST = 0x115;
    public static final int REQUESTCODE_MERCHANT_AD = 0x116;
    public static final int REQUESTCODE_MERCHANT_NAME = 0x117;
    public static final int REQUESTCODE_MERCHANT_LOCATION = 0x118;
    public static final int REQUESTCODE_MERCHANT_TEL = 0x119;
    public static final int RREQUESTCODE_CUSTOM_MERCHANT_SERVICE = 0x120;
    public static final int REQUESTCODE_SWIP_CODE = 0x121;
    public static final int REQUESTCODE_INPUT_MILEAGE = 0x122;

    public static final int RESULT_ORDER_EVALUATE = 0x209;
    public static final int RESULT_COLLECTION = 0x210;
    public static final int RESULT_RECEIVE_ORDER = 0x211;
    public static final int RESULT_SELECT_CAR = 0x212;
    public static final int RESULT_INPUT_AD_TITLE = 0x113;
    public static final int RESULT_ADD_AD = 0x214;
    public static final int RESULT_EDIT_SERVICE_LIST = 0x215;
    public static final int RESULT_MERCHANT_AD = 0x216;
    public static final int RESULT_MERCHANT_NAME = 0x217;
    public static final int RESULT_MERCHANT_LOCATION = 0x218;
    public static final int RESULT_MERCHANT_TEL = 0x219;
    public static final int RESULT_CUSTOM_MERCHANT_SERVICE = 0x220;
    public static final int RESULT_INPUT_MILEAGE = 0x221;


    // 属性动画的propertyName
    public static final String TRANSLATION_X = "translationX";
    public static final String TRANSLATION_Y = "translationY";
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    public static final String ALPHA = "alpha";
    public static final String ROTATION_X = "rotationX";
    public static final String ROTATION_Y = "rotationY";
    public static final String ROTATION = "rotation";

    //--tips
    public static final String TIPS_1 = "裁剪图片出现错误，请稍候重试";
    public static final String TIPS_2 = "访问出错 请检查您的网络 error:";

    //---info
    public static final String DESCRIPTOR = "com.umeng.share";
    public static final String CALLER = "youbutie_android";
    public static final String OK = "2000000";
    public static final String DESC_GET_SUCCEED = "车主%s已领走了%s个%s";
    /* 百度推送绑定好了通知 */
    public static final String ACTION_DB_PUSH_ONBIND_NOTIFY = "action_notify_db_push_bind";
    public static final String ACTION_NOTIFY = "action_notify";

    // 悦动接口版本
    public static final String V = "2.1";

    // 测试userId
    public static final String TEST_USER_ID = "5MWw666Q";

    //--url
    private static final String YD_URL = "http://youbutie.pkball.cn/";
    public static final String URL_UMENG_PUSH = YD_URL + "UmengPush/AndroidGroupcast";
    public static final String URL_GET_SECRETKEY = YD_URL + "SecretKey/GetSecretKey";
    public static final String URL_PUSH_SINGLE = YD_URL + "BaiduPush/pushMsgToSingle";
    public static final String URL_PUSH_GROUP = YD_URL + "BaiduPush/PushBatchUniMsg";

    // requestId
}


