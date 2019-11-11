package com.example;


import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookRepostory bookRepostory;

    public BookController(BookRepostory bookRepostory) {
        this.bookRepostory = bookRepostory;
    }

    @GetMapping
    public Iterable<Book> findAll(){
        return bookRepostory.findAll();
    }

    @GetMapping("year/{lower}/{higher}")
    public List<Book> findByPublicationYear(@PathVariable Integer lower, @PathVariable Integer higher){
        return bookRepostory.findByPublicationYearBetween(lower,higher);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Integer id){
        try {
            bookRepostory.deleteById(id);
        } catch (Exception e) {
            throw new MissingBookException();
        }
    }
}
