 package com.api.book.bootrestbook.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.book.bootrestbook.dao.BookRepository;
import com.api.book.bootrestbook.entities.Book;
@Component
public class BookServices {
//	private static List<Book> list = new ArrayList<>();
//	static {
//		list.add(new Book(12,"java", "xyz"));
//		list.add(new Book(13, "python", "abc"));
//		list.add(new Book(14,"c++", "ghj"));
//		list.add(new Book(15, "ruby", "iop"));
//	}
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
	public Book addBook(Book b) {
		//Author author = authorrepo.save(b.author);
		Book result = bookrepo.save(b);
		return result;
	}
	public void deleteBook(int bid) {
		bookrepo.deleteById(bid);
	}
	public void updateBook(Book book, int bookId) {
		book.setId(bookId);
		bookrepo.save(book);
	}
}
