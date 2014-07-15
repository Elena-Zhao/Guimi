package com.guimi.util;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LocalAsyncImageLoader {

	 private HashMap<String, SoftReference<Drawable>> imageCache;
	 //private static int listWidth = 20;
	  
	     public LocalAsyncImageLoader() {
	    	 //listWidth = width;
	    	 imageCache = new HashMap<String, SoftReference<Drawable>>();
	     }
	  
	     public Drawable loadDrawable(final int pos, final String imageUrl, final ImageCallback imageCallback) {
	         if (imageCache.containsKey(imageUrl)) {
	        	 Log.i("aaa","imageCache.containsKey(imageUrl)");
	             SoftReference<Drawable> softReference = imageCache.get(imageUrl);
	             Drawable drawable = softReference.get();
	             if (drawable != null) {
	                 return drawable;
	             }
	         }
	         final Handler handler = new Handler() {
	             public void handleMessage(Message message) {
	                 imageCallback.imageLoaded((Drawable) message.obj, pos);
	             }
	         };
	         new Thread() {
	             @Override
	             public void run() {
	                 Drawable drawable = loadImageFromUrl(imageUrl);
	                 imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
	                 Message message = handler.obtainMessage(0, drawable);
	                 handler.sendMessage(message);
	             }
	         }.start();
	         return null;
	     }
	  
		public static Drawable loadImageFromUrl(String url) {
			URL m;
			InputStream i = null;
			try {
				m = new URL(url);
				i = (InputStream) m.getContent();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			BitmapFactory.Options opts = new BitmapFactory.Options();

			opts.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(url, opts);

			   

			opts.inSampleSize = computeSampleSize(opts, -1, 256*256);  

			opts.inJustDecodeBounds = false;
			Bitmap bmp = null;
			try {

			 bmp = BitmapFactory.decodeFile(url, opts);
			}
			catch (OutOfMemoryError err) {
				err.printStackTrace();
		    }
			return new BitmapDrawable(bmp);
		}
	  
	     public interface ImageCallback {
	         public void imageLoaded(Drawable imageDrawable, int pos);
	     }
	     public static int computeSampleSize(BitmapFactory.Options options,

	    	        int minSideLength, int maxNumOfPixels) {

	    	    int initialSize = computeInitialSampleSize(options, minSideLength,

	    	            maxNumOfPixels);



	    	    int roundedSize;

	    	    if (initialSize <= 8) {

	    	        roundedSize = 1;

	    	        while (roundedSize < initialSize) {

	    	            roundedSize <<= 1;

	    	        }

	    	    } else {

	    	        roundedSize = (initialSize + 7) / 8 * 8;

	    	    }



	    	    return roundedSize;

	    	}



	    	private static int computeInitialSampleSize(BitmapFactory.Options options,

	    	        int minSideLength, int maxNumOfPixels) {

	    	    double w = options.outWidth;

	    	    double h = options.outHeight;



	    	    int lowerBound = (maxNumOfPixels == -1) ? 1 :

	    	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

	    	    int upperBound = (minSideLength == -1) ? 128 :

	    	            (int) Math.min(Math.floor(w / minSideLength),

	    	            Math.floor(h / minSideLength));



	    	    if (upperBound < lowerBound) {

	    	        // return the larger one when there is no overlapping zone.

	    	        return lowerBound;

	    	    }



	    	    if ((maxNumOfPixels == -1) &&

	    	            (minSideLength == -1)) {

	    	        return 1;

	    	    } else if (minSideLength == -1) {

	    	        return lowerBound;

	    	    } else {

	    	        return upperBound;

	    	    }

	    	} 

}
