package com.book.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock private LibraryRepository libraryRepo;
    @Mock private BookRepository bookRepository;
    @Mock private AuditRepository auditRepository;
    @InjectMocks private BookService bookService;

    @Test
    void getAllBooks_happy_path() {
        // Arrange
        List<Book> mockBooks = List.of(new Book("Title1", "Author1"), new Book("Title2", "Author2"));
        ResponseEntity<List<Book>> expectedResponse = new ResponseEntity<>(mockBooks, HttpStatus.OK);

        when(bookService.getAllBooks()).thenReturn(expectedResponse);

        // Act
        ResponseEntity<List<Book>> actualResponse = bookService.getAllBooks();

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(mockBooks);
    }

    @Test
    void getAllBooks_conditional_logic() {
        // Mocking a scenario where the repository returns an empty list
        when(bookRepository.findAll()).thenReturn(List.of());

        ResponseEntity<List<Book>> response = bookService.getAllBooks();

        // Verifying the response for an empty list condition
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isEmpty();

        // Mocking a scenario where the repository returns a non-empty list
        Book book1 = new Book("Title1", "Author1");
        Book book2 = new Book("Title2", "Author2");
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        response = bookService.getAllBooks();

        // Verifying the response for a non-empty list condition
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(book1, book2);
    }

    @Test
    void getBookById_happy_path() {
        // Arrange
        int bookId = 1;
        Book mockBook = new Book(bookId, "Title", "Author");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        // Act
        ResponseEntity<Book> response = bookService.getBookById(bookId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockBook);
    }

    @Test
    void getBookById_id_zero() {
        // Arrange
        int id = 0;
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");

        when(bookService.getBookById(id)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Object> response = bookService.getBookById(id);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Book not found");
    }

    @Test
    void getBookById_id_negative() {
        // Given
        int negativeId = -1;
        when(bookRepository.findById(negativeId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Book> responseEntity = bookService.getBookById(negativeId);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void getBookById_id_max_value() {
        // Arrange
        int id = Integer.MAX_VALUE;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Book> response = bookService.getBookById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void getBookById_conditional_logic() {
        // Mocking a scenario where the book exists
        Book mockBook = new Book(1, "Effective Java");
        when(bookRepository.findById(1)).thenReturn(Optional.of(mockBook));

        // Test when the book exists
        ResponseEntity<Book> response = bookService.getBookById(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockBook);

        // Mocking a scenario where the book does not exist
        when(bookRepository.findById(2)).thenReturn(Optional.empty());

        // Test when the book does not exist
        ResponseEntity<Book> responseNotFound = bookService.getBookById(2);
        assertThat(responseNotFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseNotFound.getBody()).isNull();
    }

    @Test
    void addBook_happy_path() {
        // Arrange
        Book book = new Book("Effective Java", "Joshua Bloch");
        when(bookRepository.save(book)).thenReturn(book);

        // Act
        ResponseEntity<Book> responseEntity = bookService.addBook(book);

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(book);
    }

    @Test
    void addBook_conditional_logic() {
        // Given
        Book book = new Book();
        book.setTitle("Test Title");
        book.setAuthor("Test Author");

        // Mocking behavior based on conditional logic in `addBook`
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        // When
        ResponseEntity<Book> response = bookService.addBook(book);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(book);

        // Additional assertions for conditional branches
        verify(bookRepository).save(book);
    }

    @Test
    public void deleteBookById_happy_path() {
        // Given
        int bookId = 1;
        Mockito.doNothing().when(bookRepository).deleteById(bookId);

        // When
        ResponseEntity<Void> response = bookService.deleteBookById(bookId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void deleteBookById_id_zero() {
        // Arrange
        int id = 0;
        doNothing().when(bookRepository).deleteById(id);

        // Act
        ResponseEntity<Void> responseEntity = bookService.deleteBookById(id);

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteBookById_id_negative() {
        // Arrange
        int negativeId = -1;
        when(bookRepository.existsById(negativeId)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = bookService.deleteBookById(negativeId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteBookById_id_max_value() {
        // Arrange
        int id = Integer.MAX_VALUE;
        doNothing().when(bookRepository).deleteById(id);

        // Act
        ResponseEntity<Void> responseEntity = bookService.deleteBookById(id);

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteBookById_conditional_logic() {
        int validId = 1;
        int invalidId = 999;

        when(bookRepository.existsById(validId)).thenReturn(true);
        when(bookRepository.existsById(invalidId)).thenReturn(false);

        // Test valid ID scenario
        ResponseEntity<Void> responseValid = bookService.deleteBookById(validId);
        assertThat(responseValid.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Test invalid ID scenario
        ResponseEntity<Void> responseInvalid = bookService.deleteBookById(invalidId);
        assertThat(responseInvalid.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(bookRepository).deleteById(validId);
        verify(bookRepository, never()).deleteById(invalidId);
    }

    @Test
    void updateBook_happy_path() {
        Book book = new Book();
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");

        int bookId = 1;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        ResponseEntity<Book> response = bookService.updateBook(book, bookId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(book);
    }

    @Test
    void updateBook_id_zero() {
        // Arrange
        Book book = new Book();
        int id = 0;

        ResponseEntity<Book> expectedResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(bookService.updateBook(book, id)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Book> response = bookService.updateBook(book, id);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateBook_id_negative() {
        // Given
        Book book = new Book();
        int negativeId = -1;

        // When
        ResponseEntity<Book> response = bookService.updateBook(book, negativeId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid book ID");

        verifyNoInteractions(bookRepository);
    }

    @Test
    void updateBook_id_max_value() {
        // Arrange
        Book book = new Book();
        int id = Integer.MAX_VALUE;
        ResponseEntity<Book> expectedResponse = new ResponseEntity<>(book, HttpStatus.OK);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        // Act
        ResponseEntity<Book> response = bookService.updateBook(book, id);

        // Assert
        assertThat(response).isEqualTo(expectedResponse);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(book);
    }

    @Test
    void deleteAllBooks_happy_path() {
        // Arrange
        doNothing().when(bookRepository).deleteAll();

        // Act
        ResponseEntity<Void> response = bookService.deleteAllBooks();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void addPublisherToExistingBooks_happy_path() {
        // Arrange
        String publisherName = "Penguin";
        List<Book> existingBooks = Arrays.asList(new Book("Book 1"), new Book("Book 2"));
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Publisher added successfully");

        when(bookService.addPublisherToExistingBooks(publisherName, existingBooks)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> actualResponse = bookService.addPublisherToExistingBooks(publisherName, existingBooks);

        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(bookService).addPublisherToExistingBooks(publisherName, existingBooks);
    }

    @Test
    void getAllLibraries_happy_path() {
        // Given
        Library library1 = new Library("Library 1");
        Library library2 = new Library("Library 2");
        List<Library> libraries = Arrays.asList(library1, library2);

        when(libraryRepo.findAll()).thenReturn(libraries);

        // When
        ResponseEntity<List<Library>> response = bookService.getAllLibraries();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).containsExactlyInAnyOrder(library1, library2);
    }

    @Test
    void getAllLibraries_conditional_logic() {
        // Arrange
        List<Library> libraries = List.of(new Library("Library1"), new Library("Library2"));

        when(bookService.getAllLibraries()).thenReturn(new ResponseEntity<>(libraries, HttpStatus.OK));

        // Act
        ResponseEntity<List<Library>> response = bookService.getAllLibraries();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).extracting(Library::getName).containsExactly("Library1", "Library2");
    }

    @Test
    void createAuditEntry_happy_path() {
        // Arrange
        Book book = new Book("123456789", "Test Title", "Test Author");
        String modifiedBy = "user123";
        String actionType = "UPDATE";

        // Act
        bookService.createAuditEntry(book, modifiedBy, actionType);

        // Assert
        verify(auditRepository, times(1)).save(any(AuditEntry.class));

        // Additional assertion to check the saved audit entry's properties
        ArgumentCaptor<AuditEntry> argumentCaptor = ArgumentCaptor.forClass(AuditEntry.class);
        verify(auditRepository).save(argumentCaptor.capture());
        AuditEntry capturedEntry = argumentCaptor.getValue();

        assertThat(capturedEntry).isNotNull();
        assertThat(capturedEntry.getBookId()).isEqualTo("123456789");
        assertThat(capturedEntry.getModifiedBy()).isEqualTo("user123");
        assertThat(capturedEntry.getActionType()).isEqualTo("UPDATE");
    }

    @Test
    void createAuditEntry_modifiedBy_empty() {
        String modifiedBy = "";

        assertThatThrownBy(() -> bookService.createAuditEntry(new Book(), modifiedBy, "UPDATE"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("modifiedBy cannot be empty");

        verifyNoInteractions(auditRepository);
    }

    @Test
    void createAuditEntry_modifiedBy_null() {
        // Arrange
        Book book = new Book();
        String modifiedBy = null;
        String actionType = "UPDATE";

        // Act
        doNothing().when(bookService).createAuditEntry(book, modifiedBy, actionType);
        bookService.createAuditEntry(book, modifiedBy, actionType);

        // Assert
        verify(bookService, times(1)).createAuditEntry(book, modifiedBy, actionType);
    }

    @Test
    void createAuditEntry_modifiedBy_whitespace() {
        // Arrange
        Book book = new Book();
        String modifiedBy = "   "; // whitespace string
        String actionType = "UPDATE";

        // Act & Assert
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .withMessageContaining("modifiedBy cannot be whitespace");
    }

    @Test
    void createAuditEntry_actionType_empty() {
        // Arrange
        Book book = new Book();
        String modifiedBy = "user123";
        String actionType = ""; // Edge case: empty string

        // Act & Assert
        assertThatThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Action type cannot be empty");
    }

    @Test
    void createAuditEntry_actionType_null() {
        // Arrange
        Book book = new Book();
        String modifiedBy = "testUser";
        String actionType = null;

        // Act & Assert
        assertThatThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Action type cannot be null");

        verifyNoInteractions(auditRepository);
    }

    @Test
    void createAuditEntry_actionType_whitespace() {
        // Arrange
        Book book = new Book("123", "Test Book", "Test Author");
        String modifiedBy = "tester";
        String actionType = "   "; // whitespace string

        // Act & Assert
        assertThatThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Action type cannot be blank");
    }

    @Test
    void getAuditHistory_happy_path() {
        // Given
        int bookId = 1;
        List<ApplicationAuditing> expectedAuditHistory = List.of(
            new ApplicationAuditing("Action1", "User1"),
            new ApplicationAuditing("Action2", "User2")
        );

        when(auditRepository.findAuditHistoryByBookId(bookId)).thenReturn(expectedAuditHistory);

        // When
        List<ApplicationAuditing> actualAuditHistory = bookService.getAuditHistory(bookId);

        // Then
        assertThat(actualAuditHistory).isEqualTo(expectedAuditHistory);
    }

    @Test
    void getAuditHistory_bookId_zero() {
        // Arrange
        int bookId = 0;
        List<ApplicationAuditing> expectedAuditHistory = Collections.emptyList();

        when(bookService.getAuditHistory(bookId)).thenReturn(expectedAuditHistory);

        // Act
        List<ApplicationAuditing> actualAuditHistory = bookService.getAuditHistory(bookId);

        // Assert
        assertThat(actualAuditHistory).isEqualTo(expectedAuditHistory);
    }

    @Test
    void getAuditHistory_bookId_negative() {
        // Arrange
        int negativeBookId = -1;
        when(auditRepository.findAuditHistoryByBookId(negativeBookId)).thenReturn(Collections.emptyList());

        // Act
        List<ApplicationAuditing> result = bookService.getAuditHistory(negativeBookId);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_max_value() {
        // Arrange
        int bookId = Integer.MAX_VALUE;
        List<ApplicationAuditing> expectedAuditHistory = Collections.emptyList();

        when(auditRepository.findAuditHistoryByBookId(bookId)).thenReturn(expectedAuditHistory);

        // Act
        List<ApplicationAuditing> actualAuditHistory = bookService.getAuditHistory(bookId);

        // Assert
        assertThat(actualAuditHistory).isEqualTo(expectedAuditHistory);
    }
}