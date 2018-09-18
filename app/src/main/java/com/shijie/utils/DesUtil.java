package com.shijie.utils;


import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DesUtil {
    public static final String ENCODING = "UTF-8";
    private static final String KEY_ALGORITHM = "des";
    private static final String CIPHER_ALGORITHM_ECB = "DES/ECB/PKCS5Padding";

    public static void main(String[] args) throws Exception {
        //调试加密解密 key最多为8个字符
        String data = "fuckyou";
        String key = "haogedad";
        String data4 = "UazVyt4KfG+lHa8HFb5eFA==";

        //输出加密
        System.out.println(encrypt(data, key));
        //输出解密
        System.out.println(decrypt(data4, key));
    }

    /**
     * 还原密钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        DESKeySpec des = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(des);
        return secretKey;
    }

    /**
     * des加密
     *
     * @param data 加密数据
     * @param key  解密密匙
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        Key k = toKey(key.getBytes(ENCODING));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        String str;
        try {
            str = Base64.encodeToString(cipher.doFinal(data.getBytes(ENCODING)), Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
        return str;
    }

    /**
     * des解密
     *
     * @param data 加密数据
     * @param key  解密密匙
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        Key k = toKey(key.getBytes(ENCODING));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        cipher.init(Cipher.DECRYPT_MODE, k);
        byte[] bytes;
        try {
            bytes = cipher.doFinal(Base64.decode(data.getBytes(ENCODING), Base64.DEFAULT));
        } catch (Exception e) {
            return null;
        }
        return new String(bytes, ENCODING);
    }
}
