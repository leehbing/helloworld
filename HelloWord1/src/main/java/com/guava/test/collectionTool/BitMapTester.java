package com.guava.test.collectionTool;

/**
 * @Author: hongbing.li
 * @Date: 28/9/2018 4:18 PM
 * BiMap是一种特殊的映射其保持映射，同时确保没有重复的值是存在于该映射和一个值可以安全地用于获取键背面的倒数映射。
 */
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class BitMapTester {
    public static void main(String args[]){
        BiMap<Integer, String> empIDNameMap = HashBiMap.create();

        empIDNameMap.put(new Integer(101), "Mahesh");
        empIDNameMap.put(new Integer(102), "Sohan");
        empIDNameMap.put(new Integer(103), "Ramesh");

        //Emp Id of Employee "Mahesh"
        System.out.println(empIDNameMap.inverse().get("Mahesh"));
    }
}
