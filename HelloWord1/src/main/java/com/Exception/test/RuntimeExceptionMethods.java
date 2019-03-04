package com.Exception.test;

/**
 * Created by lenovo on 4/15/2018.
 */

/**
 * 运行时异常测试方法
 *
 * @author xy
 */
public class RuntimeExceptionMethods {
    public void method3() throws RuntimeException {
        System.out.println("我是抛出运行时异常的方法");
    }

    public void testMethod3_01() {
        method3();
    }

    public void testMethod1_02() {
        throw new RuntimeException();
    }
}




