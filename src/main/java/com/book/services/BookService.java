package com.book.services;

import com.book.dao.BookRepo;
import com.book.dao.HistoricBookRepo;
import com.book.dao.LibraryRepository;
import com.book.entities.Book;
import com.book.entities.HistoricalBook;
import com.book.entities.Library;
import com.book.entities.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Autowired
    private LibraryRepository libraryRepo;

    @Autowired
    private HistoricBookRepo historicBookRepo;

    public ResponseEntity getAllBooks() {
        List<Book> list = this.bookRepo.findAll();
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

            //book.setLibrary(checkLibraryByName(book.getLibrary()));
//            System.out.println(book.getLibrary());

            Book b = bookRepo.save(book);


            return ResponseEntity.ok(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid attribute values");
    }

//    public Library checkLibraryByName(Library library) {
//
////        Optional<Library> lib = libraryRepo.findByName(library.getLibraryName());
//        //return lib.orElseGet(() -> createDefaultLibrary(lib.get().getLibraryName()));
//        Library lib = libraryRepo.findDistinctByLibraryName(library.getLibraryName());
//        if (lib == null) {
//            Library lib1 = new Library();
//            lib1.setLibraryName(library.getLibraryName());
//            return lib1;
//        }
//        return lib;
//    }

//    private Library createDefaultLibrary(String libName) {
//        Library lib1 = new Library();
//        lib1.setLibraryName(libName);
//        return libraryRepo.save(lib1);
//    }

    public ResponseEntity deleteBookById(int id) {
        try {
            if (bookRepo.existsById(id)) {
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

            bookRepo.save(book);
//            HistoricalBook hbook = (HistoricalBook) book.getHistoricalBooks();
//            historicBookRepo.save(hbook);

            return ResponseEntity.status(HttpStatus.OK).body(existingBook);

    } catch(Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

}

        public ResponseEntity deleteAllBooks() {

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

    public ResponseEntity addLibrary(Library library) {
        try {
            Library savedLibrary = libraryRepo.save(library);
            return ResponseEntity.ok(savedLibrary);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid attribute values");
        }
    }

    public void saveHistoricalBooks(Book book) {
        historicBookRepo.saveAll(book.getHistoricalBooks());
    }

//    public ResponseEntity set20name() {
//        try{
//            bookRepo.set20name();
//            return ResponseEntity.ok("done");
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }


}
