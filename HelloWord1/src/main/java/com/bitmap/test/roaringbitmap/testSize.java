package com.bitmap.test.roaringbitmap;

import org.apache.hadoop.hbase.util.Bytes;
import org.roaringbitmap.RoaringBitmap;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class testSize {

    public static void main(String[] args) {

        long tmp = 0;
        RoaringBitmap rb = RoaringBitmap.bitmapOf();
        for (long m = 0; m < 1000000L; m++) {

            tmp = ThreadLocalRandom.current().nextLong(1000000L);
            rb.add(tmp , tmp + 1);
        }
        rb.runOptimize(); //to improve compression
//      rb.add(0L, 1L << 32);// the biggest bitmap we can create
        System.out.println(1L << 32);
        System.out.println(rb.getLongCardinality());
        System.out.println(rb.getSizeInBytes() /(1024 ) + "KB");
        System.out.println("memory usage: " + rb.getSizeInBytes() * 1.0 / (1L << 32) + " byte per value");
        if (rb.getLongCardinality() != (1L << 32))
            throw new RuntimeException("bug!");

    }
}
