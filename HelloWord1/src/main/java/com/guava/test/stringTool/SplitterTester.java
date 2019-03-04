package com.guava.test.stringTool;

/**
 * @Author: hongbing.li
 * @Date: 28/9/2018 4:43 PM
 * Splitter 提供了各种方法来处理分割操作字符串，对象等。
 */
import com.google.common.base.Splitter;

public class SplitterTester {
    public static void main(String args[]){
        SplitterTester tester = new SplitterTester();
        tester.testSplitter();
    }

    private void testSplitter(){
        System.out.println(Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split("the ,quick, , brown         , fox,              jumps, over, the, lazy, little dog."));
    }
}
