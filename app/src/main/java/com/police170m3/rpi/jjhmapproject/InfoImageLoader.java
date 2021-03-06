package com.police170m3.rpi.jjhmapproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.police170m3.rpi.jjhmapproject.Util.FileCache;
import com.police170m3.rpi.jjhmapproject.Util.MemoryCache;
import com.police170m3.rpi.jjhmapproject.Util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jaehun on 2017-03-25.
 */

public class InfoImageLoader {

    //InfoActivity에 들어갈 이미지 크기 및 메모리 사용량 수정

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView,String>());
    ExecutorService executorService;
    //UI 스레드에서 실행될 핸들러 생성
    Handler handler = new Handler();

    public InfoImageLoader(Context context){
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    //파일을 받고있는 동안 기본으로 설정될 파일 생성
    final int stub_id = R.drawable.images_speaking;

    public void DisplayImage(String url, ImageView imageView){
        imageViews.put(imageView, url);

        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
        }else {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        InfoImageLoader.PhotoToLoad p = new InfoImageLoader.PhotoToLoad(url, imageView);
        executorService.submit(new InfoImageLoader.PhotosLoader(p));
    }

    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);

        Bitmap b = decodeFile(f);
        if (b != null){
            return b;
        }

        // JSON파일에서 받은 링크 주소로 이미지를 받는다
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.connect();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                Utils.CopyStream(is, os);
                os.close();
                conn.disconnect();
                bitmap = decodeFile(f);
            }
            return bitmap;
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    // 메모리 낭비를 막기위한 이미지 크기 설정
    private Bitmap decodeFile(File f) {
        try {
            // 이미지 크기 설정
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // 적절한 크기값을 찾음. It should be the power of 2.
            // 적절한 사이즈는 512
            final int REQUIRED_SIZE = 300;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // inSampleSize 크기를 조정한다
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);

            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException","파일을 찾지 못했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        InfoImageLoader.PhotoToLoad photoToLoad;

        PhotosLoader(InfoImageLoader.PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);

                if (imageViewReused(photoToLoad))
                    return;
                InfoImageLoader.BitmapDisplayer bd = new InfoImageLoader.BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(InfoImageLoader.PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        InfoImageLoader.PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, InfoImageLoader.PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
