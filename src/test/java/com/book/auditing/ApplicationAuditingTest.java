package com.book.auditing;

import com.book.entities.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class ApplicationAuditingTest {

    @InjectMocks
    private ApplicationAuditing underTest;

    @Mock
    private Book book;

    /**
     * Setup method to initialize common test data.
     */
    @BeforeEach
    void setUp() {
        underTest = new ApplicationAuditing();
    }

    /**
     * Test to ensure ApplicationAuditing is correctly constructed from a Book object.
     */
    @Test
    void shouldCreateAuditEntryFromBook() {
        // Given
        int bookId = 1;
        String title = "Test Book";
        LocalDateTime createDate = LocalDateTime.now().minusDays(1);
        LocalDateTime lastModified = LocalDateTime.now();
        String modifiedBy = "tester";
        String actionType = "CREATE";
        
        when(book.getId()).thenReturn(bookId);
        when(book.getTitle()).thenReturn(title);
        when(book.getCreateDate()).thenReturn(createDate);
        when(book.getLastModified()).thenReturn(lastModified);

        // When
        ApplicationAuditing auditEntry = new ApplicationAuditing(book, modifiedBy, actionType);

        // Then
        assertThat(auditEntry.getBookId()).isEqualTo(bookId);
        assertThat(auditEntry.getTitle()).isEqualTo(title);
        assertThat(auditEntry.getCreateDate()).isEqualTo(createDate);
        assertThat(auditEntry.getLastModified()).isEqualTo(lastModified);
        assertThat(auditEntry.getModifiedBy()).isEqualTo(modifiedBy);
        assertThat(auditEntry.getActionType()).isEqualTo(actionType);
    }
}