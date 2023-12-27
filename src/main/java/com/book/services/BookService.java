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

//

    public ResponseEntity getAllBooks() {
        List<Book> list = bookRepo.findAll();
        try {
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No books found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(list);
    }

    public ResponseEntity getBookById(int id) {

        try {
            if (bookRepo.existsById(id)) {
                Book book = bookRepo.findById(id);
                return ResponseEntity.ok(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book does't exist by given id ");

    }

    public ResponseEntity addBook(Book book) {
        try {
            if (book.getLibrary() == null) {
                throw new IllegalArgumentException("Library is mandatory for book creation.");
            }
            if (book.getPublisher() == null) {
                throw new IllegalArgumentException("Publisher is mandatory for book creation.");
            }

            Book b = bookRepo.save(book);
            createAuditEntry(b, "system", "Create");

            return ResponseEntity.ok(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid attribute values");
    }


    public ResponseEntity deleteBookById(int id) {
        try {
            if (bookRepo.existsById(id)) {
                createAuditEntry(bookRepo.findById(id),"system", "Delete");
                bookRepo.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Successfully deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleting the book");
    }

    public ResponseEntity updateBook(Book book, int id) {
        try {
            Book existingBook = bookRepo.findById(id);
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPublisher(book.getPublisher());
            existingBook.setLibrary(book.getLibrary());
            existingBook.setLastModified(LocalDateTime.now());
            createAuditEntry(existingBook, "system", "Update");
            bookRepo.save(existingBook);

            return ResponseEntity.status(HttpStatus.OK).body(existingBook);

    } catch(Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

}
        @Transactional
        public ResponseEntity deleteAllBooks() {


        List<Book> deletedBooks = bookRepo.findAll();
            for (Book deletedBook : deletedBooks) {
                createAuditEntry(deletedBook, "system", "DELETE");
            }
            bookRepo.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted Successfully");

    }

    public ResponseEntity addPublisherToExistingBooks() {
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
        try {
            if (libraries.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(libraries);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
