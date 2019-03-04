package com.Exception.test;

/**
 * Created by lenovo on 4/16/2018.
 */
public class InterruptTest {
    public static class TestThread extends Thread {
        public volatile boolean go = false;

        public void run() {
            test();
        }

        private synchronized void test() {
            System.out.println("running");

            while (!go) {
            }
            try {
                if (isInterrupted()) {
                    System.out.println("Interrupted");
                }

                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("InterruptedException。。");
            }
        }
    }

    public static void main(String[] args) {
        TestThread thread = new TestThread();
        thread.start();

        thread.interrupt();
        thread.go = true;
    }

}
