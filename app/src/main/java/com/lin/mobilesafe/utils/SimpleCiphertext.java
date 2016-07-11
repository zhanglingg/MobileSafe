package com.lin.mobilesafe.utils;

/**
 * 实现异或加密和解密
 * Created by Administrator on 2016/7/11 0011.
 */
public class SimpleCiphertext {

    public static String  encryptOrdecrypt(int seed, String ciphertext) {

        byte[] bytes = ciphertext.getBytes();

        for(int i = 0; i < bytes.length; i++) {

            bytes[i] ^= seed;
        }
        return new String(bytes);
    }


}
