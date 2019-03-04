package com.Exception.test;
import java.io.IOException;

/**
 * Checked异常测试方法
 */
public class CheckedExceptionMethods {
    // 总异常类，既有checkedException又有RuntimeException，所以其中的checkedException必须处理
    public void method1() throws Exception {
        System.out.println("我是抛出异常总类的方法");
    }

    public void testMethod1_01() {// 捕获并处理这个异常
        try {
            method1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testMethod1_02() throws Exception {// 把异常传递下去
        method1();
    }

    public void testMethod1_03() throws Exception {
        throw new Exception();
    }

    public void testMethod1_04() {
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // checkedException典型代表IOException
    public void method2() throws IOException {
        System.out.println("我是抛出IO异常的方法");
    }

    public void testMethod2_01() {
        try {
            method2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testMethod2_02() throws Exception {
        method2();
    }
}
