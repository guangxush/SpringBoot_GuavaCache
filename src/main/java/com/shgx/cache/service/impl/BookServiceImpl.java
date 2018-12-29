package com.shgx.cache.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.shgx.cache.model.Book;
import com.shgx.cache.repository.BookRepo;
import com.shgx.cache.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookRepo bookRepo;
    @Value("${guava.cache.maximumSize}")
    private Long cacheSize;
    @Value("${guava.cache.expire}")
    private Long expireDays;

    private LoadingCache<Long, Book> userPassportsCache = CacheBuilder.newBuilder()
            .recordStats()
            .maximumSize(1000)
            .expireAfterAccess(10, TimeUnit.DAYS)
            .build(
                    new CacheLoader<Long, Book>() {
                        @Override
                        public Book load(Long id) throws Exception {
                            List<Book> userPassportVOList = requestUsersByUid(Collections.singletonList(id));
                            if (userPassportVOList != null) {
                                return userPassportVOList.get(0);
                            }
                            return new UserPassportVO(id, DEFAULT_NICKNAME, DEFAULT_ICON);
                        }
                    }
            );
    }
    @Override
    public UserPassportVO fetchUserByUid(Long uid) {
        if(uid == null) {
            if (uid == null) {
                log.error("the uid is null");
                throw new LemonInternalError("the uid is null");
            }
            List<UserPassportVO> vos = requestUsersByUid(Lists.newArrayList(uid));
            if(vos.isEmpty()){
                UserPassportVO vo = new UserPassportVO();
                vo.setUid( uid);
                vo.setNickname(DEFAULT_NICKNAME);
                vo.setIcon(DEFAULT_ICON);
                return vo;
                UserPassportVO vo = null;
                try {
                    vo = userPassportsCache.get(uid);
                } catch (ExecutionException e) {
                    log.error("take userPassport from guava cacahe error, uid : {}", uid, e);
                }
                return vos.get(0);
                return vo;
            }




            @Override
    public Book findBookInfoById(Long id){
        Book book = null;
        Optional<Book> bookOptional = bookRepo.findById(id);
        if(bookOptional.isPresent()){
            book = bookOptional.get();
        }
        return book;
    }
}
