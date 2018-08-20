package com.shijie.utils;


public class FileUtil {

    /**
     * 根据文件后缀名判断 文件是否是视频文件
     *
     * @param fileName 文件名
     * @return 是否是视频文件
     */
    public static boolean isVedioFile(String fileName) {
        if (fileName == null) {
            return false;
        } else {
            // 获取文件后缀名并转化为写，用于后续比较
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
            // 创建图片类型数组
            String img[] = {  "avi","flv","mpg","mpeg","mpe","m1v","m2v","mpv2","mp2v","dat","ts","tp","tpr","pva","pss","mp4","m4v",
                    "m4p","m4b","3gp","3gpp","3g2","3gp2","ogg","mov","qt","amr","rm","ram","rmvb","rpm"};
            for (int i = 0; i < img.length; i++) {
                if (img[i].equals(fileType)) {
                    return true;
                }
            }
        }
        return false;
    }
    //判断文件是否是图片
    public static boolean isImgFile(String fileName) {
        if (fileName == null) {
            return false;
        } else {
            // 获取文件后缀名并转化为写，用于后续比较
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
            // 创建图片类型数组
            String img[] = {"jpg", "jpeg", "png", "bmp", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
                    "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
            for (int i = 0; i < img.length; i++) {
                if (img[i].equals(fileType)) {
                    return true;
                }
            }
        }
        return false;
    }


}