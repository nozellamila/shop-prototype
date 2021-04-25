package com.shopprototype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ShopPrototypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopPrototypeApplication.class, args);

		System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}


}
