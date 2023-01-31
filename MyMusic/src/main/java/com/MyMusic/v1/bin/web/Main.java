package com.MyMusic.v1.bin.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@SpringBootApplication(scanBasePackages = {
		"com.MyMusic.v1.beans",
		"com.MyMusic.v1.auth",
		"com.MyMusic.v1.api",
		"com.MyMusic.v1.payments"
})
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
