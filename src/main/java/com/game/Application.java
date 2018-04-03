package com.game;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * CoreService MAIN
 *
 * Some Help info:
 * 1.https://github.com/mybatis/generator  //mybatis mapper generator
 */

/**
 * API入口 class:IndexController
 *
 * web socket 入口  class:DispatcherHandler
 */

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("com.game")
@ServletComponentScan
@MapperScan("com.game.repository.mybatis")
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		DataContext.setApplicationContext(springApplication.run(args));
	}
	
}
