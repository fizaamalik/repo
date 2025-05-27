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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
        // Arrange
        Book book1 = new Book("Title1", "Author1");
        Book book2 = new Book("Title2", "Author2");
        List<Book> mockBooks = Arrays.asList(book1, book2);
        when(bookService.getAllBooks()).thenReturn(new ResponseEntity<>(mockBooks, HttpStatus.OK));

        // Act
        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).containsExactly(book1, book2);
    }

    @Test
    void getBookById_happy_path() {
        // Arrange
        int bookId = 1;
        Book mockBook = new Book("Mock Title", "Mock Author");
        when(bookService.getBookById(bookId)).thenReturn(new ResponseEntity<>(mockBook, HttpStatus.OK));

        // Act
        ResponseEntity<Book> responseEntity = bookController.getBookById(bookId);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(mockBook);
    }

    @Test
    void getBookById_id_zero() {
        // Arrange
        int testId = 0;

        // Act
        ResponseEntity<?> responseEntity = bookController.getBookById(testId);

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void getBookById_id_negative() {
        // Arrange
        int negativeId = -1;
        when(bookService.getBookById(negativeId)).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = bookController.getBookById(negativeId);

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getBookById_id_max_value() {
        // Arrange
        int maxId = Integer.MAX_VALUE;
        when(bookService.getBookById(maxId)).thenReturn(null);

        // Act
        ResponseEntity<Book> response = bookController.getBookById(maxId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void addBook_happy_path() {
        // Arrange
        Book book = new Book("Effective Java", "Joshua Bloch");
        when(bookService.addBook(book)).thenReturn(ResponseEntity.ok("Book added successfully"));

        // Act
        ResponseEntity<?> actualResponse = bookController.addBook(book);

        // Assert
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo("Book added successfully");
    }

    @Test
    void deleteBookById_happy_path() {
        // Given
        int bookId = 1;
        when(bookService.deleteBookById(bookId)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        // When
        ResponseEntity<Void> response = bookController.deleteBookById(bookId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteBookById_id_zero() {
        // Arrange
        int id = 0;
        when(bookService.deleteBookById(id)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // Act
        ResponseEntity<Void> response = bookController.deleteBookById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBookById_id_negative() {
        // Arrange
        int negativeId = -1;
        when(bookService.deleteBookById(negativeId)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Act
        ResponseEntity<?> response = bookController.deleteBookById(negativeId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteBookById_id_max_value() {
        // Arrange
        int id = Integer.MAX_VALUE;
        when(bookService.deleteBookById(id)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> responseEntity = bookController.deleteBookById(id);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void updateBook_happy_path() {
        // Arrange
        Book book = new Book();
        book.setId(1);
        book.setTitle("Updated Title");
        int id = 1;
        when(bookService.updateBook(any(Book.class), eq(id))).thenReturn(new ResponseEntity<>(book, HttpStatus.OK));

        // Act
        ResponseEntity<Book> responseEntity = bookController.updateBook(book, id);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(book.getId());
        assertThat(responseEntity.getBody().getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void updateBook_id_zero() {
        // Arrange
        Book book = new Book();
        int id = 0;
        when(bookService.updateBook(book, id)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Act
        ResponseEntity<?> response = bookController.updateBook(book, id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateBook_id_negative() {
        // Arrange
        Book book = new Book();
        int negativeId = -1;
        when(bookService.updateBook(book, negativeId)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Act
        ResponseEntity<?> response = bookController.updateBook(book, negativeId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateBook_id_max_value() {
        // Arrange
        Book book = new Book();
        int maxId = Integer.MAX_VALUE;
        when(bookService.updateBook(book, maxId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> responseEntity = bookController.updateBook(book, maxId);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteAllBooks_happy_path() {
        // Arrange
        when(bookService.deleteAllBooks()).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> actualResponse = bookController.deleteAllBooks();

        // Assert
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addPublisherToExistingBooks_happy_path() {
        // Arrange
        when(bookService.addPublisherToExistingBooks()).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = bookController.addPublisherToExistingBooks();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllLibraries_happy_path() {
        // Arrange
        List<Library> mockLibraries = Arrays.asList(new Library("Library1"), new Library("Library2"));
        when(bookService.getAllLibraries()).thenReturn(new ResponseEntity<>(mockLibraries, HttpStatus.OK));

        // Act
        ResponseEntity<List<Library>> response = bookController.getAllLibraries();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockLibraries);
    }

    @Test
    void getAuditHistory_happy_path() {
        // Arrange
        int bookId = 1;
        List<ApplicationAuditing> expectedAuditList = List.of(new ApplicationAuditing(), new ApplicationAuditing());
        when(bookService.getAuditHistory(bookId)).thenReturn(expectedAuditList);

        // Act
        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(bookId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedAuditList);
    }

    @Test
    void getAuditHistory_bookId_zero() {
        // Arrange
        int bookId = 0;
        List<ApplicationAuditing> expectedAuditHistory = Collections.emptyList();
        when(bookService.getAuditHistory(bookId)).thenReturn(expectedAuditHistory);

        // Act
        ResponseEntity<List<ApplicationAuditing>> actualResponse = bookController.getAuditHistory(bookId);

        // Assert
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_negative() {
        // Arrange
        int negativeBookId = -1;
        when(bookService.getAuditHistory(negativeBookId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(negativeBookId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_max_value() {
        // Arrange
        int bookId = Integer.MAX_VALUE;
        List<ApplicationAuditing> expectedAuditHistory = Collections.emptyList();
        when(bookService.getAuditHistory(bookId)).thenReturn(expectedAuditHistory);

        // Act
        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(bookId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedAuditHistory);
    }
}