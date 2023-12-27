package com.book.dao;

import com.book.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Integer> {
    public Book findById(int id);


}
