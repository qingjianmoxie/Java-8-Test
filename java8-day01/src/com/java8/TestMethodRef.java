package com.java8;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

/*
 * 一、方法引用：若 Lambda 体中的功能，已经有方法提供了实现，可以使用方法引用
 * 			  （可以将方法引用理解为 Lambda 表达式的另外一种表现形式）
 * 
 * 1. 对象的引用 :: 实例方法名
 * 
 * 2. 类名 :: 静态方法名
 * 
 * 3. 类名 :: 实例方法名
 * 
 * 注意：
 * 	 ①方法引用所引用的方法的参数列表与返回值类型，需要与函数式接口中抽象方法的参数列表和返回值类型保持一致！
 * 	 ②若Lambda 的参数列表的第一个参数，是实例方法的调用者，第二个参数(或无参)是实例方法的参数时，格式： ClassName::MethodName
 * 
 * 二、构造器引用 :构造器的参数列表，需要与函数式接口中参数列表保持一致！
 * 
 * 1. 类名 :: new
 * 
 * 三、数组引用
 * 
 * 	类型[] :: new;
 * 
 * 
 */
public class TestMethodRef {
	//数组引用
	@Test
	public void test8(){
		Function<Integer, String[]> fun = (args) -> new String[args];
		String[] strs = fun.apply(10);
		System.out.println(strs.length);
		
		System.out.println("--------------------------");
		
		Function<Integer, Employee[]> fun2 = Employee[] :: new;
		Employee[] emps = fun2.apply(20);
		System.out.println(emps.length);
	}
	
	//构造器引用
	@Test
	public void test7(){
		Function<String, Employee> fun = Employee::new;
		
		BiFunction<String, Integer, Employee> fun2 = Employee::new;
	}
	
	@Test
	public void test6(){
		Supplier<Employee> sup = () -> new Employee();
		System.out.println(sup.get());
		
		System.out.println("------------------------------------");
		
		Supplier<Employee> sup2 = Employee::new;
		System.out.println(sup2.get());
	}
	
	//类名 :: 实例方法名
	@Test
	public void test5(){
		BiPredicate<String, String> bp = (x, y) -> x.equals(y);
		System.out.println(bp.test("abcde", "abcde"));
		
		System.out.println("-----------------------------------------");
		
		BiPredicate<String, String> bp2 = String::equals;
		System.out.println(bp2.test("abc", "abc"));
		
		System.out.println("-----------------------------------------");
		
		
		Function<Employee, String> fun = (e) -> e.show();
		System.out.println(fun.apply(new Employee()));
		
		System.out.println("-----------------------------------------");
		
		Function<Employee, String> fun2 = Employee::show;
		System.out.println(fun2.apply(new Employee()));
		
	}
	
	//类名 :: 静态方法名
	@Test
	public void test4(){
		Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
		System.out.println(com.compare(1, 1));
		System.out.println("-------------------------------------");
		
		Comparator<Integer> com2 = Integer::compare;
		int compare = com2.compare(1, -1);
		System.out.println(compare);
	}
	
	/**
	 * 第二种形式：类名::类方法名 
	 */
	@Test
	public void test3(){
		BiFunction<Double, Double, Double> fun = (x, y) -> Math.max(x, y);
		System.out.println(fun.apply(1.5, 22.2));
		
		System.out.println("--------------------------------------------------");
		// Math::max;和 (x, y) -> Math.max(x, y);都等同于是函数式BiFunction<Double, Double, Double>接口的实现类对象
		BiFunction<Double, Double, Double> fun2 = Math::max;
		// 通过函数式接口的实现类对象去调用该函数式接口的方法。
		System.out.println(fun2.apply(1.2, 1.5));
	}

	/**
	 * 第一种形式：使用Lambda表示和java8的方法的引用来实现等效的函数式接口的实现类对象，方法的引用通过对象的引用 :: 实例方法名来调用函数式接口实现类对象的方法
	 * 对象的引用 :: 实例方法名
	 */
	@Test
	public void test2(){
		Employee emp = new Employee(101, "张三", 18, 9999.99);
		
		Supplier<String> sup = () -> emp.getName();
		System.out.println(sup.get());
		
		System.out.println("----------------------------------");
		
		Supplier<String> sup2 = emp::getName;
		System.out.println(sup2.get());
	}
	
	/**
	 * JAVA8中方法的引用和Lambda表达式的关系：都是函数式接口的实现类对象，都是等效的。要实现功能都用“对象.方法”的方式去调用函数式接口的方法。
	 */
	@Test
	public void test1(){
		PrintStream ps = System.out;
		/**
		 * 用Lambda表达式实现：Lambda表达式也可以看做是左边这个函数式接口的实现类对象
		 */
		Consumer<String> con = (str) -> ps.println(str);
		// 使用对象.方法名去调用函数式接口的实现了的方法。
		con.accept("Hello World！");
		
		System.out.println("--------------------------------");
		
		/**
		 * 用Java8中方法的引用实现Lambda表达式的功能
		 * 用java8中的方法的引用去代替Lambda表达式，说明java8的方法的引用也是左边函数式接口的实现类对象
		 */
		Consumer<String> con2 = ps::println;
		/**
		 * 也通过实现类的对象.方法名去调用函数式接口的实现类对象的方法。
		 */
		con2.accept("Hello Java8！");
		
		Consumer<String> con3 = System.out::println;
		con3.accept("lisi");
	}
	
}
