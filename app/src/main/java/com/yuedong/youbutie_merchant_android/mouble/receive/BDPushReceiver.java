package com.yuedong.youbutie_merchant_android.mouble.receive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.yuedong.youbutie_merchant_android.LoginActivity;
import com.yuedong.youbutie_merchant_android.MainActivity;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.SerializableMap;
import com.yuedong.youbutie_merchant_android.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 */
public class BDPushReceiver extends PushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = "BDPushReceiver";
    private static final int TYPE_NOTIFY_ONBIND = 0x001;
    private static final int TYPE_RECEIVE_MESSAGE = 0x002;
    private static final int TYPE_NOTIFY_CLICK = 0x003;
    public static final String ERROR_CODE = "errorCode";
    public static final String APP_ID = "appid";
    public static final String USER_ID = "userId";
    public static final String CHANNEL_ID = "channelId";
    public static final String REQUEST_ID = "requestId";
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String CUSTOM_CONTENT = "custom_content";

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        L.d("onBind-errorCode:" + errorCode);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(ERROR_CODE, errorCode);
        data.put(USER_ID, userId);
        data.put(APP_ID, appid);
        data.put(CHANNEL_ID, channelId);
        data.put(REQUEST_ID, requestId);
        notify(context, data, TYPE_NOTIFY_ONBIND);
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        L.d(TAG + messageString);

        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        Map<String, Object> data = getMap(title, description, customContentString);
        notify(context, data, TYPE_NOTIFY_CLICK);

    }

    /**
     * 接收通知到达的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        L.d(TAG + notifyString);

        Map<String, Object> data = getMap(title, description, customContentString);
        notify(context, data, TYPE_RECEIVE_MESSAGE);
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param sucessTags 设置成功的tag
     * @param failTags   设置失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        L.d(TAG + responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
    }

    /**
     * delTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param sucessTags 成功删除的tag
     * @param failTags   删除失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        L.d(TAG + responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
    }

    /**
     * listTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示列举tag成功；非0表示失败。
     * @param tags      当前应用设置的所有tag。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        L.d(TAG + responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        L.d(TAG + responseString);

        if (errorCode == 0) {
            // 解绑定成功
            Log.d(TAG, "解绑成功");
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
    }

    private void notify(Context context, Map<String, Object> data, int type) {
        if (type == TYPE_NOTIFY_ONBIND) {
            // 发送广播通知提交推送标识符
            if (!App.getInstance().appIsStart) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                SerializableMap serializableMap = new SerializableMap();
                serializableMap.setMap(data);
                bundle.putSerializable(Constants.KEY_BEAN, serializableMap);
                intent.putExtras(bundle);
                intent.setAction(Constants.ACTION_DB_PUSH_ONBIND_NOTIFY);
                context.sendBroadcast(intent);
            }
        } else if (type == TYPE_RECEIVE_MESSAGE) {
            String json = (String) data.get(CUSTOM_CONTENT);
            SerializableMap serializableMap = new SerializableMap();
            serializableMap.setMap(data);
            Intent intent = new Intent();
            intent.putExtra(Constants.KEY_BEAN, serializableMap);
            intent.setAction(Constants.ACTION_NOTIFY);
            context.sendBroadcast(intent);
        } else if (type == TYPE_NOTIFY_CLICK) {
            Class<? extends Activity> cls = null;
            if (App.getInstance().isLogin())
                cls = MainActivity.class;
            else
                cls = LoginActivity.class;
            Intent intent = new Intent(context, cls);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            L.d(TAG + "====start activity");
        }
    }


    @NonNull
    private Map<String, Object> getMap(String title, String description, String customContentString) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(TITLE, title);
        data.put(DESC, description);
        data.put(CUSTOM_CONTENT, customContentString);
        return data;
    }
}
