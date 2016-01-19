package com.yuedong.youbutie_merchant_android.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.utils.ActivityTaskUtils;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义系统的Crash捕捉类，用Toast替换系统的对话框 将软件版本信息，设备信息，出错信息保存在sd卡中，你可以上传到服务器中
 */
public class CustomCrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CustomCrashHandler";
    private Context mContext;
    private static CustomCrashHandler mInstance = new CustomCrashHandler();

    private CustomCrashHandler() {
    }

    /**
     * 单例模式，保证只有一个CustomCrashHandler实例存在
     *
     * @return
     */
    public static CustomCrashHandler getInstance() {
        return mInstance;
    }

    /**
     * 异常发生时，系统回调的函数，我们在这里处理一些操作
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 将一些信息保存到SDcard中
        savaInfoToSD(mContext, ex);

        // 提示用户程序即将退出
        showToast(mContext, "应用异常 即将退出..");
        try {
            thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 完美退出程序方法
        ActivityTaskUtils.getInstance().exit();

    }

    /**
     * 为我们的应用程序设置自定义Crash处理
     */
    public void setCustomCrashHanler(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 显示提示信息，需要在线程中显示Toast
     *
     * @param context
     * @param msg
     */
    private void showToast(final Context context, final String msg) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     * @param context
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);

        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);

        return map;
    }

    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();

        Log.e(TAG, mStringWriter.toString());
        return mStringWriter.toString();
    }

    /**
     * 保存获取的 软件信息，设备信息和出错信息保存在SDcard中
     *
     * @param context
     * @param ex
     * @return
     */
    private String savaInfoToSD(Context context, Throwable ex) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : obtainSimpleInfo(context).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        sb.append(obtainExceptionInfo(ex));

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(Config.DIR_ERROR_LOG);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                fileName = dir.toString() + File.separator + DateUtils.formatDate(new Date(), "yyyy-MM-dd_HH_mm_ss")
                        + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return fileName;

    }

}