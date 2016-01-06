package com.yuedong.youbutie_merchant_android.utils;


import android.os.Handler;
import android.os.Looper;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class RequestYDHelper {
    private android.os.Handler mainHandler;
    private OnYDRequestListener onRequestYDListener;
    private HttpURLConnection mHttpURLConnection;
    private String mAgent;
    private String mSecretKey;
    private static final String UTF_8 = "UTF-8";
    private char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private int action; // 0获取悦动Secretkey  1请求推送

    public void setOnYDRequestListener(OnYDRequestListener onRequestPushListener) {
        this.onRequestYDListener = onRequestPushListener;
    }

    public void setAppSecretkey(String secretKey) {
        this.mSecretKey = secretKey;
    }

    public RequestYDHelper() {
        // 构建运行在主线程的handler
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 获取secretkey
     */
    public void getSecretkey() {
        action = 0;
        App.getInstance().getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mHttpURLConnection = getHttpURLConnection(Constants.URL_GET_SECRETKEY);
                    addHeadInfo();
                    JSONObject all = new JSONObject();
                    JSONObject data = new JSONObject();
                    JSONObject client = new JSONObject();
                    client.put("caller", Constants.CALLER);
                    data.put("appKey", Constants.APIKEY_PUSH_YD);
                    all.put("data", data);
                    all.put("client", client);
                    String[] keys = new String[]{"appKey"};
                    all.put("sign", sign(keys, data));
                    String result = writeAndResult(all);
                    responseSucceed(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    responseFail(e);
                } finally {
                    disconnect();
                }

            }
        });
    }

    /**
     * 请求推送
     *
     * @param requestId
     * @param uids
     * @param title
     * @param content
     */
    public void requestPush(final String requestId, final String[] uids, final String title, final String content) {

        if (mSecretKey == null) {
            T.showShort(App.getInstance().getAppContext(), "SecretKey为null");
            return;
        }
        action = 1;
        StringBuilder sb = new StringBuilder();
        for (String uid : uids) {
            sb.append(uid + ",");
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        final String uidsStr = sb.toString();
        App.getInstance().getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mHttpURLConnection = getHttpURLConnection(Constants.URL_UMENG_PUSH);
                    addHeadInfo();
                    JSONObject client = new JSONObject();
                    JSONObject all = new JSONObject();
                    JSONObject data = new JSONObject();
                    client.put("caller", Constants.CALLER);
                    data.put("uids", uidsStr);
                    data.put("title", title);
                    data.put("content", content);
                    all.put("data", data);
                    all.put("client", client);
                    all.put("v", Constants.V);
                    String[] keys = new String[]{"uids", "title", "content"};
                    all.put("sign", sign(keys, data));
                    String result = writeAndResult(all);
                    responseSucceed(result);
                } catch (final Exception e) {
                    e.printStackTrace();
                    responseFail(e);
                } finally {
                    disconnect();
                }
            }
        });
    }

    private void disconnect() {
        if (mHttpURLConnection != null)
            mHttpURLConnection.disconnect();
    }

    private void responseSucceed(final String result) {
        if (onRequestYDListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onRequestYDListener.onSucceed(result);
                    onRequestYDListener.onEnd();
                }
            });
        }
    }

    private void responseFail(final Exception error) {
        if (onRequestYDListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onRequestYDListener.onFail(error);
                    onRequestYDListener.onEnd();
                }
            });
        }
    }


    private String writeAndResult(JSONObject all) throws IOException {
        mHttpURLConnection.connect();
        OutputStream outputStream = mHttpURLConnection.getOutputStream();
        outputStream.write(encodeBodyData(all));
        outputStream.flush();
        outputStream.close();
        InputStream inputStream = mHttpURLConnection.getInputStream();
        byte[] buffer = new byte[8192];
        int len = 0;
        int count = 0;
        int r = 0;
        while (true) {
            r = inputStream.read(buffer, count, 8192 - count);
            if (r == -1) {
                break;
            }
            len += r;
            count += r;
        }
        byte bytes[] = new byte[len];
        System.arraycopy(buffer, 0, bytes, 0, len);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        InputStream in = new GZIPInputStream(bis);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in), 2000);
        StringBuilder builder = new StringBuilder();
        String l;
        for (l = reader.readLine(); l != null; l = reader.readLine()) {
            builder.append(l);
        }
        String result = builder.toString();
        L.d("writeAndResult-服务器返回结果：" + result);
        result = decodeData(result);
        L.d("writeAndResult-服务器返回解密结果：" + result);
        IoUtils.closeIo(inputStream);
        return result;
    }

    /**
     * 对数据进行解密
     *
     * @param data
     * @return
     */
    public String decodeData(String data) {
        String d = "";
//        d=new String(aesDecode(Base64.decode(data, Base64.DEFAULT)));
        if (data != null) {
            byte buffer[];
            try {
                buffer = BmobAESUtils.base64Decode(data);
                if (buffer == null) {
                    L.e("buffer == null");
                } else {
                    L.d("buffer length " + buffer.length);
                }
                buffer = aesDecode(buffer);
                if (buffer != null)
                    d = new String(buffer);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return d;
    }


    private byte[] aesDecode(byte[] data) {
        String key = null;
        if (action == 0)
            key = getKeyFromResponseID();
        else
            key = mSecretKey;
        BmobAESUtils aes = new BmobAESUtils(key);
        return aes.decrypt(data);

    }

    private String getKeyFromResponseID() {
        Map<String, List<String>> map = mHttpURLConnection.getHeaderFields();
        String reponseId = map.get("Response-Id").get(0);
        try {
            byte rbytes[] = reponseId.getBytes("UTF-8");
            int i = 16;
            int j = 0;
            int len = rbytes.length;
            byte[] decodeKey = new byte[16];
            while (i > 0) {
                decodeKey[j++] = rbytes[len - i];
                --i;
            }
            return new String(decodeKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对写出的内容进行aes加密
     *
     * @param all
     * @return
     */
    private byte[] encodeBodyData(JSONObject all) throws UnsupportedEncodingException {
        L.d("请求参数:" + all.toString());
        // 获取agent的后16位作为key
        byte[] key = null;
        if (action == 0)
            key = getSecretKeyFromAgent2(mAgent);
        else if (action == 1)
            key = mSecretKey.getBytes(UTF_8);
        byte[] aesEncrypt = aesEncode(all.toString(), key);
        return BmobAESUtils.base64Encode(aesEncrypt).getBytes(UTF_8);
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
        String sign = null;
        if (action == 0) {
            builder.append(mAgent.substring(mAgent.length() - 16, mAgent.length()));
        } else if (action == 1) {
            builder.append(mSecretKey);
        }
        sign = md5(builder.toString()).toLowerCase();
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

    private void addHeadInfo() {
        mAgent = getAgent();
        mHttpURLConnection.setRequestProperty("Accept-Id", getAcceptId(mAgent));
        mHttpURLConnection.setRequestProperty("User-Agent", mAgent);
        mHttpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
        mHttpURLConnection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
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
     *User-Agent前第二位开始取16位
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

    /*
        *User-Agent后16位
        */
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
        void onStart();

        void onSucceed(String json);

        void onFail(Exception e);

        void onEnd();
    }
}
