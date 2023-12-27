package com.book.controllers;

import com.book.auditing.ApplicationAuditing;
import com.book.entities.Book;
import com.book.entities.Library;
import com.book.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity getAllBooks(){
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

    @GetMapping("/libraries")
    public ResponseEntity getAllLibraries() {
        return this.bookService.getAllLibraries();
    }

    @GetMapping("/{bookId}/audit-history")
    public ResponseEntity<List<ApplicationAuditing>> getAuditHistory(@PathVariable int bookId) {
        List<ApplicationAuditing> auditHistory = bookService.getAuditHistory(bookId);
        return new ResponseEntity<>(auditHistory, HttpStatus.OK);
    }

//    @PostMapping("/library")
//    public ResponseEntity addLibrary(@RequestBody Library library){
//        return this.bookService.addLibrary(library);
//    }
//


//    @PostMapping("/history-save")
//    public void saveBook(@RequestBody Book book) {
//        bookService.saveHistoricalBooks(book);
//
//    }

}
