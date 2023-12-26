package com.book.dao;

import com.book.entities.Book;
import com.book.entities.HistoricalBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Integer> {
    public Book findById(int id);


}
