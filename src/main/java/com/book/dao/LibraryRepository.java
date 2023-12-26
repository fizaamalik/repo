package com.book.dao;

import com.book.entities.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Integer> {

    //public Library findDistinctByLibraryName(String libraryName);
}
