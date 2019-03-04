package com.bitmap.test;

import org.roaringbitmap.RoaringBitmap;

/**
 * Created by IntelliJ IDEA.
 * Author lihongbing on 6/11/2018.
 */

public class Test {
    public static void main(String [] args){

        RoaringBitmap mrb = RoaringBitmap.bitmapOf(1,2,3,1000);
        System.out.println("starting with  bitmap "+ mrb);
    }
}
