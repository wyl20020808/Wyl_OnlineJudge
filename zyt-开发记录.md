#### 2023-8-25

- [x] 继续阅读Judge0的API文档，理解33个参数含义
- [x] 封装有关Judge0的核心API（submissions），实现简单的判题接口
- [ ] ~~优化登录注册功能~~

今天看了很久Judge0的文档。这个官方的文档写得很好，但是我并没有详细阅读英文文档的经验，于是并没有很快读完，而是从昨天开始读到了现在。

文档的很多内容我都不需要看，其中最主要的就是提交程序的接口。

封装了一下HttpUtil，封装了一下Judge0的判题接口。

ps:Http接口本来在上午就应该调试完，但是因为我在发送http请求的时候没有带上"http://"，导致程序一直报错，正好网上又查不到，耽误了很久。

#### 2023-8-26

- [ ] ~~对接judge0判题API~~

非常难受，因为服务器配置问题，判题API始终没有对接好。就写了几个工具类，完善了实体类，啥也没做出来。

#### 2023-8-27

- [ ] 设计题目数据表

- [ ] 使用简易的文件系统存放题目的输入输出

- [ ] 完善单判题接口，实现多判题接口

换了之前的服务器，判题非常快，可以继续写了。

#### 2023-8-27



今天遇到一个很奇怪的错：# [org.apache.ibatis.type.TypeException：别名“Criterion”已映射到值“com.xxx.domain.def.GameListExample$Criterion”](https://stackoverflow.com/questions/55902991/org-apache-ibatis-type-typeexception-the-alias-criterion-is-already-mapped-to)



原因是因为两个entity取名太类似了，容易映射错误。我把ProblemSample改名为Sample就没问题了。
