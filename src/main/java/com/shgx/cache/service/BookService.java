package com.shgx.cache.service;

import com.shgx.cache.model.Book;

public interface BookService {
    Book findBookInfoById(Long id);
}
