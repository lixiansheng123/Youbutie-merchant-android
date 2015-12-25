package com.yuedong.youbutie_merchant_android.utils;

public class ArrayUtils {
	private ArrayUtils() {
	}// 因为里面的方法的都是静态的所以不必new对象而浪费内存 直接调用就可以了

	/**
	 * 获取数组最大值
	 * 
	 * @param arr 整形数组
	 * @return 该数组中的最大值
	 */
	public static int getMax(int[] arr) {
		int maxIndex = 0;
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > arr[maxIndex]) {
				maxIndex = i;
			}
		}
		return arr[maxIndex];

	}

	/**
	 * 数组中的数据从小到大进行排序
	 * 
	 * @param arr 整形数组
	 */
	public static void selectSort(int[] arr) {
		for (int x = 0; x < arr.length - 1; x++) {
			for (int i = x + 1; i < arr.length; i++) {
				if (arr[x] > arr[i]) {
					swap(arr, x, i);
				}
			}
		}
	}

	private static void swap(int[] arr, int x, int i) {
		int temp = arr[x];
		arr[x] = arr[i];
		arr[i] = temp;
	}

	/**
	 * 根据值获取数组的下标
	 * 
	 * @param arr
	 *            整形数组
	 * @param value
	 *            数组里面的成员
	 * @return value 对应的下标
	 */
	public static int getIndex(int[] arr, int value) {
		int index = -1;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == value) {
				index = i;
			}
		}
		return index;

	}
}
