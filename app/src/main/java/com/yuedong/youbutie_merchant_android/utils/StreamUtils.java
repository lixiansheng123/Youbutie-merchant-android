package com.yuedong.youbutie_merchant_android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	/**
	 * 把输入流转化成字符串
	 * 
	 * @throws IOException
	 */
	public static String conversionInputStreamToString(InputStream in)
			throws IOException {
		String str = "";
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = in.read(buffer)) != -1) {
			byteOut.write(buffer, 0, len);
		}
		if (in != null && byteOut != null) {
			str = new String(byteOut.toByteArray(), "utf-8");
			in.close();
			byteOut.close();
		}

		return str;

	}
}
