package com.shgx.cache.service.impl;

import com.google.common.cache.CacheLoader;
import com.shgx.cache.config.CacheConfig;
import com.shgx.cache.model.Book;
import com.shgx.cache.repository.BookRepo;
import com.shgx.cache.service.BookCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;


@Service
@Slf4j
public class BookCacheServiceImpl extends CacheConfig implements BookCacheService {

    @Autowired
    BookRepo bookRepo;

    public BookCacheServiceImpl(){
        super();
    }

    @PostConstruct
    public void init() {
    }

    @Override
    protected Object loadData(Object o) {
        Book book = findBookInfoById((Long) o);
        return book;
    }

    /**
     * 从数据库中查询书籍
     * @param id
     * @return
     */
    private Book findBookInfoById(Long id) {
        Book book = null;
        Optional<Book> bookOptional = bookRepo.findById(id);
        if (bookOptional.isPresent()) {
            book = bookOptional.get();
        }
        return book;
    }

    @Override
    public Book getBookByID(Long id){
        return (Book)getCache(id);
    }
}
