package com.atguigu.juc;

/*
 * 一、volatile 关键字：当多个线程进行操作共享数据时，可以保证内存中的数据可见。底层原理比较复杂，内存栅栏
 * 					  相较于 synchronized 是一种较为轻量级的同步策略。当然synchronized也可以解决这个问题
 * 
 * 注意：
 * 1. volatile 不具备“互斥性”，不像synchronized
 * 2. volatile 不能保证变量的“原子性”
 */
public class TestVolatile {
	public static void main(String[] args) {
		ThreadDemo1 td = new ThreadDemo1();
		new Thread(td).start();
		while(true){
			if(td.isFlag()){
				System.out.println("------------------");
				break;
			}
			/**
			 * 如果不用volatile，用synchronized也可以,它会强制去主存读数据，但是效率很低
			  synchronized (td){
			   	 if(td.isFlag()){
			 	 	System.out.println("------------------");
			 	 	break;
			     }
			  }
			 */
		}
	}

}

 class ThreadDemo1 implements Runnable {
	private volatile boolean flag = false;
	/**
	 * 如果这里不加volatile，则System.out.println("------------------");行执行不到
	 * 因为main线程一开始读了flag的值，并且会有缓存，所以以后循环的时候都是读的是false，而如果用了volatile，则两个线程针对这个变量不用缓存，直接操作主存
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
		flag = true;
		System.out.println("flag=" + isFlag());
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}