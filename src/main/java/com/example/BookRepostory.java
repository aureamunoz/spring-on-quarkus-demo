package com.example;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepostory extends CrudRepository<Book, Integer> {

    List<Book> findByPublicationYearBetween(Integer lower,Integer higher);
}
