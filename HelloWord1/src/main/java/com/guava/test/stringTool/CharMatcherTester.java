package com.guava.test.stringTool;

/**
 * @Author: hongbing.li
 * @Date: 28/9/2018 4:43 PM
 * CharMatcher提供了各种方法来处理各种JAVA char类型值。
 */

import com.google.common.base.CharMatcher;

public class CharMatcherTester {
    public static void main(String args[]) {
        CharMatcherTester tester = new CharMatcherTester();
        tester.testCharMatcher();
    }

    private void testCharMatcher() {
        System.out.println(CharMatcher.DIGIT.retainFrom("mahesh123")); // only the digits
        System.out.println(CharMatcher.WHITESPACE.trimAndCollapseFrom("     Mahesh     Parashar ", ' '));
        // trim whitespace at ends, and replace/collapse whitespace into single spaces
        System.out.println(CharMatcher.JAVA_DIGIT.replaceFrom("mahesh123", "*")); // star out all digits
        System.out.println(CharMatcher.JAVA_DIGIT.or(CharMatcher.JAVA_LOWER_CASE).retainFrom("mahesh123"));
        // eliminate all characters that aren't digits or lowercase
    }
}
