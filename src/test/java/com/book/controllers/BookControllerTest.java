package com.book.controllers;

import com.book.entities.Book;
import com.book.services.BookService;
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
    public void getAllBooks_happy_path() {
        // Arrange
        List<Book> mockBooks = List.of(new Book("Title1", "Author1"), new Book("Title2", "Author2"));
        when(bookService.getAllBooks()).thenReturn(new ResponseEntity<>(mockBooks, HttpStatus.OK));

        // Act
        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).containsExactlyElementsOf(mockBooks);
    }

    @Test
    public void getBookById_happy_path() {
        // Arrange
        int bookId = 1;
        Book mockBook = new Book(bookId, "Effective Java", "Joshua Bloch");
        when(bookService.getBookById(bookId)).thenReturn(new ResponseEntity<>(mockBook, HttpStatus.OK));

        // Act
        ResponseEntity<Book> response = bookController.getBookById(bookId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(bookId);
        assertThat(response.getBody().getTitle()).isEqualTo("Effective Java");
        assertThat(response.getBody().getAuthor()).isEqualTo("Joshua Bloch");
    }

    @Test
    void getBookById_id_zero() {
        // Arrange
        ResponseEntity<?> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        when(bookService.getBookById(0)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> actualResponse = bookController.getBookById(0);

        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actualResponse.getBody()).isEqualTo("Book not found");
    }

    @Test
    void getBookById_id_negative() {
        // Arrange
        int negativeId = -1;
        when(bookService.getBookById(negativeId)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID"));

        // Act
        ResponseEntity<?> response = bookController.getBookById(negativeId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid ID");
    }

    @Test
    void getBookById_id_max_value() {
        // Arrange
        int id = Integer.MAX_VALUE;
        when(bookService.getBookById(id)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        // Act
        ResponseEntity<?> response = bookController.getBookById(id);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void addBook_happy_path() {
        // Arrange
        Book book = new Book("Title", "Author", "ISBN");
        when(bookService.addBook(book)).thenReturn(new ResponseEntity<>(book, HttpStatus.CREATED));

        // Act
        ResponseEntity<Book> response = bookController.addBook(book);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(book);
    }

    @Test
    void deleteBookById_happy_path() {
        // Arrange
        int bookId = 1;
        when(bookService.deleteBookById(bookId)).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        // Act
        ResponseEntity<?> responseEntity = bookController.deleteBookById(bookId);

        // Assert
        verify(bookService, times(1)).deleteBookById(bookId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteBookById_id_zero() {
        // Arrange
        int id = 0;
        when(bookService.deleteBookById(id)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        // Act
        ResponseEntity<?> responseEntity = bookController.deleteBookById(id);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBookById_id_negative() {
        // Arrange
        int negativeId = -1;
        when(bookService.deleteBookById(negativeId)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        // Act
        ResponseEntity<?> responseEntity = bookController.deleteBookById(negativeId);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteBookById_id_max_value() {
        // Arrange
        int id = Integer.MAX_VALUE;
        when(bookService.deleteBookById(id)).thenReturn(ResponseEntity.ok("Book deleted successfully"));

        // Act
        ResponseEntity<?> response = bookController.deleteBookById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Book deleted successfully");
    }

    @Test
    void updateBook_happy_path() {
        // Arrange
        Book book = new Book(); // Assuming Book has a default constructor
        int id = 1;
        when(bookService.updateBook(book, id)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> actualResponse = bookController.updateBook(book, id);

        // Assert
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateBook_id_zero() {
        // Arrange
        Book book = new Book(); // Assuming Book has a no-argument constructor
        int id = 0;
        when(bookService.updateBook(book, id)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        // Act
        ResponseEntity<?> response = bookController.updateBook(book, id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void updateBook_id_negative() {
        // Arrange
        Book book = new Book();
        int invalidId = -1;
        when(bookService.updateBook(book, invalidId)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        // Act
        ResponseEntity<?> response = bookController.updateBook(book, invalidId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateBook_id_max_value() {
        // Arrange
        Book book = new Book(); // Assume Book class has a default constructor
        int maxId = Integer.MAX_VALUE;
        when(bookService.updateBook(book, maxId)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        // Act
        ResponseEntity<?> response = bookController.updateBook(book, maxId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteAllBooks_happy_path() {
        // Arrange
        doNothing().when(bookService).deleteAllBooks();

        // Act
        ResponseEntity<?> responseEntity = bookController.deleteAllBooks();

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(bookService, times(1)).deleteAllBooks();
    }

    @Test
    public void addPublisherToExistingBooks_happy_path() {
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
        when(bookService.getAllLibraries()).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> actualResponse = bookController.getAllLibraries();

        // Assert
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAuditHistory_happy_path() {
        // Given
        int bookId = 1;
        List<ApplicationAuditing> mockAuditHistory = Arrays.asList(
                new ApplicationAuditing("Audit1"),
                new ApplicationAuditing("Audit2")
        );
        when(bookService.getAuditHistory(bookId)).thenReturn(mockAuditHistory);

        // When
        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(bookId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockAuditHistory);
    }

    @Test
    void getAuditHistory_bookId_zero() {
        // Arrange
        int bookId = 0;
        List<ApplicationAuditing> expectedAuditHistory = Collections.emptyList();
        when(bookService.getAuditHistory(eq(bookId))).thenReturn(expectedAuditHistory);

        // Act
        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(bookId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedAuditHistory);
    }

    @Test
    void getAuditHistory_bookId_negative() {
        // Arrange
        int negativeBookId = -1;
        when(bookService.getAuditHistory(negativeBookId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(negativeBookId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_max_value() {
        // Arrange
        int bookId = Integer.MAX_VALUE;
        List<ApplicationAuditing> expectedAuditList = Collections.emptyList();
        when(bookService.getAuditHistory(bookId)).thenReturn(expectedAuditList);

        // Act
        ResponseEntity<List<ApplicationAuditing>> responseEntity = bookController.getAuditHistory(bookId);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedAuditList);
    }
}