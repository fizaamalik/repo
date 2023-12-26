package com.book.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private Author author;

    //    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @Column(name="publisher_name")
    @Embedded
    private Publisher publisher;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "library_id")
//    @JsonBackReference
    private Library library;
    @Column(name = "library_name")
    private String libraryName;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JsonBackReference
    //@JsonIgnoreProperties("book")
    //@JsonManagedReference
    private List<HistoricalBook> historicalBooks;

    public Book() {

    }

    public Book(int id, String title, Author author, Publisher publisher, Library library, String libraryName, List<HistoricalBook> historicalBooks) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.library = library;
        this.libraryName = libraryName;
        this.historicalBooks = historicalBooks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public List<HistoricalBook> getHistoricalBooks() {
        return historicalBooks;
    }

    public void setHistoricalBooks(List<HistoricalBook> historicalBooks) {
        this.historicalBooks = historicalBooks;
        if (historicalBooks != null) {
            for (HistoricalBook historicalBook : historicalBooks) {
                historicalBook.setBook(this);
            }
        }
    }
}
