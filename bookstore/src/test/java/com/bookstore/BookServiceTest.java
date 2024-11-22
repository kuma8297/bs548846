package com.bookstore;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bookstore.dto.BookQueryDTO;
import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

@SpringBootTest
class BookServiceTest {

    @Resource
    private BookService bookService;

    @Test
    void testAddBook() {
        Book book = new Book();
        book.setTitle("小说");
        book.setAuthor("dht");
        book.setPrice(new BigDecimal("79.99"));
        book.setStock(50);

        bookService.addBook(book);

        System.out.println("Book added successfully: " + book.getTitle());
    }

    @Test
    void testUpdateStock_Concurrent() {
        Long bookId = 1L;
        int quantity = 1;

        // 模拟多线程并发更新库存
        Runnable task = () -> {
            try {
                bookService.updateStock(bookId, quantity);
                System.out.println("Stock updated successfully for book ID: " + bookId);
            } catch (Exception e) {
                System.err.println("Stock update failed: " + e.getMessage());
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();
    }


    @Test
    void testGetBooksByPage() {
        // 构造查询条件
        BookQueryDTO queryDTO = new BookQueryDTO();
        //queryDTO.setTitle("Spring");       // 模糊查询书名包含 "Spring"
        //queryDTO.setAuthor("Craig Walls"); // 精确匹配作者名

        // 设置分页参数
        int page = 1;
        int size = 5;

        // 调用分页查询服务
        IPage<Book> books = bookService.getBooksByCondition(page, size, queryDTO);

        // 打印分页结果
        System.out.println("Total Books: " + books.getTotal());          // 总记录数
        System.out.println("Current Page: " + books.getCurrent());       // 当前页码
        System.out.println("Books Per Page: " + books.getSize());        // 每页记录数

        // 打印当前页的书籍
        System.out.println("Books on Current Page:");
        books.getRecords().forEach(book -> {
            System.out.println("ID: " + book.getId() +
                    ", Title: " + book.getTitle() +
                    ", Author: " + book.getAuthor() +
                    ", Price: " + book.getPrice() +
                    ", Stock: " + book.getStock());
        });
    }


    @Test
    void testConcurrentUpdateBook() {
        Long bookId = 1L;

        Runnable task = () -> {
            Book updatedBook = new Book();
            updatedBook.setTitle("Updated Title");
            updatedBook.setPrice(new BigDecimal("99.99"));

            try {
                bookService.updateBook(bookId, updatedBook);
                System.out.println("Update successful for thread: " + Thread.currentThread().getName());
            } catch (Exception e) {
                System.err.println("Update failed for thread: " + Thread.currentThread().getName() + " - " + e.getMessage());
            }
        };

        // 模拟多线程并发更新
        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
