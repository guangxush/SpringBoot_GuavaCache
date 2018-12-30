package com.shgx.cache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * 书籍暴露给外部的字段
 *
 * @author guangxush
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookVO {
    /**
     * 书籍名称
     */
    private String name;
    /**
     * 书籍作者
     */
    private String author;
    /**
     * 书籍出版日期
     */
    private Date publishDate;
    /**
     * 书籍出版社
     */
    private String publishHouse;
}
