package com.book.dao;

import com.book.auditing.ApplicationAuditing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepo extends JpaRepository<ApplicationAuditing, Integer> {
    List<ApplicationAuditing> findByBookIdOrderByCreateDate(int bookId);
}
