package com.shgx.cache.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 缓存配置文件
 *
 * @author guangxush
 */
@Configuration
public abstract class CacheConfig<K, V> {
    protected LoadingCache<K, V> cache;

    public CacheConfig() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) throws Exception {
                        return loadData(k);
                    }
                });
    }

    /**
     * 超时缓存：数据写入缓存超过一定时间自动刷新
     *
     * @param duration
     * @param timeUtil
     */
    public CacheConfig(long duration, TimeUnit timeUtil) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(duration, timeUtil)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) throws Exception {
                        return loadData(k);
                    }
                });
    }

    /**
     * 限容缓存：缓存数据个数不能超过maxSize
     *
     * @param maxSize
     */
    public CacheConfig(long maxSize) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) throws Exception {
                        return loadData(k);
                    }
                });
    }

    /**
     * 权重缓存：缓存数据权重和不能超过maxWeight
     *
     * @param maxWeight
     * @param weigher：权重函数类，需要实现计算元素权重的函数
     */
    public CacheConfig(long maxWeight, Weigher<K, V> weigher) {
        cache = CacheBuilder.newBuilder()
                .maximumWeight(maxWeight)
                .weigher(weigher)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) throws Exception {
                        return loadData(k);
                    }
                });
    }


    /**
     * 缓存数据加载方法
     *
     * @param k
     * @return
     */
    protected abstract V loadData(K k);

    /**
     * 从缓存获取数据
     *
     * @param param
     * @return
     */
    public V getCache(K param) {
        return cache.getUnchecked(param);
    }

    /**
     * 清除缓存数据，缓存清除后，数据会重新调用load方法获取
     *
     * @param k
     */
    public void refresh(K k) {
        cache.refresh(k);
    }

    /**
     * 主动设置缓存数据
     *
     * @param k
     * @param v
     */
    public void put(K k, V v) {
        cache.put(k, v);
    }
}
