package com.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.junit.Test;

/*
 * 一、创建执行线程的方式三：实现 Callable 接口。 相较于实现 Runnable 接口的方式，方法可以有返回值，并且可以抛出异常。
 * 
 * 二、执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。  FutureTask 是  Future 接口的实现类
 */
public class TestCallable {
	
	public static void main(String[] args) {
		ThreadDemo td = new ThreadDemo();
		
		//1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
		FutureTask<Integer> result = new FutureTask<>(td);
		
		new Thread(result).start();
		
		//2.接收线程运算后的结果
		try {
			Integer sum = result.get();  //FutureTask 可用于 闭锁
			System.out.println(sum);
			System.out.println("------------------------------------");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取多个线程的结果
	 */
	@Test
	public void test01(){
		FutureTask<Integer> result = null;
		for(int i = 0 ;i < 10; i++){
			ThreadDemo td = new ThreadDemo(i);
			//1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
			result = new FutureTask<>(td);
			new Thread(result).start();
			Integer sum;
			try {
				sum = result.get();
				System.out.println(sum);
				System.out.println("------------------------------------");
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  //FutureTask 可用于 闭锁
			
		}
		
	}
}

class ThreadDemo implements Callable<Integer>{
   private Integer n;
   
	public ThreadDemo() {
	super();
    }
	public ThreadDemo(Integer n) {
	super();
	this.n = n;
}
	@Override
	public Integer call() throws Exception {
		int sum = 0;
		
		for (int i = 0; i <= n; i++) {
			sum += i;
		}
		
		return sum;
	}
	public Integer getN() {
		return n;
	}
	public void setN(Integer n) {
		this.n = n;
	}
	
}

/*class ThreadDemo implements Runnable{

	@Override
	public void run() {
	}
	
}*/