package org.acme.spring.web;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findByPublicationYearBetween(Integer lower, Integer higher);
}
