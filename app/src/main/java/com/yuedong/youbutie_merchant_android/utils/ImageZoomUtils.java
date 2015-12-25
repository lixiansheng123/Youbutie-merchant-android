package com.yuedong.youbutie_merchant_android.utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;

/** 图片压缩工具类 */
public class ImageZoomUtils {
	private ImageZoomUtils() {
	};

	/**
	 * 计算BimtapFactory.Options 的 insampleSize值
	 * 
	 * @param options
	 *            BimtapFactory.Options对象
	 * @param reqWidth
	 *            请求的图片宽度
	 * @param reqHeight
	 *            请求的图片高度
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = Math.min(heightRatio, widthRatio);
		}
		if (inSampleSize <= 0)
			inSampleSize = 1;

		return inSampleSize;
	}

	/**
	 * 从drawalbe目录下获取的资源文件按指定的宽高去压缩像素点 使用这样的方式获取bitmap是因为直接获取会为bitmap分配内存
	 * 如果图片过大很容易发生OOM
	 * 
	 * @param res
	 *            一般传getResources();
	 * @param resId
	 *            资源文件id
	 * @param reqWidth
	 *            要压缩到的图片宽度
	 * @param reqHeight
	 *            要压缩到的图片高度
	 * @return 压缩处理后的位图
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		return getBitmap(res, resId, reqWidth, reqHeight, "");
	}

	private static Bitmap getBitmap(Resources res, int resId, int reqWidth, int reqHeight, String path) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		if (resId != -1) {
			// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
			BitmapFactory.decodeResource(res, resId, options);
			// 调用上面定义的方法计算inSampleSize值
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
			// 使用获取到的inSampleSize值再次解析图片
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeResource(res, resId, options);
		} else {
			// 这样就可以在options获取到outWidth outHeight
			BitmapFactory.decodeFile(path, options);
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
			// 使用获取到的inSampleSize值再次解析图片
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(path, options);
		}
	}

	/**
	 * 从path中获取到指定图片宽高压缩后的位图
	 * 
	 * @param path
	 *            图片所在路径
	 * @param reqWidth
	 *            指定图片宽
	 * @param reqHeight
	 *            指定图片高
	 * @return
	 */
	public static Bitmap decodeSampleBitmapFromPath(String path, int reqWidth, int reqHeight) {

		return getBitmap(null, -1, reqWidth, reqHeight, path);

	}

	/*
	 * public static Bitmap compressImage(Bitmap image, int maxSize) {
	 * 
	 * ByteArrayOutputStream baos = new ByteArrayOutputStream();
	 * image.compress(Bitmap.CompressFormat.JPEG, 100, baos); int options = 90;
	 * boolean isOk = false; while (baos.toByteArray().length / 1024 > maxSize
	 * && !isOk) { if (options <= 0) { options = 10; isOk = true; }
	 * baos.reset(); image.compress(Bitmap.CompressFormat.JPEG, options, baos);
	 * options -= 10; }
	 * 
	 * byte[] byteArray = baos.toByteArray(); int len = byteArray.length; Bitmap
	 * bitmap = BitmapFactory.decodeByteArray(byteArray, 0, len); return bitmap;
	 * }
	 */

	/**
	 * 压缩图片并放置到另一个文件夹中 用于上传 上传完把产生的文件夹删除就好
	 * 
	 * @param filePath
	 *            文件原始路径
	 * @param fileDir
	 *            文件夹
	 * @param maxSize
	 *            限制大小 如100单位为kb
	 * @return reutrn 生成文件的绝对路径
	 */
	public static String compressImageToFile(String filePath, String fileDir, int maxSize) {
		String scaleImagePath = "";
		FileOutputStream fos = null;
		ByteArrayOutputStream baos = null;
		Bitmap scaleCompressImage = null;
		try {
			scaleCompressImage = decodeSampleBitmapFromPath(filePath, 400, 800);
			baos = new ByteArrayOutputStream();
			scaleCompressImage.compress(CompressFormat.JPEG, 100, baos);
			int options = 90;
			if (baos.toByteArray().length / 1024 > maxSize) {
				baos.reset();
				scaleCompressImage.compress(CompressFormat.JPEG, options, baos);
				options -= 10;
			}
			File dir = new File(fileDir);
			File file = new File(dir, FileUtils.getFileNameByOriginal(filePath));
			if (!dir.exists())
				dir.mkdirs();
			fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			scaleImagePath = file.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return scaleImagePath;
	}

	public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// options.inPurgeable = true;
		// options.inInputShareable = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
	}

	@SuppressLint("NewApi")
	public static long getBitmapsize(Bitmap bitmap) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	public static Bitmap bitmapZoom(int maxSize, Bitmap bitMap) {
		Bitmap result = null;
		// 图片允许最大空间 单位：KB
		// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitMap.compress(CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			result = scaleBitmap(bitMap, bitMap.getWidth() / Math.sqrt(i), bitMap.getHeight() / Math.sqrt(i));
		}
		return result;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) Math.round(newWidth), (int) Math.round(newHeight),
				matrix, true);
		return bitmap;
	}

}
