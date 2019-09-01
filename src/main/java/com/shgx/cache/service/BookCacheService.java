package com.shgx.cache.service;

import com.shgx.cache.model.Book;

public interface BookCacheService {

    /**
     * 根据ID查询某一本书籍
     * @param id
     * @return
     */
    Book getBookByID(Long id);
}
