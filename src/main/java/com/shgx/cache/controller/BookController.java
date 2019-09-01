package com.shgx.cache.controller;

import com.shgx.cache.model.Book;
import com.shgx.cache.model.BookVO;
import com.shgx.cache.service.BookCacheService;
import com.shgx.cache.service.BookService;
import com.shgx.cache.service.impl.BookCacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Autowired
    private BookCacheService bookCacheService;

    /**
     * 根据ID查找书籍
     * @param id
     * @return
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Book findBookInfoById(@PathVariable("id")Long id){
        Book book = bookService.fetchBookById(id);
        if(book!=null){
            return book;
        }else{
            return null;
        }
    }

    /**
     * 获取所有的书籍
     * @param books
     */
    @RequestMapping(path = "/books", method = RequestMethod.POST)
    public List<BookVO> findAllBooks(@RequestBody List<Long> books){
        return bookService.findBooksByIds(books);
    }

    /**
     * 根据ID查找书籍
     * @param id
     * @return
     */
    @RequestMapping(path = "/config/{id}", method = RequestMethod.GET)
    public Book findBookInfoByIdMethod2(@PathVariable("id")Long id){
        Book book = bookCacheService.getBookByID(id);
        if(book!=null){
            return book;
        }else{
            return null;
        }
    }

}
