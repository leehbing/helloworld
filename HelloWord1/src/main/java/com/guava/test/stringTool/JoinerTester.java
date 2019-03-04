package com.guava.test.stringTool;

/**
 * @Author: hongbing.li
 * @Date: 28/9/2018 4:42 PM
 * Joiner 提供了各种方法来处理字符串加入操作，对象等。
 */

import com.google.common.base.Joiner;

import java.util.Arrays;

public class JoinerTester {
    public static void main(String args[]){
        JoinerTester tester = new JoinerTester();
        tester.testJoiner();
    }

    private void testJoiner(){
        System.out.println(Joiner.on(",")
                .skipNulls()
                .join(Arrays.asList(1,2,3,4,5,null,6)));
    }
}
