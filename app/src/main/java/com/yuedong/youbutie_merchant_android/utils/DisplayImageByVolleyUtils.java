package com.yuedong.youbutie_merchant_android.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;

/**
 * 图片下载工具类
 */
public class DisplayImageByVolleyUtils {
    private static ImageLoader imageloader = new ImageLoader(Volley.newRequestQueue(App.getInstance().getAppContext()), new BitmapCache());

    public static void loadImage(String url, ImageView imageView, int defaultPic, int errorPic) {
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, defaultPic, errorPic);
        imageloader.get(url, imageListener);
    }


    public static final ImageLoader IMAGELOADER = new ImageLoader(Volley.newRequestQueue(App.getInstance().getAppContext()),
            new BitmapCache());


    public static void loadImage(NetworkImageView niv, String url) {
        niv.setErrorImageResId(R.mipmap.ic_launcher);
        niv.setDefaultImageResId(R.mipmap.ic_launcher);
        niv.setImageUrl(url, IMAGELOADER);
    }

    public static void loadImage(NetworkImageView niv, String url, int defaultImage) {
        niv.setDefaultImageResId(defaultImage);
        niv.setErrorImageResId(defaultImage);
        niv.setImageUrl(url, IMAGELOADER);
    }

    public static void loadImage(NetworkImageView niv, String url, int defaultImage, int errorImage) {
        niv.setDefaultImageResId(defaultImage);
        niv.setErrorImageResId(errorImage);
        niv.setImageUrl(url, IMAGELOADER);
    }

    static final class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            // 获取程序运行最大内存
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            // 初始化图片内存缓存
            mCache = new LruCache<String, Bitmap>(maxMemory / 8) {
                // 重写sizeof方法 让其知道图片的大小
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    if (value == null)
                        return 0;
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            if (bitmap != null) {
                mCache.put(url, bitmap);
            }
        }
    }

}
