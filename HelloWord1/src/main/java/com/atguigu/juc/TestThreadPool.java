package com.atguigu.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * 一、线程池：提供了一个线程队列，队列中保存着所有等待状态的线程。避免了创建与销毁额外开销（new Thread(tpd).start()），提高了响应的速度。
 * 
 * 二、线程池的体系结构：
 * 	java.util.concurrent.Executor : 负责线程的使用与调度的根接口
 * 		|--**ExecutorService 子接口: 线程池的主要接口
 * 			|--ThreadPoolExecutor 线程池的实现类
 * 			|--ScheduledExecutorService 子接口：负责线程的调度
 * 				|--ScheduledThreadPoolExecutor ：继承了 ThreadPoolExecutor， 实现了 ScheduledExecutorService
 * 
 * 三、工具类 : Executors 
 * ExecutorService newFixedThreadPool() : 创建固定大小线程数量的线程池
 * ExecutorService newCachedThreadPool() : 缓存线程池，线程池的数量不固定，可以根据需求自动的更改数量。
 * ExecutorService newSingleThreadExecutor() : 线程池中只有一个线程
 * 
 * ScheduledExecutorService newScheduledThreadPool() : 创建固定大小的线程，可以延迟或定时的执行任务。
 */
public class TestThreadPool {
	
	public static void main(String[] args) throws Exception {
		//1. 创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(5);
		List<Future<Integer>> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Future<Integer> future = pool.submit(new Callable<Integer>(){
				@Override
				public Integer call() throws Exception {
					int sum = 0;
					for (int i = 0; i <= 100; i++) {
						sum += i;
					}
					return sum;
				}
			});
			list.add(future);
		}

		pool.shutdown();

		for (Future<Integer> future : list) {
			System.out.println(future.get());//打印10个5050
		}

/*		//1. 创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(5);
		ThreadPoolDemo tpd = new ThreadPoolDemo();
		//2. 为线程池中的线程分配任务
		for (int i = 0; i < 10; i++) {//这里的循环，第一次submit，线程池里只有一个线程干活，第二次submit，则线程池里有两个线程干活，最多5个，因为创建的是固定数量线程的线程池
			pool.submit(tpd);  //打印0~100这一个任务提交给线程池中的线程来做，可能1-20是线程1干了，21-44是线程2干了。。。
		}
		//3. 关闭线程池
		pool.shutdown(); //当所有已经提交的任务执行完毕后关闭线程池，并且不会再接收新的任务*/
	}
//	new Thread(tpd).start();
//	new Thread(tpd).start();

}

class ThreadPoolDemo implements Runnable{

	private int i = 0;
	@Override
	public void run() {
		while(i <= 100){
			System.out.println(Thread.currentThread().getName() + " : " + i++);
		}
	}
}