package com.book.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class Library {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String libraryName;

    public Library(){
        super();
    }
    public Library(int id, String name) {
        this.id = id;
        this.libraryName=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
}
