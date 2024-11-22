package com.bookstore.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookstore.dto.BookQueryDTO;
import com.bookstore.entity.Book;
import com.bookstore.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_PREFIX = "lock:book:";


    private static final String CACHE_PREFIX = "books:query:";

    @Override
    public void addBook(Book book) {
        bookMapper.insert(book);
    }

    @Override
    public Book getBookById(Long id) {
        String cacheKey = "book:" + id;
        Book book = (Book) redisTemplate.opsForValue().get(cacheKey);
        if (book == null) {
            book = bookMapper.selectById(id);
            if (book != null) {
                redisTemplate.opsForValue().set(cacheKey, book);
            }
        }
        return book;
    }

    @Override
    @Transactional
    public void updateStock(Long bookId, int quantity) {
        String lockKey = LOCK_PREFIX + bookId;

        try {
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCKED", 10, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(locked)) {
                throw new RuntimeException("Stock update is locked by another process.");
            }

            Book book = getBookById(bookId);
            if (book == null) {
                throw new RuntimeException("Book not found for ID: " + bookId);
            }
            if (book.getStock() < quantity) {
                throw new RuntimeException("Insufficient stock");
            }

            int rows = bookMapper.updateStockWithVersion(bookId, quantity, book.getVersion());
            if (rows == 0) {
                throw new RuntimeException("Stock update failed due to concurrent modification");
            }

            redisTemplate.opsForValue().set("book:" + bookId, bookMapper.selectById(bookId));
        } finally {
            redisTemplate.delete(lockKey);
        }
    }


    @Override
    @Transactional
    public void updateBook(Long bookId, Book updatedBook) {
        String lockKey = LOCK_PREFIX + bookId;

        try {
            // 获取分布式锁
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCKED", 10, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(locked)) {
                throw new RuntimeException("Update operation is locked by another process.");
            }

            // 从数据库获取书籍信息
            Book existingBook = bookMapper.selectById(bookId);
            if (existingBook == null) {
                throw new RuntimeException("Book not found with ID: " + bookId);
            }

            // 更新字段
            if (updatedBook.getTitle() != null) {
                existingBook.setTitle(updatedBook.getTitle());
            }
            if (updatedBook.getAuthor() != null) {
                existingBook.setAuthor(updatedBook.getAuthor());
            }
            if (updatedBook.getPrice() != null) {
                existingBook.setPrice(updatedBook.getPrice());
            }
            if (updatedBook.getStock() != null) {
                existingBook.setStock(updatedBook.getStock());
            }

            // 使用乐观锁更新
            int rows = bookMapper.updateById(existingBook);
            if (rows == 0) {
                throw new RuntimeException("Update failed due to concurrent modification.");
            }

            // 更新缓存
            redisTemplate.opsForValue().set("book:" + bookId, existingBook);

            // 删除分页缓存
            Set<String> keys = redisTemplate.keys("books:page:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } finally {
            // 释放分布式锁
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    public void deleteBook(Long id) {
        bookMapper.deleteById(id);
        redisTemplate.delete("book:" + id);
    }

    @Override
    public IPage<Book> getBooksByCondition(int page, int size, BookQueryDTO queryDTO) {
        // 生成缓存键
        String cacheKey = generateCacheKey(page, size, queryDTO);

        // 从缓存中获取数据
        @SuppressWarnings("unchecked")
        IPage<Book> cachedResult = (IPage<Book>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null) {
            return cachedResult; // 如果缓存命中，直接返回
        }

        // 如果缓存未命中，查询数据库
        QueryWrapper<Book> queryWrapper = buildQueryWrapper(queryDTO);
        Page<Book> pagination = new Page<>(page, size);
        IPage<Book> result = bookMapper.selectPage(pagination, queryWrapper);

        // 将查询结果存入缓存（设置过期时间）
        redisTemplate.opsForValue().set(cacheKey, result, Duration.ofMinutes(10));
        return result;
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<Book> buildQueryWrapper(BookQueryDTO queryDTO) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        if (queryDTO.getTitle() != null && !queryDTO.getTitle().isEmpty()) {
            queryWrapper.like("title", queryDTO.getTitle());
        }
        if (queryDTO.getAuthor() != null && !queryDTO.getAuthor().isEmpty()) {
            queryWrapper.eq("author", queryDTO.getAuthor());
        }
        if (queryDTO.getMinPrice() != null) {
            queryWrapper.ge("price", queryDTO.getMinPrice());
        }
        if (queryDTO.getMaxPrice() != null) {
            queryWrapper.le("price", queryDTO.getMaxPrice());
        }
        if (queryDTO.getMinStock() != null) {
            queryWrapper.ge("stock", queryDTO.getMinStock());
        }
        if (queryDTO.getMaxStock() != null) {
            queryWrapper.le("stock", queryDTO.getMaxStock());
        }
        return queryWrapper;
    }

    /**
     * 生成缓存键
     */
    private String generateCacheKey(int page, int size, BookQueryDTO queryDTO) {
        return CACHE_PREFIX +
                "page:" + page +
                ":size:" + size +
                ":title:" + Objects.toString(queryDTO.getTitle(), "") +
                ":author:" + Objects.toString(queryDTO.getAuthor(), "") +
                ":minPrice:" + Objects.toString(queryDTO.getMinPrice(), "") +
                ":maxPrice:" + Objects.toString(queryDTO.getMaxPrice(), "") +
                ":minStock:" + Objects.toString(queryDTO.getMinStock(), "") +
                ":maxStock:" + Objects.toString(queryDTO.getMaxStock(), "");
    }
}
