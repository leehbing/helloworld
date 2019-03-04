package com.atguigu.juc;

/*
 * 生产者和消费者案例
 * 如果不用等待唤醒机制，则会出现没有货了，却一直在消费，当然是返回缺货，或者一直在进货，当然是返回产品已满
 */
public class TestProductorAndConsumer {

	public static void main(String[] args) {
		Clerk1 clerk = new Clerk1();
		
		Productor1 pro = new Productor1(clerk);
		Consumer1 cus = new Consumer1(clerk);
		
		new Thread(pro, "生产者 A").start();
		new Thread(cus, "消费者 B").start();
		
		new Thread(pro, "生产者 C").start();
		new Thread(cus, "消费者 D").start();
	}
	
}


class Clerk1{   //店员
	private int product = 0;
	//进货
	public synchronized void get(){//循环次数：0
		while(product >= 1){//为了避免虚假唤醒问题，应该总是使用在循环中，用if会有虚假唤醒问题
			System.out.println("产品已满！");
			try {
				this.wait();  //如果已满，则不能继续生产了，得等待
			} catch (InterruptedException e) {
			}
		}
		System.out.println(Thread.currentThread().getName() + " : " + ++product);   //进货
		this.notifyAll();  //如果进货了，说明有多余的产品可卖，通知卖货


//		if(product >= 10){
//			System.out.println("产品已满！");
//		}else{
//			System.out.println(Thread.currentThread().getName() + " : " + ++product); //进货
//		}
	}
	//卖货
	public synchronized void sale(){//product = 0; 循环次数：0
		while(product <= 0){//为了避免虚假唤醒问题，应该总是使用在循环中，用if会有虚假唤醒问题
			System.out.println("缺货！");
			try {
				this.wait();  //发现缺货了，则等待
			} catch (InterruptedException e) {
			}
		}
		System.out.println(Thread.currentThread().getName() + " : " + --product);
		this.notifyAll();  //如果卖出了一个，则说明有空位了，可以通知进货

//		if(product <= 0){
//			System.out.println("缺货！");
//		}else{
//			System.out.println(Thread.currentThread().getName() + " : " + --product); //卖货
//		}
	}
}

//生产者
class Productor1 implements Runnable{
	private Clerk1 clerk;
	public Productor1(Clerk1 clerk) {
		this.clerk = clerk;
	}
	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			clerk.get();
		}
	}
}

//消费者
class Consumer1 implements Runnable{
	private Clerk1 clerk;
	public Consumer1(Clerk1 clerk) {
		this.clerk = clerk;
	}
	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			clerk.sale();
		}
	}
}