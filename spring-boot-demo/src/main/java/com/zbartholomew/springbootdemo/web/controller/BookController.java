package com.zbartholomew.springbootdemo.web.controller;

import com.zbartholomew.springbootdemo.data.model.Book;
import com.zbartholomew.springbootdemo.data.repo.BookRepository;
import com.zbartholomew.springbootdemo.web.exception.BookIdMismatchException;
import com.zbartholomew.springbootdemo.web.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/title/{bookTitle}")
    public List<Book> findByTitle(@PathVariable String bookTitle) {
        return bookRepository.findByTitle(bookTitle);
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        final Book book = bookRepository.findOne(id);
        if (book == null) {
            throw new BookNotFoundException();
        }
        return book;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        final Book book = bookRepository.findOne(id);
        if (book == null) {
            throw new BookNotFoundException();
        }
        bookRepository.delete(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        final Book old = bookRepository.findOne(id);
        if (old == null) {
            throw new BookNotFoundException();
        }
        return bookRepository.save(book);
    }

}