package com.police170m3.rpi.jjhmapproject.Util;

import android.content.Context;

import java.io.File;

/**
 * Created by Jaehun on 2017-03-23.
 */

public class FileCache {

    /**
    /*파일 캐시는 임시로 받아온 이미지를 연속적으로 다운로드가 되는것을 막기위해
     내부 저장소에 저장시킨다
    */

    private File cacheDir;

    public FileCache(Context context) {
        // 캐싱 된 이미지를 저장할 디렉터리를 찾거나 없으면 만든다
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "jjhJSONParsingCache");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        // String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;
    }

    // 파일이 옮겨지면 원래들어있던 파일은 지움
    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}
