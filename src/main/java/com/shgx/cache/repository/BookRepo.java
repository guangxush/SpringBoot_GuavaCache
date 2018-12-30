package com.shgx.cache.repository;

import com.shgx.cache.enums.ReviewTypeEnum;
import com.shgx.cache.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * 数据库查询类
 *
 * @author guangxush
 */
@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    /**
     * 根据文件id获取未删除而且审核通过的相册文件信息
     * @param id
     * @param delete
     * @param status
     * @return
     */
    Optional<List<Book>> findAllByIdInAndDeletedEqualsAndReviewStatusEquals(List<Long> id, Boolean delete, Enum<ReviewTypeEnum> status);
}
