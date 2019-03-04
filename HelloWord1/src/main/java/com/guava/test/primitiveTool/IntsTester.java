package com.guava.test.primitiveTool;

/**
 * @Author: hongbing.li
 * @Date: 28/9/2018 4:49 PM
 * 整数Ints是原始的int类型的实用工具类。
 */

import com.google.common.primitives.Ints;

import java.util.List;

public class IntsTester {
    public static void main(String args[]){
        IntsTester tester = new IntsTester();
        tester.testInts();
    }

    private void testInts(){
        int[] intArray = {1,2,3,4,5,6,7,8,9};

        //convert array of primitives to array of objects
        List<Integer> objectArray = Ints.asList(intArray);
        System.out.println(objectArray.toString());

        //convert array of objects to array of primitives
        intArray = Ints.toArray(objectArray);
        System.out.print("[ ");
        for(int i = 0; i< intArray.length ; i++){
            System.out.print(intArray[i] + " ");
        }
        System.out.println("]");
        //check if element is present in the list of primitives or not
        System.out.println("5 is in list? "+ Ints.contains(intArray, 5));

        //Returns the minimum
        System.out.println("Min: " + Ints.min(intArray));

        //Returns the maximum
        System.out.println("Max: " + Ints.max(intArray));

        //get the byte array from an integer
        byte[] byteArray = Ints.toByteArray(20000);
        for(int i = 0; i< byteArray.length ; i++){
            System.out.print(byteArray[i] + " ");
        }
    }
}
