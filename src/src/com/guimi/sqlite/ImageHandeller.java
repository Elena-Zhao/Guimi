package com.guimi.sqlite;

import java.io.ByteArrayOutputStream;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.guimi.imagehandle.ImageFileCache;
import com.guimi.imagehandle.ImageMemoryCache;
import com.guimi.util.LocalAsyncImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageHandeller {
	private static String URL = "http://hyacinth067357.xicp.net:8080/GUIMIService.asmx";
	private static String NAMESPACE = "http://tempuri.org/";
	private static String METHOD_NAME;
	private static String SOAP_ACTION;
	private static ImageMemoryCache memoryCache;
	private static ImageFileCache fileCache;
	private static ImageHandeller imageHandeller;
	
	private ImageHandeller(Context context)
	{
		memoryCache = new ImageMemoryCache(context);
		fileCache = new ImageFileCache();
	}
	
	public static ImageHandeller getInstance(Context context) {
		if (imageHandeller == null)
			synchronized (GuiMiDB.class) {
				if (imageHandeller == null)
					imageHandeller = new ImageHandeller(context);
			}
		return imageHandeller;
	}
	
	public Bitmap getBitmap(String url) {
		// 从内存缓存中获取图片
		Bitmap result = memoryCache.getBitmapFromCache(url);
		if (result == null) {
			// 文件缓存中获取
			result = fileCache.getImage(url);
			memoryCache.addBitmapToCache(url, result);
		}
		return result;
	}

	public Bitmap download(String pictureUrl) {
		METHOD_NAME = "download";
		SOAP_ACTION = "http://tempuri.org/download";
		try {
			// 从内存缓存中获取图片
			Bitmap result = memoryCache.getBitmapFromCache(pictureUrl);
			if (result == null) {
				// 文件缓存中获取
				result = fileCache.getImage(pictureUrl);
				if (result == null) {
					SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
					rpc.addProperty("fileName", convertUrlToFileName(pictureUrl));
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					HttpTransportSE ht = new HttpTransportSE(URL);
					ht.debug = true;
					ht.call(SOAP_ACTION, envelope);
					Log.i("aaa", envelope.toString());
					if (envelope.bodyIn != null) {
						SoapObject soapObject = (SoapObject) envelope.bodyIn;
						byte[] data = Base64.decode(soapObject.getProperty(0)
								.toString());
						
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inJustDecodeBounds = true;
						result = BitmapFactory.decodeByteArray(data, 0,
								data.length,options);
						
						options.inSampleSize = LocalAsyncImageLoader.computeSampleSize(options, -1, 256*128);  
						options.inJustDecodeBounds = false;
						
						result = BitmapFactory.decodeByteArray(data, 0,
								data.length,options);
						Log.i("aaa", result.toString());
						// 将bitmap缓存到内存和文件缓存中
						fileCache.saveBitmap(result, pictureUrl);
						memoryCache.addBitmapToCache(pictureUrl, result);
					}
				} else
					memoryCache.addBitmapToCache(pictureUrl, result);
			}
			return result;
		} catch (Exception e) {
			Log.i("aaa", e.toString());
			return null;
		}
	}

	public boolean uploadPicture(String url) {
		METHOD_NAME = "uploadPicture";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		try {
			/* FileInputStream fis = new FileInputStream(url); */
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 添加压缩图片内容
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(url, options);
			options.inSampleSize = calculateInSampleSize(options, 480, 800);
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(url, options);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			/*
			 * byte[] buffer = new byte[1024]; int count = 0; while((count =
			 * fis.read(buffer)) >= 0){ baos.write(buffer, 0, count); }
			 * fis.close();
			 */
			String uploadBuffer = new String(Base64.encode(baos.toByteArray())); // 进行Base64编码
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			String[] temp = url.split("/");
			String fileName = temp[temp.length - 1];
			rpc.addProperty("fileName", fileName);
			rpc.addProperty("image", uploadBuffer);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			Log.i("db", "aaa");
			ht.call(SOAP_ACTION, envelope);
			Log.i("db", "aaa");
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			Log.i("db", "aaa");
			if (Boolean.valueOf(soapObject.getProperty(0).toString())) {
				String fileUrl = "D:/GuiMi/picture/" + convertUrlToFileName(url);
				BitmapFactory.Options opts = new BitmapFactory.Options();

				opts.inJustDecodeBounds = true;

				BitmapFactory.decodeFile(url, opts);

				   

				opts.inSampleSize = LocalAsyncImageLoader.computeSampleSize(opts, -1, 512*256);  

				opts.inJustDecodeBounds = false;
				Bitmap bmp = null;
				try {

				 bmp = BitmapFactory.decodeFile(url, opts);
				}
				catch (OutOfMemoryError err) {
					err.printStackTrace();
			    }
				fileCache.saveBitmap(bmp, fileUrl);
				memoryCache.addBitmapToCache(fileUrl, bmp);
				return true;
			} else
				return false;
		} catch (Exception e) {
			Log.i("aaa", e.toString());
			return false;
		}
	}

	private String convertUrlToFileName(String url) {
		String[] strs = url.split("/");
		return strs[strs.length - 1];
	}

	// 计算图片缩放值
	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int intSampleSize = 1;
		int height = options.outHeight;
		int width = options.outWidth;

		if (height > reqHeight || width > reqWidth) {
			int heightRatio = Math.round((float) height / (float) reqHeight);
			int widthRatio = Math.round((float) width / (float) reqWidth);
			intSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return intSampleSize;
	}
}
