package com.guava.test.stringTool;

import com.google.common.base.CaseFormat;

/**
 * @Author: hongbing.li
 * @Date: 28/9/2018 4:43 PM
 * CaseFormat是一种实用工具类，以提供不同的ASCII字符格式之间的转换。
 */
public class CaseFormatTester {
    public static void main(String args[]){
        CaseFormatTester tester = new CaseFormatTester();
        tester.testCaseFormat();
    }

    private void testCaseFormat(){
        String data = "test_data";
        System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-data"));
        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "test_data"));
        System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "test_data"));
    }
}
