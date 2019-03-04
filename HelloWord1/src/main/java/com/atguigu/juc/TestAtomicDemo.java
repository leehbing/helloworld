package com.atguigu.juc;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * 一、i++ 的原子性问题：i++ 的操作实际上分为三个步骤“读-改-写”
 * 		  int i = 10;
 * 		  i = i++; //10
 * 
 * 		  int temp = i;//i++计算机底层原理
 * 		  i = i + 1;
 * 		  i = temp;
 * 
 * 二、原子变量：在 java.util.concurrent.atomic 包下提供了一些原子变量。
 * 		1. volatile 只保证内存可见性，不能保证原子性,原子变量里面包括了volatile特性，可以看AtomicInteger源码
 * 		2. CAS（Compare-And-Swap） 算法保证数据变量的原子性
 * 			CAS 算法是硬件对于并发操作的支持，，针对多处理器操作而设计的处理器中的一种特殊指令，用于管理对共享数据的并发访问。
 * 		    CAS 是一种无锁的非阻塞算法的实现
 * 			CAS 包含了三个操作数：
 * 			①需要读写的内存值  V
 * 			②进行比较的值  A
 * 			③拟写入的新值  B
			当且仅当 V 的值等于 A 时， CAS 通过原子方式用新值 B 来更新 V 的值，否则不会执行任何操作，然后再次尝试
 */
public class TestAtomicDemo {

    public static void main(String[] args) {
        AtomicDemo ad = new AtomicDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(ad).start();
        }
    }
}

class AtomicDemo implements Runnable {

    //	private int serialNumber = 0;// 如果用这个，则打印的地方会出现重复值，加volatile解决不了
    private AtomicInteger serialNumber = new AtomicInteger(0);

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        System.out.println(Thread.currentThread().getName() +":"+ getSerialNumber());
    }

    public int getSerialNumber() {
        return serialNumber.getAndIncrement();
//		return serialNumber++;  //i++ 是分步骤的。。。
    }
}



