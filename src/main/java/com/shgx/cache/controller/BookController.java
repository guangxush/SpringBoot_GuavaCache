package com.shgx.cache.controller;

import com.shgx.cache.model.Book;
import com.shgx.cache.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 常量定义
 *
 * @author guangxush
 */
@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Book findBookInfoById(@PathVariable("id")Long id){
        Book book = bookService.fetchBookById(id);
        if(book!=null){
            return book;
        }else{
            return null;
        }
    }

}
