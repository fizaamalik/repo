package com.book.entities;


import jakarta.persistence.Embeddable;
@Embeddable
public class Publisher {
    String publisher_name;

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }
}
