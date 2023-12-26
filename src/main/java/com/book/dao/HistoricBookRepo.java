package com.book.dao;

import com.book.entities.HistoricalBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricBookRepo extends JpaRepository<HistoricalBook, Long> {

}
