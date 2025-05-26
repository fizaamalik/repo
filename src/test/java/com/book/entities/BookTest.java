package com.book.entities;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookTest {

    @InjectMocks
    private Book underTest;

    @Mock
    private Author authorMock;

    @Test
    void testGetAuthorName() {
        when(authorMock.getName()).thenReturn("John Doe");
        underTest.setAuthor(authorMock);

        String authorName = underTest.getAuthorName();
        assertThat(authorName).isEqualTo("John Doe");
    }
}