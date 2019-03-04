package com.guava.test.primitiveTool;

/**
 * @Author: hongbing.li
 * @Date: 28/9/2018 4:48 PM
 * Shorts是基本类型short的实用工具类。
 */

import com.google.common.primitives.Shorts;

import java.util.List;

public class ShortsTester {
    public static void main(String args[]){
        ShortsTester tester = new ShortsTester();
        tester.testShorts();
    }

    private void testShorts(){
        short[] shortArray = {1,2,3,4,5,6,7,8,9};

        //convert array of primitives to array of objects
        List<Short> objectArray = Shorts.asList(shortArray);
        System.out.println(objectArray.toString());

        //convert array of objects to array of primitives
        shortArray = Shorts.toArray(objectArray);
        System.out.print("[ ");
        for(int i = 0; i< shortArray.length ; i++){
            System.out.print(shortArray[i] + " ");
        }
        System.out.println("]");
        short data = 5;
        //check if element is present in the list of primitives or not
        System.out.println("5 is in list? "+ Shorts.contains(shortArray, data));

        //Returns the minimum
        System.out.println("Min: " + Shorts.min(shortArray));

        //Returns the maximum
        System.out.println("Max: " + Shorts.max(shortArray));
        data = 2400;
        //get the byte array from an integer
        byte[] byteArray = Shorts.toByteArray(data);
        for(int i = 0; i< byteArray.length ; i++){
            System.out.print(byteArray[i] + " ");
        }
    }
}