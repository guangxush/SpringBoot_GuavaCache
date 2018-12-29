package com.shgx.cache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 书籍类
 *
 * @author guangxush
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book {
    /**
     * 书籍id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 书籍名称
     */
    @Id
    @Column(name = "name")
    private String name;
    /**
     * 书籍作者
     */
    @Id
    @Column(name = "author")
    private String author;
    /**
     * 书籍数量
     */
    @Id
    @Column(name = "count")
    private int count;
    /**
     * 书籍出版日期
     */
    @Id
    @Column(name = "publish_date")
    private Date publishDate;
    /**
     * 书籍出版社
     */
    @Id
    @Column(name = "publish_house")
    private String publishHouse;
}
