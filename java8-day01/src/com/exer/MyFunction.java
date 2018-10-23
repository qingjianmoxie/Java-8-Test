package com.exer;
/**
 * 一、Lambda表达式的基础语法：java8中引入了一个新的操作符“->”该操作符称为箭头操作符或Lambda操作符：
 *             箭头操作符将Lambda表达式拆分成两部分：
 *                   左侧：Lambda表达式的参数列表；
 *                   右侧：Lambda表达式中所需要执行的功能，即Lambda体。
 *                   
 * 函数式接口：接口中只用一个抽象方法的接口叫做函数式接口。
 *                Lambda表达式的语法格式：
 *                      语法格式1：无参数，无返回值
 *                          () -> System.out.printlin("hello lambda");
 *                         
 *                      
 *                      
 * @author zhouzhigang
 *
 */
@FunctionalInterface
public interface MyFunction {
	
	public String getValue(String str);

}
