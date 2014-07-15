package com.guimi.imagehandle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import com.guimi.util.LocalAsyncImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class ImageFileCache {
    private  final String CACHDIR = "/WeiBao/ImgCach";
    private  final String WHOLESALE_CONV = ".cach";
                                                            
    private  final int MB = 1024*1024;
    private  final int CACHE_SIZE = 10;
    private  final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
                                                                
    public ImageFileCache() {
        removeCache(getDirectory());
    }
                            
    public Bitmap getImage(final String url) {    
        final String path = getDirectory() + "/" + convertUrlToFileName(url);
        File file = new File(path);
        if (file.exists()) {
        	BitmapFactory.Options opts = new BitmapFactory.Options();

			opts.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(url, opts);

			   

			opts.inSampleSize = LocalAsyncImageLoader.computeSampleSize(opts, -1, 256*128);  
			opts.inJustDecodeBounds = false;
			Bitmap bmp = null;
			try {

			 bmp = BitmapFactory.decodeFile(url, opts);
			}
			catch (OutOfMemoryError err) {
				err.printStackTrace();
		    }
            bmp = BitmapFactory.decodeFile(path);
            if (bmp == null) {
                file.delete();
            } else {
                updateFileTime(path);
                return bmp;
            }
        }
        return null;
    }
                               
    public void saveBitmap(Bitmap bm, String url) {
        if (bm == null) {
            return;
        }
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return;
        }
        String filename = convertUrlToFileName(url);
        String dir = getDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdirs();
        File file = new File(dir +"/" + filename);
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }
    } 
    
    private boolean removeCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return true;
        }
        if (!android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return false;
        }
                                                            
        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }
                                                            
        if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }
                                                            
        if (freeSpaceOnSd() <= CACHE_SIZE) {
            return false;
        }
                                                                    
        return true;
    }
                             
    public void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }
                              
    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    } 
                        
    private String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1] + WHOLESALE_CONV;
    }
                   
    private String getDirectory() {
        String dir = getSDPath() + CACHDIR;
        return dir;
    }
                        
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);  //�ж�sd���Ƿ����
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //��ȡ��Ŀ¼
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    } 
                                                            
    
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
                                                            
}
