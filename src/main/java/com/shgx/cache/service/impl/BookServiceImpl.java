package com.shgx.cache.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.shgx.cache.enums.ReviewTypeEnum;
import com.shgx.cache.model.Book;
import com.shgx.cache.model.BookVO;
import com.shgx.cache.repository.BookRepo;
import com.shgx.cache.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.shgx.cache.constant.BookConstant.*;

/**
 * 书籍服务实现
 *
 * @author guangxush
 */

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    @Autowired
    BookRepo bookRepo;
    @Value("${guava.cache.maximumSize}")
    private Long cacheSize;
    @Value("${guava.cache.expire}")
    private Long expireDays;

    private LoadingCache<Long, Book> booksCache = CacheBuilder.newBuilder()
            .recordStats()
            .maximumSize(1000)
            .expireAfterAccess(10, TimeUnit.DAYS)
            .build(
                    new CacheLoader<Long, Book>() {
                        @Override
                        public Book load(Long id) throws Exception {
                            List<Book> bookVOList = findAllBookById(Collections.singletonList(id));
                            if (bookVOList != null && bookVOList.size() != 0) {
                                return bookVOList.get(0);
                            }
                            return new Book(BOOK_NAME, BOOK_AUTHOR, PUBLISH_HOUSE);
                        }
                    }
            );

    private LoadingCache<List<Long>, List<Book>> bookVOsCache = CacheBuilder.newBuilder()
            .recordStats()
            .maximumSize(1000)
            .expireAfterAccess(10, TimeUnit.DAYS)
            .build(
                    new CacheLoader<List<Long>, List<Book>>() {
                        @Override
                        public List<Book> load(List<Long> ids) throws Exception {
                            List<Book> bookVOList = findAllBookById(ids);
                            if (bookVOList != null && bookVOList.size() != 0) {
                                return bookVOList;
                            }
                            //设置一个默认值
                            bookVOList.add(new Book(BOOK_NAME, BOOK_AUTHOR, PUBLISH_HOUSE));
                            return bookVOList;
                        }
                    }
            );

    /**
     * 获取书籍信息
     *
     * @param id
     * @return
     */
    @Override
    public Book fetchBookById(Long id) {
        if (id == null) {
            log.error("the id is null");
            throw new NullPointerException("the id is null");
        }
        //从缓存中获取
        try {
            Book book = booksCache.get(id);
            if (book != null) {
                return book;
            }
        } catch (ExecutionException e) {
            log.error("take book from guava cache error, id : {}", id, e);
        }
        //从数据库中查询
        List<Book> vos = findAllBookById(Lists.newArrayList(id));
        if (vos.isEmpty()) {
            //返回默认值
            Book book = new Book();
            book.setId(id);
            book.setName(BOOK_NAME);
            book.setAuthor(BOOK_AUTHOR);
            book.setPublishHouse(PUBLISH_HOUSE);
            return book;
        }
        return vos.get(0);
    }

    /**
     * 根据ID查询某一本书籍
     *
     * @param id
     * @return
     */
    @Override
    public Book findBookInfoById(Long id) {
        Book book = null;
        Optional<Book> bookOptional = bookRepo.findById(id);
        if (bookOptional.isPresent()) {
            book = bookOptional.get();
        }
        return book;
    }

    /**
     * 根据书籍id列表查询一系列书籍
     *
     * @param ids
     * @return
     */
    @Override
    public List<Book> findAllBookById(List<Long> ids) {
        List<Book> books = new ArrayList<>();
        Optional<List<Book>> booksOption = bookRepo.findAllByIdInAndDeletedEqualsAndReviewStatusEquals(ids, false, ReviewTypeEnum.PASS);
        if (booksOption.isPresent()) {
            books = booksOption.get();
        }
        return books;
    }

    /**
     * 查询所有审核通过的书籍
     *
     * @param ids
     * @return
     */
    @Override
    public List<BookVO> findBooksByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty() || ids.size() == 0) {
            log.error("the ids is null");
            throw new NullPointerException("the ids is null");
        }
        List<BookVO> bookVOS = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        //从缓存中获取
        try {
            List<Book> booksCache = bookVOsCache.get(ids);
            if (booksCache != null) {
                books = booksCache;
            }
        } catch (ExecutionException e) {
            log.error("take books from guava cache error, ids : {}", ids, e);
        }
        //从数据库中查询
        if (books.isEmpty() && books.size() < 0) {
            books = findAllBookById(ids);
        }
        if (!books.isEmpty() && books.size() > 0) {
            for (Book book : books) {
                bookVOS.add(
                        BookVO.builder()
                                .name(book.getName())
                                .author(book.getAuthor())
                                .publishDate(book.getPublishDate())
                                .publishHouse(book.getPublishHouse())
                                .build()
                );
            }
        }
        return bookVOS;
    }

    /**
     * 将缓存状态定时记录在日志中
     *
     * @param
     * @return
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    private void recordCacheStatus() {
        CacheStats stats = booksCache.stats();
        log.info("guava cache status : {}", stats.toString());
    }

}
