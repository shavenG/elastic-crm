package com.al.crm.elastic.dataflow;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args ){
		new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
    }
}
