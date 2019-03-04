package com.roocon.jvm;

/**
 * @Author: hongbing.li
 * @Date: 8/10/2018 4:12 PM
 *
 * -verbose:gc -XX:+PrintGCDetails
 */
public class GCLogDemo {
    public static void main(String[] args) {
        int _10m = 10 * 1024 * 1024;
        byte[] data = new byte[_10m];

        data = null;      // 将data置为null即让它成为垃圾
        System.gc();        // 通知垃圾回收器回收垃圾
        try {
            Thread.sleep(2000);
            System.out.println("waiting....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
