package com.book.entities;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PublisherTest {

    @InjectMocks
    private Publisher underTest;

    @Test
    void getPublisher_name_happy_path() {
        // Arrange
        Publisher publisher = new Publisher();
        String expectedName = "Test Publisher";

        // Act
        String actualName = publisher.getPublisher_name();

        // Assert
        assertThat(actualName).isEqualTo(expectedName);
    }

    @Test
    void setPublisher_name_happy_path() {
        // Arrange
        Publisher publisher = new Publisher();
        String expectedName = "O'Reilly Media";

        // Act
        publisher.setPublisher_name(expectedName);

        // Assert
        assertThat(publisher.getPublisher_name()).isEqualTo(expectedName);
    }

    @Test
    void setPublisher_name_publisher_name_empty() {
        // Arrange
        Publisher publisher = new Publisher();
        String emptyPublisherName = "";

        // Act & Assert
        assertThatThrownBy(() -> publisher.setPublisher_name(emptyPublisherName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Publisher name cannot be empty");
    }

    @Test
    void setPublisher_name_publisher_name_null() {
        // Arrange
        Publisher publisher = Mockito.mock(Publisher.class);
        String publisher_name = null;

        // Act & Assert
        assertThatThrownBy(() -> publisher.setPublisher_name(publisher_name))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Publisher name cannot be null");
    }

    @Test
    void setPublisher_name_publisher_name_whitespace() {
        // Arrange
        Publisher publisher = new Publisher();
        String whitespacePublisherName = "   ";

        // Act
        publisher.setPublisher_name(whitespacePublisherName);

        // Assert
        assertThat(publisher.getPublisher_name()).isEqualTo(whitespacePublisherName);
    }
}