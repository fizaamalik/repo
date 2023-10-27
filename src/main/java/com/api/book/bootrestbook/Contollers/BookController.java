package com.api.book.bootrestbook.Contollers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.book.bootrestbook.entities.Book;
import com.api.book.bootrestbook.services.BookServices;

@RestController
public class BookController {
	
	@Autowired
	private BookServices bookservice;
	//@RequestMapping(value="/books", method=RequestMethod.GET)
	@GetMapping("/books")
	public ResponseEntity<List<Book>> getBooks() {
		
		return bookservice.getBooks();
	}
	@GetMapping("/book/{id}")
	public ResponseEntity<Book> getBook(@PathVariable("id") int id) {
		
	
		return bookservice.getBook(id);
	}
	@PostMapping("/books")
	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		
		return bookservice.addBook(book);
		
	}
	@DeleteMapping("/books/{bookId}")
	public ResponseEntity<Book> deleteBook(@PathVariable("bookId") int bookId){
		
		return bookservice.deleteBook(bookId);
		
	}
	@PutMapping("/books/{bookId}")
	public ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable("bookId") int bookId) {
		return bookservice.updateBook(book, bookId);
		
	}
}
