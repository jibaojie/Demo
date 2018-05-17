package com.example.demo;

import com.example.demo.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);


	public static void main(String[] args) {

		ApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
		//将applicationContext属性注入SpringContextHolder，后面static方法调用
		SpringContextHolder.setApplicationContext(applicationContext);
		logger.info("Registry ApplicationContext");
	}
}

