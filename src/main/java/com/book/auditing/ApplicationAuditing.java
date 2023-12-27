package com.book.auditing;

import com.book.entities.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ApplicationAuditing{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int bookId;

    private String title;

    private LocalDateTime createDate;

    private LocalDateTime lastModified;

//    @ManyToOne
//    private Book book;
//
    private String modifiedBy;

    private String actionType;

    // other fields to capture changes, e.g., modifiedBy, actionType, etc.

    // constructor to create an audit entry from a book
    public ApplicationAuditing(Book book, String modifiedBy, String actionType) {
        this.bookId = book.getId();
        this.title = book.getTitle();
        this.createDate = book.getCreateDate();
        this.lastModified = book.getLastModified();
        this.modifiedBy=modifiedBy;
        this.actionType=actionType;

    }
}
