package com.yuedong.youbutie_merchant_android.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * 图片特效类
 * 
 * @author Administrator
 *
 */
public class ImageEffectsUtils {
	private ImageEffectsUtils() {
	};

	/**
	 * 根据传进来的参数对图片进行特效处理
	 * 
	 * @param bit
	 *            图片
	 * @param mHue
	 *            色相
	 * @param mSaturation
	 *            饱和度
	 * @param mLum
	 *            亮度
	 * @return
	 */
	public static Bitmap ImageChange(Bitmap bit, float mHue, float mSaturation, float mLum) {
		// 因为bit是不可修改的所以创建一个
		Bitmap bitmap = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 创建一个画笔 同时让它抗锯齿
		// 色相
		ColorMatrix hueMatrix = new ColorMatrix();
		hueMatrix.setRotate(0, mHue); // 0是R
		hueMatrix.setRotate(1, mHue); // 1是G
		hueMatrix.setRotate(2, mHue); // 2是B
		// 饱和度
		ColorMatrix saturationMatrix = new ColorMatrix();
		saturationMatrix.setSaturation(mSaturation);
		// 亮度
		ColorMatrix lumMatrix = new ColorMatrix();
		// R G B A
		lumMatrix.setScale(mLum, mLum, mLum, 1);
		// 创建一个colorMatrix把所有特性整合一齐
		ColorMatrix imageMatrix = new ColorMatrix();
		imageMatrix.postConcat(hueMatrix);
		imageMatrix.postConcat(saturationMatrix);
		imageMatrix.postConcat(lumMatrix);
		paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
		canvas.drawBitmap(bit, 0, 0, paint);
		return bitmap;

	}

	/**
	 * 图片底片效果
	 * 
	 * @param bit
	 * @return
	 */
	public static Bitmap handlerImageNeative(Bitmap bit) {
		int width = bit.getWidth();
		int height = bit.getHeight();
		int color;
		int r, g, b, a;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

		// 保存像素到数组
		int[] oldPx = new int[width * height];
		int[] newPx = new int[width * height];
		// 存储像素的数组 起点偏移的量 多少个算一行 起点x坐标 起点y坐标 读取像素的长度 同左
		bit.getPixels(oldPx, 0, width, 0, 0, width, height);

		for (int i = 0; i < width * height; i++) {
			color = oldPx[i];
			// 提取分量
			r = Color.red(color);
			g = Color.green(color);
			b = Color.blue(color);
			a = Color.alpha(color);

			// 对分量做算法操作
			r = 255 - r;
			g = 255 - g;
			b = 255 - b;

			if (r > 255) {
				r = 255;
			} else if (r < 0) {
				r = 0;
			}
			if (g > 255) {
				g = 255;
			} else if (g < 0) {
				g = 0;
			}
			if (b > 255) {
				b = 255;
			} else if (b < 0) {
				b = 0;
			}
			// 把 新的颜色赋值給新的像素数组
			newPx[i] = Color.argb(a, r, g, b);
		}

		bitmap.setPixels(newPx, 0, width, 0, 0, width, height);

		return bitmap;
	}

	/**
	 * 怀旧效果
	 * 
	 * @param bit
	 * @return
	 */
	public static Bitmap handlerImagePixelOldPhoto(Bitmap bit) {
		int width = bit.getWidth();
		int height = bit.getHeight();
		int color;
		int r, g, b, a;
		int r1, g1, b1;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

		// 保存像素到数组
		int[] oldPx = new int[width * height];
		int[] newPx = new int[width * height];
		// 存储像素的数组 起点偏移的量 多少个算一行 起点x坐标 起点y坐标 读取像素的长度 同左
		bit.getPixels(oldPx, 0, width, 0, 0, width, height);

		for (int i = 0; i < width * height; i++) {
			color = oldPx[i];
			// 提取分量
			r = Color.red(color);
			g = Color.green(color);
			b = Color.blue(color);
			a = Color.alpha(color);

			// 对分量做算法操作
			r1 = (int) (0.393 * r + 0.769 * g + 0.189 * b);
			g1 = (int) (0.349 * r + 0.686 * g + 0.168 * b);
			b1 = (int) (0.272 * r + 0.534 * g + 0.131 * b);

			if (r1 > 255) {
				r1 = 255;
			} else if (r1 < 0) {
				r1 = 0;
			}
			if (g1 > 255) {
				g1 = 255;
			} else if (g1 < 0) {
				g1 = 0;
			}
			if (b1 > 255) {
				b1 = 255;
			} else if (b1 < 0) {
				b1 = 0;
			}
			// 把 新的颜色赋值給新的像素数组
			newPx[i] = Color.argb(a, r1, g1, b1);
		}

		bitmap.setPixels(newPx, 0, width, 0, 0, width, height);

		return bitmap;
	}

	/**
	 * 浮雕效果
	 * 
	 * @param bit
	 * @return
	 */
	public static Bitmap handlerImagePixelRelief(Bitmap bit) {
		int width = bit.getWidth();
		int height = bit.getHeight();
		int color, colorBefore;
		int r, g, b, a;
		int r1, g1, b1;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

		// 保存像素到数组
		int[] oldPx = new int[width * height];
		int[] newPx = new int[width * height];
		// 存储像素的数组 起点偏移的量 多少个算一行 起点x坐标 起点y坐标 读取像素的长度 同左
		bit.getPixels(oldPx, 0, width, 0, 0, width, height);

		for (int i = 1; i < width * height; i++) {

			colorBefore = oldPx[i - 1];
			r = Color.red(colorBefore);
			g = Color.green(colorBefore);
			b = Color.blue(colorBefore);
			a = Color.alpha(colorBefore);

			color = oldPx[i];
			// 提取分量
			r1 = Color.red(color);
			g1 = Color.red(color);
			b1 = Color.red(color);

			// 对分量做算法操作
			r = (r - r1 + 127);
			g = (g - g1 + 127);
			b = (b - b1 + 127);

			if (r > 255) {
				r = 255;
			} else if (r < 0) {
				r = 0;
			}
			if (g > 255) {
				g = 255;
			} else if (g < 0) {
				g = 0;
			}
			if (b > 255) {
				b = 255;
			} else if (b < 0) {
				b = 0;
			}
			// 把 新的颜色赋值給新的像素数组
			newPx[i] = Color.argb(a, r, g, b);
		}

		bitmap.setPixels(newPx, 0, width, 0, 0, width, height);

		return bitmap;
	}
}
