package com.book.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class TimeTest {

    @Test
    void toUnixTimestamp_happy_path() {
        // Given
        String dateTimeStr = "2023-03-15 10:15:30";

        // When
        long actualTimestamp = Time.toUnixTimestamp(dateTimeStr);

        // Then
        long expectedTimestamp = 1678871730L; // Expected UNIX timestamp for the given dateTimeStr
        assertThat(actualTimestamp).isEqualTo(expectedTimestamp);
    }

    @Test
    void toUnixTimestamp_dateTimeStr_empty() {
        // Act & Assert
        assertThatThrownBy(() -> Time.toUnixTimestamp(""))
                .isInstanceOf(Exception.class) // Assuming Exception as IllegalArgumentException is not specifically thrown in the class
                .hasMessageContaining("Text 'T' could not be parsed at index 0");
    }

    @Test
    void toUnixTimestamp_dateTimeStr_null() {
        // Act & Assert
        assertThatThrownBy(() -> Time.toUnixTimestamp(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void toUnixTimestamp_dateTimeStr_whitespace() {
        // Act & Assert
        assertThatThrownBy(() -> Time.toUnixTimestamp("   "))
                .isInstanceOf(Exception.class) // Assuming Exception as IllegalArgumentException is not specifically thrown in the class
                .hasMessageContaining("Text '   ' could not be parsed at index 0");
    }

    @Test
    void main_happy_path() {
        // Arrange: Mock the system-related components if necessary
        try (MockedStatic<System> mockedSystem = mockStatic(System.class)) {
            mockedSystem.when(() -> System.out.println(Mockito.anyString())).thenAnswer(invocation -> {
                String message = invocation.getArgument(0);
                // Assert: Verify that the correct output is produced
                assertThat(message).isEqualTo("Unix Timestamp for 2025-05-27 15:30:00 = 1748350200");
                return null;
            });

            // Act
            Time.main(new String[]{});
        }
    }

    @Test
    void main_args_empty() {
        // Act & Assert
        assertThatCode(() -> Time.main(new String[]{""}))
                .doesNotThrowAnyException();
    }

    @Test
    void main_args_null() {
        // Act & Assert
        assertThatThrownBy(() -> Time.main(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void main_args_whitespace() {
        // Act & Assert
        assertThatCode(() -> Time.main(new String[]{"   "}))
                .doesNotThrowAnyException();
    }
}