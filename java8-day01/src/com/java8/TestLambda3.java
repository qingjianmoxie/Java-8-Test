package com.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.Test;

/*
 * Java8 内置的四大核心函数式接口
 * 
 * Consumer<T> : 消费型接口
 * 		void accept(T t);
 * 
 * Supplier<T> : 供给型接口
 * 		T get(); 
 * 
 * Function<T, R> : 函数型接口
 * 		R apply(T t);
 * 
 * Predicate<T> : 断言型接口
 * 		boolean test(T t);
 * 
 */
public class TestLambda3 {
	
	/**
	 * ④.Java8内置的函数式接口之 断言型接口
	 * Predicate<T> 断言型接口：这个函数式接口中的唯一的一个叫  boolean test(T t);的接口，主要是调用完这个函数式接口的方法将操作完入参之后，以boolean的值返回。
	 */
	@Test
	public void test4(){
		List<String> list = Arrays.asList("Hello", "atguigu", "Lambda", "www", "ok");
		List<String> strList = filterStr(list, (s) -> s.length() > 3);
		
		for (String str : strList) {
			System.out.println(str);
		}
	}
	
	//需求：将满足条件的字符串，放入集合中
	public List<String> filterStr(List<String> list, Predicate<String> pre){
		List<String> strList = new ArrayList<>();
		
		for (String str : list) {
			if(pre.test(str)){
				strList.add(str);
			}
		}
		
		return strList;
	}
	
	/**
	 * ③.Java8内置的函数式接口之 函数型接口
	 * Function<T, R> 函数型接口：这个函数型接口中唯一的一个叫  R apply(T t);的方法既有入参，又有返回值的方法，主要是处理完入参，然后把结果返回。
	 */
	@Test
	public void test3(){
		String newStr = strHandler("\t\t\t 我大testdemo威武   ", (str) -> str.trim());
		System.out.println(newStr);
		
		String subStr = strHandler("我大testdemo威武", (str) -> str.substring(2, 5));
		System.out.println(subStr);
	}
	
	//需求：用于处理字符串
	public String strHandler(String str, Function<String, String> fun){
		return fun.apply(str);
	}
	
	/**
	 * ②.Java8内置的函数式接口之 供给型接口【供给型接口用来给你返回一些对象】
	 * Supplier<T> 供给型接口 :这个供给型函数式接口中的这个唯一的叫T get();的方法有返回值，没有入参数，主要是使用返回值。
	 */
	@Test
	public void test2(){
		/**
		 * 记住：Lambda表达式就是对函数式接口中的这个唯一的方法的实现的代码体的表达式。
		 */
		List<Integer> numList = getNumList(10, () -> (int)(Math.random() * 100));
		
		for (Integer num : numList) {
			System.out.println(num);
		}
	}
	
	//需求：产生指定个数的整数，并放入集合中
	public List<Integer> getNumList(int num, Supplier<Integer> sup){
		List<Integer> list = new ArrayList<>();
		
		for (int i = 0; i < num; i++) {
			// 用于产生什么样的整数：接口的实现类对象调用使用Lambda表达式实现了这个函数式接口的方法
			Integer n = sup.get();
			list.add(n);
		}
		
		return list;
	}
	
	/**
	 * ①.Java8内置的函数式接口之消费型接口
	 * Consumer<T> 消费型接口 :函数式接口中的这唯一的这个叫 void accept(T t);的方法没有返回值，主要是使用这个入参。
	 */
	@Test
	public void test1(){
		/**
		 * 消费型接口：就是你传进去一个参数，然后右边对这个参数作一些操作，没有返回值，只是消费。
		 */
		happy(10000, (m) -> System.out.println("你们刚哥喜欢大宝剑，每次消费：" + m + "元"));
	} 
	
	public void happy(double money, Consumer<Double> con){
		con.accept(money);
	}
}
