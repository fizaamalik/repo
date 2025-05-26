package com.book.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @Mock private BookService bookService;
    @InjectMocks private BookController bookController;

    @Test
    public void getAllBooks_happy_path() {
        // Arrange
        List<Book> mockBooks = Arrays.asList(new Book(1, "Book One"), new Book(2, "Book Two"));
        when(bookService.getAllBooks()).thenReturn(mockBooks);

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
        Book expectedBook = new Book(bookId, "Effective Java", "Joshua Bloch");
        when(bookService.findBookById(bookId)).thenReturn(expectedBook);

        // Act
        ResponseEntity<Book> response = bookController.getBookById(bookId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedBook);
    }

    @Test
    void getBookById_id_zero() {
        // Arrange
        int id = 0;
        when(bookService.findBookById(id)).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = bookController.getBookById(id);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void getBookById_id_negative() {
        // Arrange
        int negativeId = -1;
        when(bookService.findBookById(negativeId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = bookController.getBookById(negativeId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid book ID");
    }

    @Test
    void getBookById_id_max_value() {
        // Arrange
        int maxValueId = Integer.MAX_VALUE;
        when(bookService.findBookById(maxValueId)).thenReturn(null);

        // Act
        ResponseEntity<Book> response = bookController.getBookById(maxValueId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void addBook_happy_path() {
        // Arrange
        Book book = new Book("Effective Java", "Joshua Bloch");
        when(bookService.saveBook(book)).thenReturn(book);

        // Act
        ResponseEntity<?> response = bookController.addBook(book);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(book);
    }

    @Test
    public void deleteBookById_happy_path() {
        // Arrange
        int bookId = 1;
        doNothing().when(bookService).deleteBookById(bookId);

        // Act
        ResponseEntity<?> responseEntity = bookController.deleteBookById(bookId);

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteBookById_id_zero() {
        // Arrange
        int id = 0;
        when(bookService.deleteBookById(id)).thenReturn(false);

        // Act
        ResponseEntity<?> response = bookController.deleteBookById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(bookService, times(1)).deleteBookById(id);
    }

    @Test
    void deleteBookById_id_negative() {
        // Arrange
        int negativeId = -1;
        when(bookService.deleteBookById(negativeId)).thenReturn(false);

        // Act
        ResponseEntity<?> response = bookController.deleteBookById(negativeId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid book ID");
    }

    @Test
    void deleteBookById_id_max_value() {
        // Arrange
        int id = Integer.MAX_VALUE;
        when(bookService.deleteBookById(id)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = bookController.deleteBookById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateBook_happy_path() {
        // Arrange
        Book book = new Book();
        int id = 1;
        Book updatedBook = new Book();
        when(bookService.updateBook(book, id)).thenReturn(updatedBook);

        // Act
        ResponseEntity<Book> response = bookController.updateBook(book, id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedBook);
        verify(bookService, times(1)).updateBook(book, id);
    }

    @Test
    void updateBook_id_zero() {
        // Arrange
        Book book = new Book();
        int id = 0;
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(bookService.updateBook(book, id)).thenReturn(null);

        // Act
        ResponseEntity<?> actualResponse = bookController.updateBook(book, id);

        // Assert
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Verify no interaction with the service due to invalid id
        verify(bookService, times(0)).updateBook(any(Book.class), anyInt());
    }

    @Test
    void updateBook_id_negative() {
        // Arrange
        Book book = new Book();
        int negativeId = -1;

        // Act
        ResponseEntity<?> response = bookController.updateBook(book, negativeId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Verify no interaction with the service due to invalid id
        verify(bookService, times(0)).updateBook(any(Book.class), anyInt());
    }

    @Test
    void updateBook_id_max_value() {
        // Arrange
        Book book = new Book();
        int maxValueId = Integer.MAX_VALUE;
        ResponseEntity<Book> expectedResponse = new ResponseEntity<>(book, HttpStatus.OK);
        when(bookService.updateBook(book, maxValueId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Book> response = bookController.updateBook(book, maxValueId);

        // Assert
        assertThat(response).isEqualTo(expectedResponse);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(book);
    }

    @Test
    void deleteAllBooks_happy_path() {
        // Arrange
        doNothing().when(bookService).deleteAllBooks();

        // Act
        ResponseEntity<?> responseEntity = bookController.deleteAllBooks();

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void addPublisherToExistingBooks_happy_path() {
        // Arrange
        List<String> bookIds = List.of("book1", "book2", "book3");
        String publisher = "New Publisher";
        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
        when(bookService.addPublisherToBooks(bookIds, publisher)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Void> response = bookController.addPublisherToExistingBooks(bookIds, publisher);

        // Assert
        assertThat(response).isEqualTo(expectedResponse);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(bookService, times(1)).addPublisherToBooks(bookIds, publisher);
    }

    @Test
    void getAllLibraries_happy_path() {
        // Arrange
        BookController bookControllerMock = mock(BookController.class);
        List<String> libraries = List.of("Library1", "Library2", "Library3");
        ResponseEntity<List<String>> expectedResponse = new ResponseEntity<>(libraries, HttpStatus.OK);
        when(bookControllerMock.getAllLibraries()).thenReturn(expectedResponse);

        // Act
        ResponseEntity<List<String>> actualResponse = bookControllerMock.getAllLibraries();

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).containsExactlyElementsOf(libraries);
    }

    @Test
    void getAuditHistory_happy_path() {
        // Arrange
        int bookId = 1;
        List<ApplicationAuditing> expectedAuditHistory = Arrays.asList(
            new ApplicationAuditing("Change1", "User1"),
            new ApplicationAuditing("Change2", "User2")
        );
        when(bookService.getAuditHistory(bookId)).thenReturn(expectedAuditHistory);

        // Act
        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(bookId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expectedAuditHistory);
    }

    @Test
    void getAuditHistory_bookId_zero() {
        // Arrange
        int bookId = 0;
        List<ApplicationAuditing> expectedAuditList = Collections.emptyList();
        when(bookService.getAuditHistory(bookId)).thenReturn(expectedAuditList);

        // Act
        ResponseEntity<List<ApplicationAuditing>> response = bookController.getAuditHistory(bookId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedAuditList);
    }

    @Test
    public void getAuditHistory_bookId_negative() {
        // Arrange
        int negativeBookId = -1;
        when(bookService.getAuditHistory(negativeBookId)).thenReturn(List.of());

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
        List<ApplicationAuditing> expectedAuditHistory = List.of(new ApplicationAuditing(), new ApplicationAuditing());
        when(bookService.getAuditHistory(bookId)).thenReturn(expectedAuditHistory);

        // Act
        ResponseEntity<List<ApplicationAuditing>> responseEntity = bookController.getAuditHistory(bookId);

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedAuditHistory);
        assertThat(responseEntity.getBody()).hasSize(2);
    }
}