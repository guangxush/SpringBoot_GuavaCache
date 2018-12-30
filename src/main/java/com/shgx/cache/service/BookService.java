package com.shgx.cache.service;

import com.shgx.cache.model.Book;
import com.shgx.cache.model.BookVO;

import java.util.List;
/**
 * 书籍操作服务
 *
 * @author guangxush
 */
public interface BookService {
    /**
     * 根据ID查询某一本书籍
     * @param id
     * @return
     */
    Book findBookInfoById(Long id);

    /**
     * 根据书籍id列表查询一系列书籍
     * @param ids
     * @return
     */
    List<Book> findAllBookById(List<Long> ids);

    /**
     * 根据书籍id查询并获取所需要的字段
     * @param id
     * @return
     */
    Book fetchBookById(Long id);

    /**
     * 根据ID列表获取所有的书籍
     * @param ids
     * @return
     */
    List<BookVO> findBooksByIds(List<Long> ids);
}
