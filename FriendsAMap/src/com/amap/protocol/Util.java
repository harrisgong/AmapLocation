package com.amap.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.regex.Pattern;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Base64;
import android.widget.Toast;

public class Util {


	/**
	 * 按照指定格式将long日期装换为字符串
	 * 
	 * @param longDate
	 *            �?1970-01-01至今的毫秒数
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String formatDate(Long longDate, String pattern) {
		Date date = new Date(longDate);
		return formatDate(date, pattern);
	}

	/**
	 * 使用用户格式格式化日�?
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * bitmap 转byte[]
	 * 
	 * @param bitmap
	 * @return
	 */
	public static byte[] getBitmapByte(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	public static Bitmap bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	// byte[]转换成Drawable
	public static Drawable bytes2Drawable(byte[] b) {
		Bitmap bitmap = bytes2Bimap(b);
		return bitmap2Drawable(bitmap);
	}

	// Bitmap转换成Drawable
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		Drawable d = (Drawable) bd;
		return d;
	}

	/**
	 * 使用用户格式格式化日�?
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * 获取软件版本�?
	 * 
	 * @return V${viesion}
	 */
	public static String getVersion(Activity activity) {
		String version = "0.0.0.0";

		PackageManager packageManager = activity.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					activity.getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "V" + version;
	}


	/**
	 * 判断是否有网络连�?
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static String assembleParams(Map<String, String> params) {
		if (params == null || params.size() == 0) {
			return null;
		}
		StringBuffer sBuffer = new StringBuffer();
		try {
			boolean firstParamFlag = true;

			for (Entry<String, String> entry : params.entrySet()) {
				if (firstParamFlag) {
					firstParamFlag = false;
					sBuffer.append(entry.getKey()).append("=")
							.append(entry.getValue());
				} else {
					sBuffer.append("&").append(entry.getKey()).append("=")
							.append(entry.getValue());
				}

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return sBuffer.toString();
	}

	/**
	 * 对string串进行排�?
	 * 
	 * @param str
	 * @return
	 */
	public static String sortParams(String str) {
		try {
			if (str == null) {
				return null;
			}
			String[] params = str.split("&");
			Arrays.sort(params);
			StringBuffer sortResult = new StringBuffer();
			for (String param : params) {
				sortResult.append(param);
				sortResult.append("&");
			}
			String result = sortResult.toString();
			if (result.length() > 1) {
				result = (String) result.subSequence(0, (result.length() - 1));
				return result;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return str;
	}

	static byte[] coreGZip(byte[] data) {
		try {
			return gzip(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	public static byte[] gZip(byte[] data) {
		try {
			return gzip(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new byte[0];
	}


	public static byte[] zip(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}
		byte[] zipBytes = null;
		ZipOutputStream zip = null;
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			zip = new ZipOutputStream(bos);
			zip.putNextEntry(new ZipEntry("log"));
			zip.write(data);
			zip.closeEntry();
			zip.finish();
			zipBytes = bos.toByteArray();
		} catch (Throwable ex) {
			ex.printStackTrace();

		}

		finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

		}
		return zipBytes;
	}

	static String standardBytes2HexString(byte[] b) {
		try {
			return bytes2HexString(b);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	static String coreStandardBytes2HexString(byte[] b) {
		try {
			return bytes2HexString(b);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String bytes2HexString(byte[] b) {
		StringBuilder ret = new StringBuilder();
		if (b == null) {
			return null;
		}
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex);
		}
		return ret.toString();
	}

	/**
	 * 对数据进行gzip压缩
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 */
	private static byte[] gzip(byte[] data) throws IOException, Throwable {
		byte[] gzipBytes = null;
		ByteArrayOutputStream bos = null;
		GZIPOutputStream gzip = null;

		if (data == null) {
			return null;
		}
		try {
			bos = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(bos);
			gzip.write(data);
			gzip.finish();

			gzipBytes = bos.toByteArray();
		} catch (IOException e) {
			throw e;

		} catch (Throwable e) {
			throw e;
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (Throwable e) {
					throw e;
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Throwable e) {
					throw e;

				}
			}
		}

		return gzipBytes;
	}

	 /* 是否为数�?
	 * 
	 * @param value
	 *            待验证内�?
	 * @return 判断结果
	 */
	public static boolean isValidNumber(String value) {
		String regex = "^\\d*$";
		return Pattern.matches(regex, value);
	}
	
	/**
	 * 是否为数�?
	 * 
	 * @param value
	 *            待验证内�?
	 * @param len
	 *            限制的整数长�?
	 * @return 判断结果
	 */
	public static boolean isValidNumber(String value, int len) {
		String regex = "^\\d{0," + len + "}$";
		return Pattern.matches(regex, value);
	}

	/**
	 * 是否为小�?
	 * 
	 * @param value
	 *            待验证的内容
	 * @param floatLen
	 *            小数位数的长�?
	 * @return 验证结果
	 */
	public static boolean isValidDecimal(String value) {
		String regex = "^\\d+(.\\d*)?$";
		return Pattern.matches(regex, value);
	}
	
	/**
	 * 是否为小�?
	 * 
	 * @param value
	 *            待验证的内容
	 * @param floatLen
	 *            小数位数的长�?
	 * @return 验证结果
	 */
	public static boolean isValidDecimal(String value, int floatLen) {
		String regex = "^[0-9]+(.[0-9]{0," + floatLen + "})?$";
		return Pattern.matches(regex, value);
	}
	
	public static void showToast(Context context,String toastInfo){
		Toast.makeText(context, toastInfo, Toast.LENGTH_SHORT).show();
	}
}
