package com.renren.ugc.comment.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA. User: jinshunlong Date: 12-12-21 Time:
 * 下午12:09 To change this template use File | Settings | File Templates.
 */

public class HashAlgorithm {

    /**
     * Get the md5 of the given key.
     */
    private static byte[] computeMd5(String k) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
        md5.reset();
        try {
            md5.update(k.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return md5.digest();
    }

    public static BigInteger gen128bitKey(String k) {
        byte[] bs = computeMd5(k);
        return new BigInteger(1, bs);
    }

    public static BigInteger gen64bitKey(String k) {
        byte[] bs = computeMd5(k);
        byte[] bs2 = new byte[8];
        for (int i = 0; i < bs2.length; i++)
            bs2[i] = bs[i];
        return new BigInteger(1, bs2);
    }

    public static void main(String args[]) {
        System.out.println(gen64bitKey("nuclear_client_unittest_51000000000000004"));
    }
}
