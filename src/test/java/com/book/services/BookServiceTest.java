package com.book.services;

import com.book.auditing.ApplicationAuditing;
import com.book.dao.AuditRepo;
import com.book.dao.BookRepo;
import com.book.dao.LibraryRepository;
import com.book.entities.Book;
import com.book.entities.Library;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @Mock
    private LibraryRepository libraryRepo;

    @Mock
    private AuditRepo bookAuditRepository;

    @InjectMocks
    private BookService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_happy_path() {
        // Arrange
        List<Book> expectedBooks = Arrays.asList(new Book(), new Book());
        when(bookRepo.findAll()).thenReturn(expectedBooks);

        // Act
        ResponseEntity<List<Book>> responseEntity = underTest.getAllBooks();

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull().hasSize(2);
        assertThat(responseEntity.getBody()).containsExactlyElementsOf(expectedBooks);
    }

    @Test
    void getAllBooks_conditional_logic() {
        // Arrange
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepo.findAll()).thenReturn(books);

        // Act
        ResponseEntity<List<Book>> actualResponse = underTest.getAllBooks();

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).containsExactlyElementsOf(books);
    }

    @Test
    void getBookById_happy_path() {
        // Arrange
        int bookId = 1;
        Book book = new Book();
        when(bookRepo.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        ResponseEntity<?> response = underTest.getBookById(bookId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBookById_id_zero() {
        // Arrange
        int id = 0;
        when(bookRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = underTest.getBookById(id);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBookById_id_negative() {
        // Arrange
        int negativeId = -1;
        when(bookRepo.findById(negativeId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = underTest.getBookById(negativeId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Book doesn't exist by given id");
    }

    @Test
    void getBookById_id_max_value() {
        // Arrange
        int id = Integer.MAX_VALUE;
        when(bookRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = underTest.getBookById(id);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void addBook_happy_path() {
        // Arrange
        Book book = new Book();
        when(bookRepo.save(book)).thenReturn(book);

        // Act
        ResponseEntity<?> actualResponse = underTest.addBook(book);

        // Assert
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bookRepo, times(1)).save(book);
    }

    @Test
    void deleteBookById_happy_path() {
        // Arrange
        int bookId = 1;
        Book book = new Book();
        when(bookRepo.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepo).deleteById(bookId);

        // Act
        ResponseEntity<?> responseEntity = underTest.deleteBookById(bookId);

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteBookById_id_zero() {
        // Arrange
        int id = 0;
        when(bookRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = underTest.deleteBookById(id);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateBook_happy_path() {
        // Arrange
        int bookId = 1;
        Book book = new Book();
        when(bookRepo.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        // Act
        ResponseEntity<?> response = underTest.updateBook(book, bookId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateBook_id_zero() {
        // Arrange
        Book book = new Book();
        int id = 0;
        when(bookRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = underTest.updateBook(book, id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteAllBooks_happy_path() {
        // Arrange
        doNothing().when(bookRepo).deleteAll();

        // Act
        ResponseEntity<?> response = underTest.deleteAllBooks();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void addPublisherToExistingBooks_happy_path() {
        // Arrange
        when(bookRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<?> actualResponse = underTest.addPublisherToExistingBooks();

        // Assert
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bookRepo, times(1)).saveAll(any());
    }

    @Test
    void getAllLibraries_happy_path() {
        // Arrange
        List<Library> mockLibraries = List.of(new Library("Library A"), new Library("Library B"));
        when(libraryRepo.findAll()).thenReturn(mockLibraries);

        // Act
        ResponseEntity<List<Library>> responseEntity = underTest.getAllLibraries();

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().hasSize(2).containsExactlyElementsOf(mockLibraries);
    }

    @Test
    void createAuditEntry_happy_path() {
        // Arrange
        Book book = new Book();
        String modifiedBy = "John Doe";
        String actionType = "UPDATE";
        doNothing().when(bookAuditRepository).save(any(ApplicationAuditing.class));

        // Act
        underTest.createAuditEntry(book, modifiedBy, actionType);

        // Assert
        verify(bookAuditRepository, times(1)).save(any(ApplicationAuditing.class));
    }

    @Test
    void getAuditHistory_happy_path() {
        // Arrange
        int bookId = 1;
        List<ApplicationAuditing> expectedAuditHistory = List.of(
                new ApplicationAuditing(), new ApplicationAuditing()
        );
        when(bookAuditRepository.findByBookIdOrderByCreateDate(bookId)).thenReturn(expectedAuditHistory);

        // Act
        List<ApplicationAuditing> actualAuditHistory = underTest.getAuditHistory(bookId);

        // Assert
        assertThat(actualAuditHistory).isNotNull().hasSize(2).containsExactlyElementsOf(expectedAuditHistory);
    }
}