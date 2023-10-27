 package com.api.book.bootrestbook.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.book.bootrestbook.dao.BookRepository;
import com.api.book.bootrestbook.entities.Book;
@Component
public class BookServices {

	@Autowired
	private BookRepository bookrepo;
	//get all books
	public List<Book> getAllBooks() {
		List<Book> list = (List<Book>) this.bookrepo.findAll();
		return list;
		
	}
	//get book by ID
	public Book getBookById(int id){
		Book b = null;
		try {
			b = this.bookrepo.findById(id);
		}
		catch(Exception e ) {
			e.printStackTrace();
		}
		return b;
	}
	public Book addBooks(Book b) {
		//Author author = authorrepo.save(b.author);
		Book result = bookrepo.save(b);
		return result;
	}
	public void deleteBooks(int bid) {
		bookrepo.deleteById(bid);
	}
	public void updateBooks(Book book, int bookId) {
		book.setId(bookId);
		bookrepo.save(book);
	}
	

	public ResponseEntity<List<Book>> getBooks() {
		List<Book> list = getAllBooks();
		if(list.size()<=0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(list);
	}

	public ResponseEntity<Book> getBook(@PathVariable("id") int id) {
		Book book = getBookById(id);
		if(book==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.of(Optional.of(book));
	}

	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		Book b = null;
		try {
		b = addBooks(book);
		return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		catch(Exception e ) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}

	public ResponseEntity<Book> deleteBook(@PathVariable("bookId") int bookId){
		try {
		deleteBooks(bookId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		catch(Exception e ) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
	}
	
	public ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable("bookId") int bookId) {
		try {
		updateBooks(book, bookId);
		return ResponseEntity.ok().body(book);
		}
		catch(Exception e ) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
	}
}
