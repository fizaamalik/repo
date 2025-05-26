package com.book.entities;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryTest {
    @InjectMocks
    private Library underTest;

    @Test
    void getId_happy_path() {
        // Assuming Library has a constructor or a setter to set the id
        int expectedId = 123;
        underTest.setId(expectedId); // or use a constructor that sets the id

        int actualId = underTest.getId();

        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void setId_happy_path() {
        // Arrange
        Library library = spy(new Library());
        int expectedId = 123;

        // Act
        library.setId(expectedId);

        // Assert
        assertThat(library.getId()).isEqualTo(expectedId);
    }

    @Test
    void setId_id_zero() {
        // Arrange
        Library library = mock(Library.class);
        int id = 0;

        // Act
        doCallRealMethod().when(library).setId(id);
        library.setId(id);

        // Assert
        verify(library).setId(id);
        assertThat(library.getId()).isEqualTo(id);
    }

    @Test
    void setId_id_negative() {
        // Arrange
        Library library = mock(Library.class);
        int negativeId = -1;

        // Act & Assert
        assertThatThrownBy(() -> library.setId(negativeId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("ID must be positive");
    }

    @Test
    void setId_id_max_value() {
        // Arrange
        Library library = new Library();
        int maxId = Integer.MAX_VALUE;

        // Act
        library.setId(maxId);

        // Assert
        assertThat(library.getId()).isEqualTo(maxId);
    }

    @Test
    void getLibraryName_happy_path() {
        // Arrange
        Library library = mock(Library.class);
        String expectedLibraryName = "Central Library";
        when(library.getLibraryName()).thenReturn(expectedLibraryName);

        // Act
        String actualLibraryName = library.getLibraryName();

        // Assert
        assertThat(actualLibraryName).isEqualTo(expectedLibraryName);
    }

    @Test
    void setLibraryName_happy_path() {
        // Given
        String expectedLibraryName = "Central Library";

        // When
        underTest.setLibraryName(expectedLibraryName);

        // Then
        assertThat(underTest.getLibraryName()).isEqualTo(expectedLibraryName);
    }

    @Test
    void setLibraryName_libraryName_empty() {
        // Arrange
        Library library = new Library();

        // Act & Assert
        assertThatThrownBy(() -> library.setLibraryName(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Library name cannot be empty");
    }

    @Test
    void setLibraryName_libraryName_null() {
        // Arrange
        Library library = new Library();

        // Act & Assert
        assertThatThrownBy(() -> library.setLibraryName(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Library name cannot be null");
    }

    @Test
    void setLibraryName_libraryName_whitespace() {
        // Arrange
        Library library = mock(Library.class);
        String whitespaceLibraryName = "   ";

        // Act
        library.setLibraryName(whitespaceLibraryName);

        // Assert
        verify(library).setLibraryName(whitespaceLibraryName);
        assertThat(library.getLibraryName()).isEqualTo(whitespaceLibraryName);
    }
}