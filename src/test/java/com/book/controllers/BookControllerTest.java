package com.book.controllers;

import com.book.auditing.ApplicationAuditing;
import com.book.entities.Book;
import com.book.entities.Library;
import com.book.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_happy_path() {
        List<Book> mockBooks = Arrays.asList(new Book("Title1", "Author1"), new Book("Title2", "Author2"));
        ResponseEntity<List<Book>> expectedResponse = new ResponseEntity<>(mockBooks, HttpStatus.OK);
        when(bookService.getAllBooks()).thenReturn(expectedResponse);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockBooks);
    }

    @Test
    void getBookById_happy_path() {
        int bookId = 1;
        Book mockBook = new Book(bookId, "Mock Title", "Mock Author");
        when(bookService.getBookById(bookId)).thenReturn(new ResponseEntity<>(mockBook, HttpStatus.OK));

        ResponseEntity<Book> response = bookController.getBookById(bookId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockBook);
    }

    @Test
    void getBookById_id_zero() {
        int id = 0;
        when(bookService.getBookById(id)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<?> response = bookController.getBookById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getBookById_id_negative() {
        int negativeId = -1;
        when(bookService.getBookById(negativeId)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        ResponseEntity<?> response = bookController.getBookById(negativeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getBookById_id_max_value() {
        int id = Integer.MAX_VALUE;
        when(bookService.getBookById(eq(id))).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<?> responseEntity = bookController.getBookById(id);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void addBook_happy_path() {
        Book book = new Book("Title", "Author", 2023);
        when(bookService.addBook(book)).thenReturn(new ResponseEntity<>(book, HttpStatus.CREATED));

        ResponseEntity<Book> responseEntity = bookController.addBook(book);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(book);
    }

    @Test
    void deleteBookById_happy_path() {
        int bookId = 1;
        doNothing().when(bookService).deleteBookById(bookId);
        when(bookService.deleteBookById(bookId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = bookController.deleteBookById(bookId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteBookById_id_zero() {
        int id = 0;
        when(bookService.deleteBookById(id)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<Void> actualResponse = bookController.deleteBookById(id);

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBookById_id_negative() {
        int negativeId = -1;
        when(bookService.deleteBookById(negativeId)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        ResponseEntity<?> response = bookController.deleteBookById(negativeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteBookById_id_max_value() {
        int maxId = Integer.MAX_VALUE;
        when(bookService.deleteBookById(maxId)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<Void> actualResponse = bookController.deleteBookById(maxId);

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateBook_happy_path() {
        int bookId = 1;
        Book book = new Book("Effective Java", "Joshua Bloch", 2008);
        when(bookService.updateBook(book, bookId)).thenReturn(new ResponseEntity<>(book, HttpStatus.OK));

        ResponseEntity<Book> response = bookController.updateBook(book, bookId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(book);
    }

    @Test
    void updateBook_id_zero() {
        Book book = new Book();
        int id = 0;
        when(bookService.updateBook(book, id)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        ResponseEntity<Void> response = bookController.updateBook(book, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateBook_id_negative() {
        Book book = new Book();
        int negativeId = -1;
        when(bookService.updateBook(book, negativeId)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        ResponseEntity<?> response = bookController.updateBook(book, negativeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateBook_id_max_value() {
        Book book = new Book();
        int id = Integer.MAX_VALUE;

        when(bookService.updateBook(book, id)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> responseEntity = bookController.updateBook(book, id);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteAllBooks_happy_path() {
        when(bookService.deleteAllBooks()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = bookController.deleteAllBooks();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addPublisherToExistingBooks_happy_path() {
        when(bookService.addPublisherToExistingBooks()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = bookController.addPublisherToExistingBooks();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllLibraries_happy_path() {
        List<Library> mockLibraries = List.of(new Library(1, "Central Library"), new Library(2, "Community Library"));
        when(bookService.getAllLibraries()).thenReturn(new ResponseEntity<>(mockLibraries, HttpStatus.OK));

        ResponseEntity<List<Library>> responseEntity = bookController.getAllLibraries();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(mockLibraries);
    }

    @Test
    void getAuditHistory_happy_path() {
        int bookId = 1;
        List<ApplicationAuditing> mockAuditHistory = List.of(new ApplicationAuditing(), new ApplicationAuditing());

        when(bookService.getAuditHistory(bookId)).thenReturn(mockAuditHistory);

        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(bookId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockAuditHistory);
    }

    @Test
    void getAuditHistory_bookId_zero() {
        int bookId = 0;
        List<ApplicationAuditing> emptyAuditList = Collections.emptyList();

        when(bookService.getAuditHistory(bookId)).thenReturn(emptyAuditList);

        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(bookId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_negative() {
        int negativeBookId = -1;
        when(bookService.getAuditHistory(negativeBookId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(negativeBookId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_max_value() {
        int bookId = Integer.MAX_VALUE;
        List<ApplicationAuditing> expectedAuditHistory = Collections.emptyList();
        when(bookService.getAuditHistory(eq(bookId))).thenReturn(expectedAuditHistory);

        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(bookId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedAuditHistory);
    }
}