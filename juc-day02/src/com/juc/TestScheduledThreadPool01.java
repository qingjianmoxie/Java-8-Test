package com.juc;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
/**
 * 构建一个能够替代while(true)死循环的循环任务
 * @author zhouzhigang
 *
 */
public class TestScheduledThreadPool01 {
	public static void main(String[] args) throws Exception {
		/**
		 * 第一步： 创建可以任务调度的线程池
		 */
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
			/**
			 * 第二步： 执行可以任务调度的线程池的schedule方法
			 */
		ScheduledFuture<?>  result = pool.scheduleAtFixedRate(new Runnable() {
				private int counts = 0;
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + " :  START");
					int num = new Random().nextInt(100);//生成随机数
					System.out.println(Thread.currentThread().getName() + " : " + num);
					counts ++;
//					try {
//							Thread.sleep(5000);
//					} catch (InterruptedException e1) {
//					}
					try {
						if(counts == 10){
							throw new RuntimeException("发生异常！");
						}
					} catch (Exception e) {
						System.out.println("发生异常！");
					}
					System.out.println(Thread.currentThread().getName() + " : " + num + "END");
				}
				
			}, 0,1, TimeUnit.SECONDS);// 第二个参数是延时，第三个参数是延时的单位
		}
}
