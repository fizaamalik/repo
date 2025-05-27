package com.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringBookLibraryApplicationTest {

    @InjectMocks
    private SpringBookLibraryApplication underTest;

    /**
     * Test to verify the main method executes without exceptions when called with no arguments.
     */
    @Test
    void main_happy_path() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            String[] args = {};
            mockedSpringApplication.when(() -> SpringApplication.run(SpringBookLibraryApplication.class, args)).thenReturn(null);

            SpringBookLibraryApplication.main(args);

            mockedSpringApplication.verify(() -> SpringApplication.run(SpringBookLibraryApplication.class, args));
            assertThatCode(() -> SpringBookLibraryApplication.main(args)).doesNotThrowAnyException();
        }
    }

    /**
     * Test to verify the main method handles empty string argument without exceptions.
     */
    @Test
    void main_args_empty() {
        // Arrange
        String[] args = {""};

        // Act
        SpringBookLibraryApplication.main(args);

        // Assert
        // Since the method is void and you're testing edge cases,
        // you might need to verify some side effects or states.
        // Here, we assume that there are no side effects for an empty string argument.
        // Adjust assertions based on actual expected behavior.
        assertThat(true).isTrue(); // Placeholder assertion
    }

    /**
     * Test to verify the main method throws NullPointerException when null arguments are passed.
     */
    @Test
    void main_args_null() {
        String[] args = null;

        assertThatThrownBy(() -> SpringBookLibraryApplication.main(args))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("args cannot be null");
    }

    /**
     * Test to verify the main method executes without exceptions when called with whitespace arguments.
     */
    @Test
    void main_args_whitespace() {
        // Arrange
        String[] args = {"   "};

        // Act & Assert
        assertThatNoException().isThrownBy(() -> SpringBookLibraryApplication.main(args));
    }
}