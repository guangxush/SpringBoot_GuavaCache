### SpringBoot_GuavaCache

- 使用Guava做缓存，将数据库查询结果保存在缓存里面，下次查询可以直接从内存中取值

- 直接测试查询一本书的数据：http://localhost:8080/booksys/book/3
  
  第一次查询现实数据库查询语句，第二次查询直接从内存中读取

- 查询全部的书籍数据（未删除并且审核通过）：http://localhost:8080/booksys/book/books
 

  
 ### 基本原理
 - 如果某个请求需要频繁的去数据库中请求数据，当用户量较大且服务器性能较低的时候会导致服务效率低下，QPS较低
 ![image.png](https://upload-images.jianshu.io/upload_images/7632302-d562dc2a5e7edf92.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 - 这时我们可以把这些固定的请求，先用一个线程去数据库里面查询，然后把他放入缓存中，这样后序的线程再请求的时候就会直接从缓存中取数据，从而提高了查询性能也缓解了数据库的压力
 ![image.png](https://upload-images.jianshu.io/upload_images/7632302-78e2813339afb08d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
 ### 什么时候使用缓存
 如果你的需求满足以下条件可以使用Cahce缓存数据
 - 愿意消耗一些内存空间来提升速度
 - 预料到某些键会被查询一次以上
 - 缓存中存放的数据总量不会超过内存容量
 
 ### 项目场景
 
 我们这里设计了一张图书信息表，用于查询，因为一般网站的主页上都会显示最新上架的一些图书，可以把这些固定的查询图书的信息放到缓存里，这里使用SpringBoot+Guava的技术方案；
 ![image.png](https://upload-images.jianshu.io/upload_images/7632302-41e0e82839033d21.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
 ### 基本配置
 pom.xml引入依赖“
 
 ```
 <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
         <version>18.0</version>
  </dependency>
 ```
 application.yml中配置缓存大小和缓存时间
 ```
 # guava
 guava:
   cache:
     maximumSize: 10000
     expire: 15
 ```
 ### 查询语句
 缓存设置
 ```
 private LoadingCache<Long, Book> booksCache = CacheBuilder.newBuilder()
             .recordStats()
             .maximumSize(1000)
             .expireAfterAccess(10, TimeUnit.DAYS)
             .build(
                     new CacheLoader<Long, Book>() {
                         @Override
                         public Book load(Long id) throws Exception {
                             List<Book> bookVOList = findAllBookById(Collections.singletonList(id));
                             if (bookVOList != null && bookVOList.size() != 0) {
                                 return bookVOList.get(0);
                             }
                             return new Book(BOOK_NAME, BOOK_AUTHOR, PUBLISH_HOUSE);
                         }
                     }
             );
 ```
 数据库查询请求
 ```
 public List<Book> findAllBookById(List<Long> ids) {
         List<Book> books = new ArrayList<>();
         Optional<List<Book>> booksOption = bookRepo.findAllById(ids);
         if (booksOption.isPresent()) {
             books = booksOption.get();
         }
         return books;
     }
 ```
 最终查询请求
 ```
 public Book fetchBookById(Long id) {
         if (id == null) {
             log.error("the id is null");
             throw new NullPointerException("the id is null");
         }
         //从缓存中获取
         try {
             Book book = booksCache.get(id);
             if (book != null) {
                 return book;
             }
         } catch (ExecutionException e) {
             log.error("take book from guava cache error, id : {}", id, e);
         }
         //从数据库中查询
         List<Book> vos = findAllBookById(Lists.newArrayList(id));
         if (vos.isEmpty()) {
             //返回默认值
             Book book = new Book();
             book.setId(id);
             book.setName(BOOK_NAME);
             book.setAuthor(BOOK_AUTHOR);
             book.setPublishHouse(PUBLISH_HOUSE);
             return book;
         }
         return vos.get(0);
     }
 ```
 外部请求Controller
 ```
 @RequestMapping(path = "/{id}", method = RequestMethod.GET)
     public Book findBookInfoById(@PathVariable("id")Long id){
         Book book = bookService.fetchBookById(id);
         if(book!=null){
             return book;
         }else{
             return null;
         }
     }
 ```
 ### 请求结果查看
 直接测试查询一本书的数据：http://localhost:8080/booksys/book/3
 第一次查询现实数据库查询语句
 ![image.png](https://upload-images.jianshu.io/upload_images/7632302-2925cc84db562847.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
 ![](https://upload-images.jianshu.io/upload_images/7632302-1692b42942861cc6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
 第二次查询直接从内存中读取,SQL还是原来的一条
 ![image.png](https://upload-images.jianshu.io/upload_images/7632302-9dc10239d6f35b27.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
 因此有了缓存，我们可以减少很多计算压力，提高应用程序的QPS。
 
 ### 项目源码
 [项目源码](https://github.com/guangxush/SpringBoot_GuavaCache)
 

 
