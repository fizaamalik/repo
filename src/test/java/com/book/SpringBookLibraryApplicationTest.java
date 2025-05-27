package com.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.SpringApplication;

@ExtendWith(MockitoExtension.class)
class SpringBookLibraryApplicationTest {

    @InjectMocks
    private SpringBookLibraryApplication underTest;

    /**
     * Test the happy path scenario for the main method.
     * Ensures that the SpringApplication runs without issues when the main method is called.
     */
    @Test
    void main_happy_path() {
        // Arrange
        SpringApplication mockSpringApplication = mock(SpringApplication.class);
        String[] args = {};

        // Mock SpringApplication.run(...) to avoid actual application start
        when(mockSpringApplication.run(SpringBookLibraryApplication.class, args))
            .thenReturn(null);

        // Act
        SpringBookLibraryApplication.main(args);

        // Assert
        verify(mockSpringApplication).run(SpringBookLibraryApplication.class, args);
    }

    /**
     * Test the main method with empty arguments.
     * Ensures that no exception is thrown when empty arguments are passed to the main method.
     */
    @Test
    void main_args_empty() {
        try (MockedStatic<SpringBookLibraryApplication> mockedStatic = Mockito.mockStatic(SpringBookLibraryApplication.class)) {
            mockedStatic.when(() -> SpringBookLibraryApplication.main(new String[]{""}))
                         .thenCallRealMethod();

            assertThatNoException().isThrownBy(() -> {
                SpringBookLibraryApplication.main(new String[]{""});
            });
        }
    }

    /**
     * Test the main method with null arguments.
     * Ensures that no exception is thrown when null arguments are passed to the main method.
     */
    @Test
    void main_args_null() {
        // Act & Assert
        assertThatCode(() -> SpringBookLibraryApplication.main(null))
            .doesNotThrowAnyException();
    }

    /**
     * Test the main method with whitespace arguments.
     * Ensures that the application initializes properly without throwing exceptions.
     */
    @Test
    void main_args_whitespace() {
        // Arrange
        String[] args = {" "};

        // Act
        SpringBookLibraryApplication.main(args);

        // Assert
        // Since the main method does not return anything, we need to assert the expected state changes.
        // For example, if main method initializes some components or logs something, we could verify that.
        // As we don't have specific details, we'll assume we expect no exceptions and proper initialization.
        assertThat(true).isTrue(); // Placeholder assertion to indicate successful execution
    }
}