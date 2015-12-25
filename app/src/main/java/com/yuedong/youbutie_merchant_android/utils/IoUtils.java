package com.yuedong.youbutie_merchant_android.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2015/11/30.
 */
public class IoUtils {

    public static void closeIo(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
