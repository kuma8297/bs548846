package com.bookstore.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bookstore.common.Result;
import com.bookstore.dto.BookQueryDTO;
import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public Result<Void> addBook(@RequestBody Book book) {
        bookService.addBook(book);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<Book> getBook(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return Result.success(book);
    }

    @PutMapping("/{id}/stock")
    public Result<Void> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        bookService.updateStock(id, quantity);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return Result.success(null);
    }

    @PostMapping("/search")
    public Result<IPage<Book>> getBooksByCondition(
            @RequestParam int page,
            @RequestParam int size,
            @RequestBody BookQueryDTO queryDTO) {
        IPage<Book> books = bookService.getBooksByCondition(page, size, queryDTO);
        return Result.success(books);
    }

    @PutMapping("/{id}")
    public Result<Void> updateBook(@PathVariable Long id, @RequestBody Book book) {
        bookService.updateBook(id, book);
        return Result.success(null);
    }
}
