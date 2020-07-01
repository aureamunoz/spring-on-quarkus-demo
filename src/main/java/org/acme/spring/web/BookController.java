package org.acme.spring.web;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Iterable<Book> findAll(){
        return bookRepository.findAll();
    }

    @GetMapping("year/{lower}/{higher}")
    public List<Book> findByPublicationYear(@PathVariable Integer lower, @PathVariable Integer higher){
        return bookRepository.findByPublicationYearBetween(lower,higher);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Integer id){
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new MissingBookException();
        }
    }
}
