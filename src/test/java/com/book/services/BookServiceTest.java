package com.book.services;

import com.book.dao.AuditRepo;
import com.book.dao.BookRepo;
import com.book.dao.LibraryRepository;
import com.book.entities.Book;
import com.book.entities.Library;
import com.book.entities.Publisher;
import com.book.auditing.ApplicationAuditing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @Mock
    private AuditRepo bookAuditRepository;

    @Mock
    private LibraryRepository libraryRepo;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_happy_path() {
        List<Book> expectedBooks = Arrays.asList(new Book("Title1", "Author1"), new Book("Title2", "Author2"));
        when(bookRepo.findAll()).thenReturn(expectedBooks);

        ResponseEntity<List<Book>> actualResponse = bookService.getAllBooks();

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).containsExactlyElementsOf(expectedBooks);
    }

    @Test
    void getAllBooks_conditional_logic() {
        Book book1 = new Book("Title1", "Author1");
        Book book2 = new Book("Title2", "Author2");
        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepo.findAll()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookService.getAllBooks();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).containsExactlyInAnyOrder(book1, book2);
    }

    @Test
    void getBookById_happy_path() {
        int bookId = 1;
        Book book = new Book();
        when(bookRepo.findById(bookId)).thenReturn(Optional.of(book));

        ResponseEntity<?> response = bookService.getBookById(bookId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getBookById_id_zero() {
        int id = 0;
        when(bookRepo.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.getBookById(id);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBookById_id_negative() {
        int negativeId = -1;
        when(bookRepo.findById(negativeId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.getBookById(negativeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Book doesn't exist by given id");
    }

    @Test
    void getBookById_id_max_value() {
        int id = Integer.MAX_VALUE;
        when(bookRepo.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.getBookById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBookById_conditional_logic() {
        when(bookRepo.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = bookService.getBookById(1);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        when(bookRepo.findById(anyInt())).thenReturn(Optional.of(new Book()));

        responseEntity = bookService.getBookById(1);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addBook_happy_path() {
        Book book = new Book();
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        ResponseEntity<?> response = bookService.addBook(book);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void addBook_conditional_logic() {
        Book book = new Book();
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setLibrary(new Library());
        book.setPublisher(new Publisher());

        when(bookRepo.save(book)).thenReturn(book);

        ResponseEntity<?> response = bookService.addBook(book);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteBookById_happy_path() {
        int bookId = 1;
        when(bookRepo.findById(bookId)).thenReturn(Optional.of(new Book()));
        doNothing().when(bookRepo).deleteById(bookId);

        ResponseEntity<?> response = bookService.deleteBookById(bookId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(bookRepo, times(1)).deleteById(bookId);
    }

    @Test
    void deleteBookById_id_zero() {
        int id = 0;
        when(bookRepo.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.deleteBookById(id);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBookById_id_negative() {
        int negativeId = -1;
        when(bookRepo.findById(negativeId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.deleteBookById(negativeId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBookById_id_max_value() {
        int id = Integer.MAX_VALUE;
        when(bookRepo.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.deleteBookById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBookById_conditional_logic() {
        int bookId = 1;
        when(bookRepo.findById(bookId)).thenReturn(Optional.of(new Book()));

        ResponseEntity<?> response = bookService.deleteBookById(bookId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void updateBook_happy_path() {
        Book book = new Book();
        int id = 1;
        when(bookRepo.findById(id)).thenReturn(Optional.of(book));
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        ResponseEntity<?> response = bookService.updateBook(book, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bookRepo, times(1)).save(book);
    }

    @Test
    void updateBook_id_zero() {
        Book book = new Book();
        int id = 0;

        ResponseEntity<?> responseEntity = bookService.updateBook(book, id);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateBook_id_negative() {
        Book book = new Book();
        int negativeId = -1;

        ResponseEntity<?> response = bookService.updateBook(book, negativeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateBook_id_max_value() {
        Book book = new Book();
        int id = Integer.MAX_VALUE;
        when(bookRepo.findById(id)).thenReturn(Optional.of(book));
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        ResponseEntity<?> response = bookService.updateBook(book, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteAllBooks_happy_path() {
        doNothing().when(bookRepo).deleteAll();

        ResponseEntity<?> response = bookService.deleteAllBooks();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void addPublisherToExistingBooks_happy_path() {
        List<Book> books = List.of(new Book("Book1"), new Book("Book2"));
        when(bookRepo.findAll()).thenReturn(books);

        ResponseEntity<?> responseEntity = bookService.addPublisherToExistingBooks();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bookRepo, times(1)).findAll();
    }

    @Test
    void getAllLibraries_happy_path() {
        List<Library> mockLibraries = List.of(new Library("Library A"), new Library("Library B"));
        when(libraryRepo.findAll()).thenReturn(mockLibraries);

        ResponseEntity<List<Library>> response = bookService.getAllLibraries();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2).containsExactlyElementsOf(mockLibraries);
    }

    @Test
    void getAllLibraries_conditional_logic() {
        Library library1 = new Library("Library A");
        Library library2 = new Library("Library B");
        when(libraryRepo.findAll()).thenReturn(Arrays.asList(library1, library2));

        ResponseEntity<List<Library>> responseEntity = bookService.getAllLibraries();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().hasSize(2);
        assertThat(responseEntity.getBody()).containsExactly(library1, library2);
    }

    @Test
    void createAuditEntry_happy_path() {
        Book book = new Book();
        String modifiedBy = "user123";
        String actionType = "CREATE";

        doNothing().when(bookAuditRepository).save(any(ApplicationAuditing.class));

        assertThatCode(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .doesNotThrowAnyException();
    }

    @Test
    void getAuditHistory_happy_path() {
        int bookId = 1;
        List<ApplicationAuditing> expectedAuditHistory = List.of(new ApplicationAuditing());
        when(bookAuditRepository.findByBookIdOrderByCreateDate(bookId)).thenReturn(expectedAuditHistory);

        List<ApplicationAuditing> actualAuditHistory = bookService.getAuditHistory(bookId);

        assertThat(actualAuditHistory).isEqualTo(expectedAuditHistory);
    }

    @Test
    void getAuditHistory_bookId_zero() {
        when(bookAuditRepository.findByBookIdOrderByCreateDate(0)).thenReturn(Collections.emptyList());

        List<ApplicationAuditing> result = bookService.getAuditHistory(0);

        assertThat(result).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_negative() {
        int negativeBookId = -1;
        when(bookAuditRepository.findByBookIdOrderByCreateDate(negativeBookId)).thenReturn(Collections.emptyList());

        List<ApplicationAuditing> result = bookService.getAuditHistory(negativeBookId);

        assertThat(result).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_max_value() {
        List<ApplicationAuditing> expectedAuditHistory = List.of(
                new ApplicationAuditing(),
                new ApplicationAuditing()
        );
        when(bookAuditRepository.findByBookIdOrderByCreateDate(Integer.MAX_VALUE)).thenReturn(expectedAuditHistory);

        List<ApplicationAuditing> actualAuditHistory = bookService.getAuditHistory(Integer.MAX_VALUE);

        assertThat(actualAuditHistory).isNotNull().hasSize(expectedAuditHistory.size())
                .containsExactlyElementsOf(expectedAuditHistory);
    }
}