package com.book;

import com.book.dao.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBookLibraryApplication {



	public static void main(String[] args) {
		SpringApplication.run(SpringBookLibraryApplication.class, args);

	}

}
