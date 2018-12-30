package com.shgx.cache.model;

import com.shgx.cache.enums.ReviewTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 书籍类
 *
 * @author guangxush
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "book")
public class Book {
    /**
     * 书籍id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 书籍名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 书籍作者
     */
    @Column(name = "author")
    private String author;
    /**
     * 书籍数量
     */
    @Column(name = "count")
    private int count;
    /**
     * 书籍出版日期
     */
    @Column(name = "publish_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;
    /**
     * 书籍出版社
     */
    @Column(name = "publish_house")
    private String publishHouse;
    /**
     * 是否被删除
     */
    @Column(name = "deleted_status", nullable = false)
    @Type(type = "boolean")
    private Boolean deleted;
    /**
     * 审核结果
     */
    @Column(name = "review_status")
    @Convert(converter = ReviewTypeEnum.Converter.class)
    private ReviewTypeEnum reviewStatus;

    public Book(String name, String author, String publishHouse) {
        this.name = name;
        this.author = author;
        this.publishHouse = publishHouse;
    }

    public Book(){

    }
}
