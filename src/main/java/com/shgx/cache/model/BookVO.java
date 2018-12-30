package com.shgx.cache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookVO {
    /**
     * 书籍id
     */
    private Long id;
    /**
     * 书籍名称
     */
    private String name;
    /**
     * 书籍作者
     */
    private String author;
    /**
     * 书籍数量
     */
    private int count;
    /**
     * 书籍出版日期
     */
    private Date publishDate;
    /**
     * 书籍出版社
     */
    private String publishHouse;
}
