package com.shgx.cache.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.shgx.cache.enums.ReviewTypeEnum;
import com.shgx.cache.model.Book;
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

    /**
     * 获取书籍信息
     *
     * @param uid
     * @return
     */
    @Override
    public Book fetchBookById(Long uid) {
        if (uid == null) {
            log.error("the uid is null");
            throw new NullPointerException("the uid is null");
        }
        //从缓存中获取
        try {
            Book book = booksCache.get(uid);
            if (book != null) {
                return book;
            }
        } catch (ExecutionException e) {
            log.error("take userPassport from guava cacahe error, uid : {}", uid, e);
        }
        //从数据库中查询
        List<Book> vos = findAllBookById(Lists.newArrayList(uid));
        if (vos.isEmpty()) {
            //返回默认值
            Book book = new Book();
            book.setId(uid);
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
