package com.shgx.cache.service;

import com.shgx.cache.model.Book;

import java.util.List;

public interface BookService {
    Book findBookInfoById(Long id);

    List<Book> findAllBookById(List<Long> ids);

    Book fetchBookByUid(Long uid);
}
