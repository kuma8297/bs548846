package com.bookstore.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bookstore.dto.BookQueryDTO;
import com.bookstore.entity.Book;

public interface BookService {
    void addBook(Book book);

    Book getBookById(Long id);

    void updateStock(Long bookId, int quantity);

    void updateBook(Long bookId, Book book);

    void deleteBook(Long id);

    IPage<Book> getBooksByCondition(int page, int size, BookQueryDTO queryDTO);
}
