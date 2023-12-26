package com.book.controllers;

import com.book.entities.Book;
import com.book.entities.HistoricalBook;
import com.book.entities.Library;
import com.book.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/books")
    public ResponseEntity getAllBooks(Book book){
        return this.bookService.getAllBooks();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity getBookById(@PathVariable int id){
        return this.bookService.getBookById(id);
    }

    @PostMapping("/books")
    public ResponseEntity addBook(@RequestBody Book book){
        return this.bookService.addBook(book);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity deleteBookById(@PathVariable int id ){
        return this.bookService.deleteBookById(id);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity updateBook(@RequestBody Book book, @PathVariable int id){
        return this.bookService.updateBook(book, id);
    }

    @DeleteMapping("/books")
    public ResponseEntity deleteAllBooks(){
        return this.bookService.deleteAllBooks();
    }

    @PostMapping("/books/add-publisher")
    public ResponseEntity addPublisherToExistingBooks() {
        return this.bookService.addPublisherToExistingBooks();
    }

    @PostMapping("/library")
    public ResponseEntity addLibrary(@RequestBody Library library){
        return this.bookService.addLibrary(library);
    }

    @GetMapping("/libraries")
    public ResponseEntity getAllLibraries() {
        return this.bookService.getAllLibraries();
    }

    @PostMapping("/history-save")
    public void saveBook(@RequestBody Book book) {
        bookService.saveHistoricalBooks(book);

    }

}
