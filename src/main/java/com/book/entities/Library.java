package com.book.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class Library {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String libraryName;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "library")
//    @JsonManagedReference
    private List<Book> books;

    public Library(){
        super();
    }
    public Library(int id, String name, List<Book> books) {
        this.id = id;
        this.books = books;
        this.libraryName=name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
}
