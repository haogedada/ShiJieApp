package com.shijie.utils;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by haoge on 2018/8/20.
 */

public class UpLoadFormUtil {

    /**
     * 初始化表单
     */
    public static MultipartBody formToMultipartBody(Map<String, ?> map, List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (File file : files) {
            if (!FileUtil.isImgFile(file.getName())||!FileUtil.isVedioFile(file.getName())){
                try {
                    throw new Exception("文件格式不合法");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            builder.addFormDataPart(key,value);
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }
}
