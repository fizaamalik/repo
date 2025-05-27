package com.book.services;

import com.book.auditing.ApplicationAuditing;
import com.book.dao.AuditRepo;
import com.book.dao.BookRepo;
import com.book.dao.LibraryRepository;
import com.book.entities.Book;
import com.book.entities.Library;
import com.book.entities.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepo bookRepo;

    private final AuditRepo bookAuditRepository;

    @Autowired
    public BookService(AuditRepo bookAuditRepository) {
        this.bookAuditRepository = bookAuditRepository;
    }

    @Autowired
    private LibraryRepository libraryRepo;

    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> list = bookRepo.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(list);
        }
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getBookById(int id) {
        Optional<Book> optionalBook = Optional.ofNullable(bookRepo.findById(id));
        if (optionalBook.isPresent()) {
            return ResponseEntity.ok(optionalBook.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book doesn't exist by given id");
    }

    @Transactional
    public ResponseEntity<?> addBook(Book book) {
        try {
            if (book.getLibrary() == null) {
                return ResponseEntity.badRequest().body("Library is mandatory for book creation.");
            }
            if (book.getPublisher() == null) {
                return ResponseEntity.badRequest().body("Publisher is mandatory for book creation.");
            }
            Book savedBook = bookRepo.save(book);
            createAuditEntry(savedBook, "system", "Create");
            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid attribute values or error occurred.");
        }
    }

    @Transactional
    public ResponseEntity<?> deleteBookById(int id) {
        Optional<Book> optionalBook = Optional.ofNullable(bookRepo.findById(id));
        if (optionalBook.isPresent()) {
            Book bookToDelete = optionalBook.get();
            createAuditEntry(bookToDelete, "system", "Delete");
            bookRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Successfully deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found with id: " + id);
    }

    @Transactional
    public ResponseEntity<?> updateBook(Book book, int id) {
        try {
            Optional<Book> optionalBook = Optional.ofNullable(bookRepo.findById(id));
            if (optionalBook.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found with id: " + id);
            }
            Book existingBook = optionalBook.get();
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPublisher(book.getPublisher());
            existingBook.setLibrary(book.getLibrary());
            existingBook.setLastModified(LocalDateTime.now());
            createAuditEntry(existingBook, "system", "Update");
            Book updatedBook = bookRepo.save(existingBook);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @Transactional
    public ResponseEntity<?> deleteAllBooks() {
        List<Book> booksToDelete = bookRepo.findAll();
        for (Book book : booksToDelete) {
            createAuditEntry(book, "system", "Delete");
        }
        bookRepo.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted Successfully");
    }

    @Transactional
    public ResponseEntity<?> addPublisherToExistingBooks() {
        try {
            List<Book> books = bookRepo.findAll();
            for (Book book : books) {
                if (book.getPublisher() == null) {
                    Publisher publisher = new Publisher();
                    publisher.setPublisher_name("XYZ");
                    book.setPublisher(publisher);
                }
            }
            bookRepo.saveAll(books);
            return ResponseEntity.ok("Publisher 'XYZ' added to existing books.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding publisher to existing books");
        }
    }

    public ResponseEntity<List<Library>> getAllLibraries() {
        List<Library> libraries = libraryRepo.findAll();
        if (libraries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(libraries);
        }
        return ResponseEntity.ok(libraries);
    }

    @Transactional
    public void createAuditEntry(Book book, String modifiedBy, String actionType) {
        ApplicationAuditing bookAudit = new ApplicationAuditing(book, modifiedBy, actionType);
        bookAuditRepository.save(bookAudit);
    }

    public List<ApplicationAuditing> getAuditHistory(int bookId) {
        return bookAuditRepository.findByBookIdOrderByCreateDate(bookId);
    }
}
