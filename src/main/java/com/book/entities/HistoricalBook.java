package com.book.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class HistoricalBook {

    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    //@JsonBackReference
    private Book book;
    @Column(name = "title")
    private String title;

    @Column(name = "author_id")
    private int authorId;

    @Column(name = "publisher_name")
    private String publisherName;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public HistoricalBook(){

    }

    public HistoricalBook(Long id, Book book, String title, int authorId, String publisherName, LocalDateTime createdAt) {
        this.id = id;
        this.book = book;
        this.title = title;
        this.authorId = authorId;
        this.publisherName = publisherName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
