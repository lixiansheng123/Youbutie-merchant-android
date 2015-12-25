package com.yuedong.youbutie_merchant_android.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.yuedong.youbutie_merchant_android.app.App;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    /**
     * 客户端数据库位置
     */
    public static final String DATABASE_FOLDER_NAME = Environment.getDataDirectory() + "/data/"
            + App.getInstance().getAppContext().getPackageName() + "/databases/";

    /**
     * 客户端缓存位置
     */
    public static final String SHAREPRE_CACHE_NAME = Environment.getDataDirectory() + "/data/"
            + App.getInstance().getAppContext().getPackageName() + "/";

    /**
     * sd卡的根目录
     */
    public static final String FILE_ROOT = Environment.getExternalStorageDirectory() + "/TranslateGo/";

    /**
     * 日志的目目录
     */
    public static final String LOG_ROOT = FILE_ROOT + "log";

    private FileUtils() {
    }

    /**
     * 取得文件的大�?
     *
     * @param file 本地文件
     * @return 文件的大�?如果找不到文件或读取失败则返�?1
     */
    public static long getFileSize(File file) {
        return (file.exists() && file.isFile() ? file.length() : -1L);
    }

    /**
     * �?��sdcard是否存在
     *
     * @return 如果sdcard存在返回true, 否则返回false
     * @deprecated 请直接使用{@link SystemManage#externalMemoryAvailable()}
     */
    public static boolean isSDCardExist() {
        return SystemUtils.externalMemoryAvailable();
    }

    /***
     * 计算文件夹大�?
     *
     * @param mFile 目录或文�?
     * @return 文件或目录的大小
     */
    public static long calculateFolderSize(File mFile) {
        // 判断文件是否存在
        if (!mFile.exists()) {
            return 0;
        }

        // 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大�?
        if (mFile.isDirectory()) {
            File[] files = mFile.listFiles();
            long size = 0;
            for (File f : files) {
                size += calculateFolderSize(f);
            }
            return size;
        } else {
            return mFile.length();
        }

    }

    /**
     * 计算�?��指定缓存文件夹的大小
     *
     * @return 缓存大小, 以M为单�?保留两位小数
     */
    public static String getAllcacheFolderSize() {
        long c1 = calculateFolderSize(new File(SHAREPRE_CACHE_NAME));
        long c2 = calculateFolderSize(new File(FILE_ROOT));
        // 单位是M
        double m = 1024.0 * 1024.0;
        double resoult = (c1 + c2) / m;
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(resoult);
    }

    /***
     * 清空指定文件�?文件
     *
     * @return 清空成功的话返回true, 否则返回false
     */

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] childs = file.listFiles();
            if (childs == null || childs.length <= 0) {
                // 空文件夹删掉
                return file.delete();
            } else {
                // 非空，遍历删除子文件
                for (int i = 0; i < childs.length; i++) {
                    deleteFile(childs[i]);
                }
                return deleteFile(file);
            }
        } else {
            return file.delete();
        }

    }

    /***
     * 清空指定文件夹下�?��文件
     *
     * @return 清空成功的话返回true, 否则返回false
     * @deprecated
     */
    public static boolean cleanDirectory(File directory) {
        return deleteFile(directory);
    }

    /**
     * 删除�?��缓存文件
     *
     * @return 如果缓存清空成功返回true, 否则返回false
     */
    public static boolean deleteAllCacheFiles() {
        return cleanDirectory(new File(SHAREPRE_CACHE_NAME)) && cleanDirectory(new File(FILE_ROOT));
    }

    /**
     * 获取asset目录中文件的�?
     *
     * @param fileName asserts目录中的文件�?
     * @return 文件�?如果读取失败则返回null
     */
    public static InputStream getAssetsInputStream(Context context, String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
        } catch (IOException e) {
        }
        return is;
    }

    /**
     * 获取asset目录中文件中的字符串
     *
     * @param fileName asserts目录中的文件�?
     * @return 文件内容, 如果读取失败则返回空字符�?
     */
    public static String getAssets(String fileName) {
        return stream2String(getAssetsInputStream(App.getInstance().getAppContext(), fileName));

    }

    /**
     * 判断路径(文件或目�?是否存在
     *
     * @param path 文件或目录路�?
     * @return 如果存在返回true, 否则返回false
     */
    public static boolean isFileExist(String path) {
        return new File(path).exists();
    }

    /**
     * 关闭输入输出�?
     *
     * @param c 输入输出�?
     */
    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 流转为string
     *
     * @param is
     * @return String [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类�?�?方法、类#成员]
     */
    public static String stream2String(InputStream is) {
        if (is == null) {
            return "";
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer strBuffer = new StringBuffer("");

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
        } catch (IOException e) {
        } finally {
            closeQuietly(reader);
        }
        return strBuffer.toString();
    }

    /**
     * 把输入流拷贝到输出流
     *
     * @param input  输入�?
     * @param output 输出�?
     * @return 拷贝的字节数，如果失败返�?1
     */
    public static int copyStream(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024];
        int count = 0;
        int n = 0;
        try {
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
        } catch (Exception ex) {
            return -1;
        }
        return count;
    }

    /**
     * <判断SD卡上的图片是否存�? <功能详细描述>
     *
     * @param filePath
     * @return boolean [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类�?�?方法、类#成员]
     */
    public static boolean isImageExist(String filePath) {
        if (!StringUtil.isEmpty(filePath)) {
            File file = new File(filePath);
            return file.exists() && file.length() > 0;
        }
        return false;
    }

    /**
     * 字节数组保存到文�?
     *
     * @param data
     * @param path [参数说明]
     * @return void [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类�?�?方法、类#成员]
     */
    public static void saveFile(byte[] data, String path) {
        File file = new File(new File(path).getParent());
        if (!file.exists()) {
            file.mkdirs();
        }
        BufferedOutputStream stream = null;
        try {
            file = new File(path);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(data);
        } catch (Exception e) {
        } finally {
            closeQuietly(stream);
        }
    }

    /**
     * 获取文件的字�?
     *
     * @param file
     * @return [参数说明]
     */
    public static byte[] getFileContent(File file) {
        FileInputStream fis = null;
        ByteArrayOutputStream output = null;
        try {
            fis = new FileInputStream(file);
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = fis.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            FileUtils.closeQuietly(output);
            FileUtils.closeQuietly(fis);
        }
        return null;
    }

    /**
     * 获取文件名
     */
    public static String getFileNameByMillis(String suffix) {
        return System.currentTimeMillis() + suffix;
    }

    /**
     * 获取文件名
     */
    public static String getFileNameByMd5(String url) {
        return generate(url);
    }

    /**
     * 获取原来的文件名
     */
    public static String getFileNameByOriginal(String filePath) {
        return filePath != null ? filePath.substring(filePath.lastIndexOf(File.separator) + 1)
                : getFileNameByMillis(".jpg");
    }

    private static String generate(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        L.d("MD5FileNameGenerator", cacheKey);
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 把对象写入文件 对象必须是序列化
     */
    public static void writeObjectToFile(Object obj, String AbsolutePath) {
        if (obj == null)
            return;
        File file = new File(AbsolutePath);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    /**
     * 从文件获取对象
     */
    public static Object readObjectFromFile(String AbsolutePath) {
        Object temp = null;
        File file = new File(AbsolutePath);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static File buildFile() {
        return new File(FILE_ROOT, getFileNameByMillis(".jpg"));
    }


    /**
     * @param context
     * @return 获取磁盘缓存路径
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File file = context.getExternalCacheDir();
            if (null != file) {
                cachePath = file.getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
            return cachePath;

        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

}
