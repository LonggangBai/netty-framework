/**
 * 
 */
package com.easyway.netty.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * 测试spring 整合之后的netty
 * 
 * * 测试内容如下： http://127.0.0.1:8080/sdf?username=test1&password=1
 * bbd886460827015e5d605ed44252221
 * 
 * @author longgangbai 2015-1-19 上午9:36:11
 */
public class SpringHttpServer {
    public static void main(String[] args) {
	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "classpath:applicationContext.xml" });

    }
}
