package com.yuedong.youbutie_merchant_android.utils;


import android.os.Handler;
import android.os.Looper;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RequestYDHelper {
    private android.os.Handler mainHandler;
    private OnYDRequestListener onRequestPushListener;
    private String mAgent;
    private char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public void setOnYDRequestListener(OnYDRequestListener onRequestPushListener) {
        this.onRequestPushListener = onRequestPushListener;
    }

    private RequestYDHelper() {
        // 构建运行在主线程的handler
        mainHandler = new Handler(Looper.getMainLooper());
    }


    public void requestPush(String requestId, final String[] uids, final String title, final String content) {
        App.getInstance().getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = getHttpURLConnection(Constants.URL_UMENG_PUSH);
                    addHeadInfo(httpURLConnection);
                    JSONObject client = new JSONObject();
                    JSONObject all = new JSONObject();
                    JSONObject data = new JSONObject();
                    client.put("caller", Constants.CALLER);
                    data.put("uids", uids);
                    data.put("title", title);
                    data.put("content", content);
                    all.put("data", data);
                    all.put("client", client);
                    all.put("v", Constants.V);
                    String[] keys = new String[]{"uids", "title", "content"};
                    all.put("sign", sign(keys, data));
                    httpURLConnection.connect();
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(encodeBodyData(all));
                    outputStream.flush();
                    outputStream.close();
                    if (httpURLConnection.getResponseCode() == 200) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null)
                        httpURLConnection.disconnect();
                }
            }
        });
    }

    /**
     * 对写出的内容进行aes加密
     *
     * @param all
     * @return
     */
    private byte[] encodeBodyData(JSONObject all) {
        // 获取agent的后16位作为key
        byte key[] = getSecretKeyFromAgent2(mAgent);
        byte data[] = new byte[0];
        try {
            data = BmobAESUtils.base64Encode(aesEncode(all.toString(), key)).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }


    private String sign(String keys[], JSONObject data) {
        Arrays.sort(keys);
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.CALLER);
        for (int i = 0; i < keys.length; ++i) {
            try {
                builder.append(keys[i] + "=" + data.getString(keys[i]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        builder.append(Constants.SECRETKEY_PUSH_YD);
        String sign = md5(builder.toString()).toLowerCase();
        L.d("builder:" + builder.toString() + "======sign:" + sign);


        return sign;
    }

    public String md5(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte buf[] = digest.digest(data.getBytes("UTF-8"));
            char str[] = new char[buf.length * 2];
            int k = 0;
            for (int i = 0; i < buf.length; i++) {
                byte byte0 = buf[i];
                str[k++] = hex[byte0 >>> 4 & 0xf];
                str[k++] = hex[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addHeadInfo(HttpURLConnection httpURLConnection) {
        String agent = getAgent();
        httpURLConnection.setRequestProperty("Accept-Id", getAcceptId(agent));
        httpURLConnection.setRequestProperty("User-Agent", agent);
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
        httpURLConnection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
    }

    private String getAcceptId(String agent) {
        String appKey = Constants.APIKEY_PUSH_YD;
        byte key[] = getSecretKeyFromAgent1(agent);
        return Base64.encode(aesEncode(appKey, key));
    }

    private byte[] aesEncode(String data, byte[] key) {
        byte content[] = null;
        try {
            BmobAESUtils bmobAESUtils = new BmobAESUtils(new String(key, "UTF-8"));
            content = bmobAESUtils.encrypt(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    /*
     *User-Agent最后16位
     */
    private byte[] getSecretKeyFromAgent1(String agent) {

        try {
            byte agentBytes[] = agent.getBytes("UTF-8");
            byte key[] = new byte[16];
            for (int i = 0; i < 16; ++i) {
                key[i] = agentBytes[i + 1];
            }
            return key;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getSecretKeyFromAgent2(String agent) {
        try {
            byte agentBytes[] = agent.getBytes("UTF-8");
            byte key[] = new byte[16];
            int i = 16;
            int j = 0;
            int len = agentBytes.length;
            while (i > 0) {
                key[j++] = agentBytes[len - i];
                --i;
            }
            return key;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAgent() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        builder.append(String.valueOf(System.currentTimeMillis() / 1000));
        builder.append("Android");
        builder.append(Constants.V);
        return builder.toString();
    }

    private HttpURLConnection getHttpURLConnection(String url) throws Exception {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setReadTimeout(10 * 1000);
        httpURLConnection.setConnectTimeout(10 * 1000);
        return httpURLConnection;
    }


    public interface OnYDRequestListener {
        void onStart(String requestId);

        void onSucceed(String requestId, String json);

        void onFail(String requestId, Exception e);

        void onEnd(String requestId);
    }
}
