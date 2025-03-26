package org.acme.spring.web;

import io.quarkus.logging.Log;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("year/{lower}/{higher}")
    public List<Book> findByPublicationYear(@PathVariable Integer lower, @PathVariable Integer higher) {
        return bookRepository.findByPublicationYearBetween(lower, higher);
    }

    @PostMapping
    @Transactional
    public void addBook(Book book) {
        Log.infof("Adding book: %s ", book.name);
        book.persist();
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new MissingBookException();
        }
    }
}
