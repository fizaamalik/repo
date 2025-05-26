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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
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

    @Mock private BookRepo bookRepository;
    @Mock private LibraryRepository libraryRepo;
    @Mock private AuditRepo auditRepository;
    @InjectMocks private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_happy_path() {
        List<Book> mockBooks = List.of(new Book(), new Book());
        ResponseEntity<List<Book>> expectedResponse = ResponseEntity.ok(mockBooks);

        when(bookRepository.findAll()).thenReturn(mockBooks);

        ResponseEntity<List<Book>> actualResponse = bookService.getAllBooks();

        assertThat(actualResponse).isEqualTo(expectedResponse);
        assertThat(actualResponse.getBody()).hasSize(2);
        assertThat(actualResponse.getBody()).containsExactlyElementsOf(mockBooks);
    }

    @Test
    void getAllBooks_conditional_logic() {
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookService.getAllBooks();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty().hasSize(2).containsExactlyElementsOf(books);
    }

    @Test
    void getBookById_happy_path() {
        int bookId = 1;
        Book mockBook = new Book();
        mockBook.setId(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        ResponseEntity<Book> responseEntity = bookService.getBookById(bookId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(bookId);
    }

    @Test
    void getBookById_id_zero() {
        int id = 0;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Book> responseEntity = bookService.getBookById(id);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void getBookById_id_negative() {
        int negativeId = -1;
        when(bookRepository.findById(negativeId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.getBookById(negativeId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Book does't exist by given id ");
    }

    @Test
    void getBookById_id_max_value() {
        int maxValueId = Integer.MAX_VALUE;
        when(bookRepository.findById(maxValueId)).thenReturn(Optional.empty());

        ResponseEntity<String> actualResponse = bookService.getBookById(maxValueId);

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actualResponse.getBody()).isEqualTo("Book does't exist by given id ");
    }

    @Test
    void getBookById_conditional_logic() {
        int existingBookId = 1;
        Book existingBook = new Book();
        existingBook.setId(existingBookId);
        when(bookRepository.findById(existingBookId)).thenReturn(Optional.of(existingBook));

        int nonExistingBookId = 2;
        when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());

        ResponseEntity<Book> responseForExistingBook = bookService.getBookById(existingBookId);
        ResponseEntity<Book> responseForNonExistingBook = bookService.getBookById(nonExistingBookId);

        assertThat(responseForExistingBook.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseForExistingBook.getBody()).isEqualTo(existingBook);

        assertThat(responseForNonExistingBook.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseForNonExistingBook.getBody()).isNull();
    }

    @Test
    void addBook_happy_path() {
        Book book = new Book();
        book.setLibrary(new Library());
        book.setPublisher(new Publisher());

        when(bookRepository.save(book)).thenReturn(book);
        ResponseEntity<Book> expectedResponse = ResponseEntity.ok(book);

        ResponseEntity<Book> actualResponse = bookService.addBook(book);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void addBook_conditional_logic() {
        Book book = new Book();
        book.setLibrary(new Library());
        book.setPublisher(new Publisher());

        when(bookRepository.save(book)).thenReturn(book);

        ResponseEntity<Book> responseEntity = bookService.addBook(book);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void deleteBookById_happy_path() {
        int bookId = 1;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        ResponseEntity<?> actualResponse = bookService.deleteBookById(bookId);

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void deleteBookById_id_zero() {
        int id = 0;
        when(bookRepository.existsById(id)).thenReturn(false);

        ResponseEntity<?> responseEntity = bookService.deleteBookById(id);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(bookRepository, never()).deleteById(id);
    }

    @Test
    void deleteBookById_id_negative() {
        int negativeId = -1;
        when(bookRepository.existsById(negativeId)).thenReturn(false);

        ResponseEntity<?> responseEntity = bookService.deleteBookById(negativeId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBookById_id_max_value() {
        int maxId = Integer.MAX_VALUE;
        when(bookRepository.existsById(maxId)).thenReturn(false);

        ResponseEntity<?> response = bookService.deleteBookById(maxId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBookById_conditional_logic() {
        int bookId = 1;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(bookId);

        ResponseEntity<?> response = bookService.deleteBookById(bookId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void updateBook_happy_path() {
        Book book = new Book();
        int id = 1;
        Book existingBook = new Book();
        existingBook.setId(id);

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        ResponseEntity<Book> response = bookService.updateBook(book, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(existingBook);
    }

    @Test
    void updateBook_id_zero() {
        Book book = new Book();
        int id = 0;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.updateBook(book, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void updateBook_id_negative() {
        Book book = new Book();
        int negativeId = -1;

        ResponseEntity<?> response = bookService.updateBook(book, negativeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateBook_id_max_value() {
        Book book = new Book();
        int id = Integer.MAX_VALUE;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        ResponseEntity<Book> response = bookService.updateBook(book, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteAllBooks_happy_path() {
        doNothing().when(bookRepository).deleteAll();

        ResponseEntity<Void> response = bookService.deleteAllBooks();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(bookRepository, times(1)).deleteAll();
    }

    @Test
    void addPublisherToExistingBooks_happy_path() {
        Book book = mock(Book.class);
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(book.getPublisher()).thenReturn(null);

        ResponseEntity<?> responseEntity = bookService.addPublisherToExistingBooks();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Publisher 'XYZ' added to existing books.");
    }

    @Test
    void getAllLibraries_happy_path() {
        Library library1 = new Library();
        Library library2 = new Library();
        List<Library> mockLibraries = List.of(library1, library2);

        when(libraryRepo.findAll()).thenReturn(mockLibraries);

        ResponseEntity<List<Library>> response = bookService.getAllLibraries();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(library1, library2);
    }

    @Test
    void getAllLibraries_conditional_logic() {
        List<Library> libraries = List.of(new Library(), new Library());
        when(libraryRepo.findAll()).thenReturn(libraries);

        ResponseEntity<List<Library>> response = bookService.getAllLibraries();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsExactlyElementsOf(libraries);
    }

    @Test
    void createAuditEntry_happy_path() {
        Book book = new Book();
        String modifiedBy = "John Doe";
        String actionType = "UPDATE";
        doNothing().when(auditRepository).save(any(ApplicationAuditing.class));

        bookService.createAuditEntry(book, modifiedBy, actionType);

        verify(auditRepository).save(any(ApplicationAuditing.class));
    }

    @Test
    void createAuditEntry_modifiedBy_empty() {
        Book book = new Book();
        String modifiedBy = "";
        String actionType = "UPDATE";

        assertThatThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("modifiedBy cannot be empty");
    }

    @Test
    void createAuditEntry_modifiedBy_null() {
        Book book = new Book();
        String modifiedBy = null;
        String actionType = "UPDATE";

        assertThatThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("modifiedBy must not be null");
    }

    @Test
    void createAuditEntry_modifiedBy_whitespace() {
        Book book = new Book();
        String modifiedBy = "   ";
        String actionType = "UPDATE";

        assertThatThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("modifiedBy cannot be whitespace");
    }

    @Test
    void createAuditEntry_actionType_empty() {
        Book book = new Book();
        String modifiedBy = "user123";
        String actionType = "";

        assertThatThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Action type cannot be empty");
    }

    @Test
    void createAuditEntry_actionType_null() {
        Book book = new Book();
        String modifiedBy = "John Doe";
        String actionType = null;

        assertThatThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("actionType cannot be null");
    }

    @Test
    void createAuditEntry_actionType_whitespace() {
        Book book = new Book();
        String modifiedBy = "user123";
        String actionType = "   ";

        assertThatThrownBy(() -> bookService.createAuditEntry(book, modifiedBy, actionType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Action type cannot be whitespace");
    }

    @Test
    void getAuditHistory_happy_path() {
        int bookId = 1;
        List<ApplicationAuditing> expectedAuditHistory = List.of(new ApplicationAuditing(), new ApplicationAuditing());

        when(auditRepository.findByBookIdOrderByCreateDate(bookId)).thenReturn(expectedAuditHistory);

        List<ApplicationAuditing> actualAuditHistory = bookService.getAuditHistory(bookId);

        assertThat(actualAuditHistory).isEqualTo(expectedAuditHistory);
    }

    @Test
    void getAuditHistory_bookId_zero() {
        int bookId = 0;
        when(auditRepository.findByBookIdOrderByCreateDate(bookId)).thenReturn(Collections.emptyList());

        List<ApplicationAuditing> result = bookService.getAuditHistory(bookId);

        assertThat(result).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_negative() {
        int negativeBookId = -1;
        when(auditRepository.findByBookIdOrderByCreateDate(negativeBookId)).thenReturn(Collections.emptyList());

        List<ApplicationAuditing> auditHistory = bookService.getAuditHistory(negativeBookId);

        assertThat(auditHistory).isEmpty();
    }

    @Test
    void getAuditHistory_bookId_max_value() {
        int bookId = Integer.MAX_VALUE;
        List<ApplicationAuditing> expectedAuditHistory = Collections.emptyList();

        when(auditRepository.findByBookIdOrderByCreateDate(bookId)).thenReturn(expectedAuditHistory);

        List<ApplicationAuditing> actualAuditHistory = bookService.getAuditHistory(bookId);

        assertThat(actualAuditHistory).isEqualTo(expectedAuditHistory);
    }
}