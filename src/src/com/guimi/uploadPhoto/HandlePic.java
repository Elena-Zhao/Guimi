package com.guimi.uploadPhoto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.guimi.util.LocalAsyncImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class HandlePic {
	public static final int ACTION_TAKE_PHOTO = 100;
	public static final int ACTION_GET_LOCAL_PHOTO = 200;
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private static Activity mActivity;
	private String mCurrentPhotoPath;
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	private static String albumNameString;
	private int targetDeviceWidth;
	
	
	
	public int getTargetDeviceWidth() {
		return targetDeviceWidth;
	}

	public void setTargetDeviceWidth(int targetDeviceWidth) {
		this.targetDeviceWidth = targetDeviceWidth;
	}

	public String getmCurrentPhotoPath() {
		return mCurrentPhotoPath;
	}

	public void setmCurrentPhotoPath(String mCurrentPhotoPath) {
		this.mCurrentPhotoPath = mCurrentPhotoPath;
	}

	public HandlePic(String albumName, Activity activity) {

		albumNameString = albumName;
		
		mActivity = activity;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}
	public void handleBigCameraPhoto(ImageView mImageView) {

		if (mCurrentPhotoPath != null) {
			setPic(mImageView);
			galleryAddPic();
			mCurrentPhotoPath = null;
		}

	}
	public void dispatchTakePictureIntent(int actionCode) {
		Intent picIntent = null;
		

		switch(actionCode) {
		case HandlePic.ACTION_TAKE_PHOTO:{
			picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			break;}
		case ACTION_GET_LOCAL_PHOTO:{
			picIntent = new Intent("android.intent.action.PICK");
			picIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
			picIntent.putExtra("crop", "true");
			picIntent.putExtra("aspectY", 1);
			picIntent.putExtra("aspectX", 1);
		};break;
		default:
			break;			
		} // switch
		File file = null;
		try {
			file = setUpPhotoFile();
			picIntent.putExtra("output", Uri.fromFile(file));


		} catch (IOException e) {
			e.printStackTrace();
			file = null;
			setmCurrentPhotoPath(null);
		}

		mActivity.startActivityForResult(picIntent, actionCode);
	}

	
	
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}
	public void galleryAddPic() {
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    mActivity.sendBroadcast(mediaScanIntent);
	}

	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(albumNameString);

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
			
		} else {
			Log.v("app Debug Msg", "External storage is not mounted READ/WRITE.");
		}
		
		return storageDir;
	}
	public String saveBitmap(Bitmap bitmap){
		try {
			File f = setUpPhotoFile();
			FileOutputStream out = new FileOutputStream(f); 
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "cannot create new image!!!!!";
		}
		return getmCurrentPhotoPath();
	};
	private void setPic(ImageView mImageView) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();
		

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		Log.d("picPath", mCurrentPhotoPath);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = true;
		/* Decode the JPEG file into a Bitmap */
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		
		
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = LocalAsyncImageLoader.computeSampleSize(bmOptions, -1, 512*512);
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		
		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);
		mImageView.setVisibility(View.VISIBLE);
	}
	
	


}
