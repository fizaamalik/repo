package com.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class SpringBookLibraryApplicationTest {

    @InjectMocks
    private SpringBookLibraryApplication underTest;

    /**
     * Test for main method with valid arguments.
     * It verifies that SpringApplication.run is called without throwing exceptions.
     */
    @Test
    void main_happy_path() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            // Arrange
            String[] args = {"arg1", "arg2"};

            // Act & Assert
            assertThatCode(() -> SpringBookLibraryApplication.main(args))
                .doesNotThrowAnyException();

            // Verify that SpringApplication.run was called
            mockedSpringApplication.verify(() -> SpringApplication.run(SpringBookLibraryApplication.class, args));
        }
    }

    /**
     * Test for main method with empty arguments.
     * It ensures that SpringApplication.run is never called.
     */
    @Test
    void main_args_empty() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            // Arrange
            String[] args = {""};

            // Act
            SpringBookLibraryApplication.main(args);

            // Assert
            mockedSpringApplication.verify(() -> SpringApplication.run(SpringBookLibraryApplication.class, args), never());
        }
    }

    /**
     * Test for main method with null arguments.
     * It verifies that no exception is thrown when arguments are null.
     */
    @Test
    void main_args_null() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringBookLibraryApplication.main(null)).thenCallRealMethod();
            
            // Assert
            assertThatNoException().isThrownBy(() -> SpringBookLibraryApplication.main(null));
        }
    }

    /**
     * Test for main method with whitespace arguments.
     * It ensures that SpringApplication.run is called and no exception is thrown.
     */
    @Test
    void main_args_whitespace() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            // Arrange
            String[] args = {" "};

            // Act
            SpringBookLibraryApplication.main(args);

            // Assert
            mockedSpringApplication.verify(() -> SpringApplication.run(SpringBookLibraryApplication.class, args));
        }
    }
}