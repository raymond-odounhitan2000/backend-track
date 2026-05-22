package com.it_num.crud_api;

import org.springframework.boot.SpringApplication;

public class TestCrudApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(CrudApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
