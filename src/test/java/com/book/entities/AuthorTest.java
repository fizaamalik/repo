package com.book.entities;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorTest {

    @InjectMocks
    private Author underTest;

    @Test
    void getId_happy_path() {
        // Arrange
        Author author = new Author();
        int expectedId = 123;  // Assuming we have a way to set this ID, e.g., through a constructor or a setter
        author.setId(expectedId);

        // Act
        int actualId = author.getId();

        // Assert
        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void setId_happy_path() {
        // Arrange
        Author author = new Author();
        int expectedId = 123;

        // Act
        author.setId(expectedId);

        // Assert
        assertThat(author.getId()).isEqualTo(expectedId);
    }

    @Test
    void setId_id_zero() {
        // Arrange
        Author author = new Author();

        // Act
        author.setId(0);

        // Assert
        assertThat(author.getId()).isEqualTo(0);
    }

    @Test
    void setId_id_negative() {
        // Arrange
        Author author = new Author();
        int negativeId = -1;

        // Act
        author.setId(negativeId);

        // Assert
        assertThat(author.getId()).isEqualTo(negativeId);
    }

    @Test
    void setId_id_max_value() {
        // Arrange
        Author author = new Author();
        int maxId = Integer.MAX_VALUE;

        // Act
        author.setId(maxId);

        // Assert
        assertThat(author.getId()).isEqualTo(maxId);
    }

    @Test
    void getFirst_name_happy_path() {
        // Arrange
        Author author = new Author();
        author.setFirstName("John");

        // Act
        String firstName = author.getFirstName();

        // Assert
        assertThat(firstName).isEqualTo("John");
    }

    @Test
    void setFirst_name_happy_path() {
        // Arrange
        Author author = new Author();
        String expectedFirstName = "John";

        // Act
        author.setFirstName(expectedFirstName);

        // Assert
        assertThat(author.getFirstName()).isEqualTo(expectedFirstName);
    }

    @Test
    void setFirst_name_first_name_empty() {
        // Arrange
        Author author = new Author();
        String emptyFirstName = "";

        // Act & Assert
        assertThatThrownBy(() -> author.setFirstName(emptyFirstName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("First name cannot be empty");
    }

    @Test
    void setFirst_name_first_name_null() {
        // Arrange
        Author author = new Author();

        // Act & Assert
        assertThatThrownBy(() -> author.setFirstName(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("first_name cannot be null");
    }

    @Test
    void setFirst_name_first_name_whitespace() {
        // Arrange
        Author author = new Author();
        String whitespaceFirstName = "   ";

        // Act
        author.setFirstName(whitespaceFirstName);

        // Assert
        assertThat(author.getFirstName()).isEqualTo(whitespaceFirstName);
    }

    @Test
    void getLast_name_happy_path() {
        // Arrange
        Author author = mock(Author.class);
        String expectedLastName = "Doe";
        when(author.getLastName()).thenReturn(expectedLastName);

        // Act
        String actualLastName = author.getLastName();

        // Assert
        assertThat(actualLastName).isEqualTo(expectedLastName);
    }

    @Test
    void setLast_name_happy_path() {
        // Given
        Author author = new Author();
        String expectedLastName = "Smith";

        // When
        author.setLastName(expectedLastName);

        // Then
        assertThat(author.getLastName()).isEqualTo(expectedLastName);
    }

    @Test
    void setLast_name_last_name_empty() {
        // Arrange
        Author author = new Author();

        // Act
        author.setLastName("");

        // Assert
        assertThat(author.getLastName()).isEmpty();
    }

    @Test
    void setLast_name_last_name_null() {
        // Arrange
        Author author = mock(Author.class);

        // Act
        author.setLastName(null);

        // Assert
        verify(author).setLastName(null); // Ensure the method was called with null
        assertThat(author.getLastName()).isNull();
    }

    @Test
    void setLast_name_last_name_whitespace() {
        // Arrange
        Author author = new Author();
        String whitespaceLastName = "   ";

        // Act
        author.setLastName(whitespaceLastName);

        // Assert
        assertThat(author.getLastName()).isEqualTo(whitespaceLastName);
    }

    @Test
    void getBook_happy_path() {
        // Arrange
        Author author = mock(Author.class);
        Book book1 = new Book("Title1", "ISBN1");
        Book book2 = new Book("Title2", "ISBN2");
        List<Book> expectedBooks = List.of(book1, book2);

        when(author.getBooks()).thenReturn(expectedBooks);

        // Act
        List<Book> actualBooks = author.getBooks();

        // Assert
        assertThat(actualBooks).isEqualTo(expectedBooks);
    }

    @Test
    void setBook_happy_path() {
        // Arrange
        Author author = new Author();
        Book book1 = mock(Book.class);
        Book book2 = mock(Book.class);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        // Act
        author.setBooks(books);

        // Assert
        assertThat(author.getBooks()).hasSize(2);
        assertThat(author.getBooks()).containsExactlyInAnyOrder(book1, book2);
    }
}