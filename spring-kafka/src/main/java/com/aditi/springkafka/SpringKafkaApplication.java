package com.aditi.springkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SpringKafkaApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringKafkaApplication.class, args);

	}

}
