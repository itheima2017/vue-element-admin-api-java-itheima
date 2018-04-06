# 项目综述-summary

## 项目logo



## 软件环境

- JDK8
- Maven3+
- Git2+
- IDEA Lombok插件
- MySQL 5+

## 项目简介

Vue-Element-Admin是一个轻量级的Spring Boot2快速开发平台，设计目标是快速开发、学习简单、轻量级、易扩展；使用Spring Boot2、Spring Security、SpringData JPA、JWT、Webflux等框架。功能包含用户管理、菜单管理、权限管理等，采用前后端分离技术，前端基于Vue开发，后端提供Rest接口。

## 技术栈

Spring Boot2+Spring Security+SpringData JPA+JWT+Webflux

## 功能特色

* [x] 基于Webflux包装的Rest API
* [x] 灵活的权限配置
* [x] 基于注解的API权限配置

## 模块说明

* config：全局配置及启动预加载数据处理
* controller：Rest API
* entity：JPA实体
* exception：全局异常处理及自定义异常类
* handler：登录及校验处理器
* repository：JPA数据库操作接口
* service：业务层
* util：工具

## 如何运行

### 克隆项目

```bash
git clone https://github.com/itheima2017/vue-element-admin-api-java-itheima.git
```

### server

* 步骤一：新建数据库

  创建名为vue_element_admin的数据库

* 步骤二：修改默认配置

  ```
  #修改默认配置
  vim ./vue-element-admin-api/src/main/resources/application.yml
  #修改数据库配置
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/vue_element_admin?useUnicode=true&characterEncoding=utf8
      username: root   #数据库账号
      password: 123456 #数据库密码    
  #修改端口
  server:
    port: 7999
  ```

* 步骤三：打包及运行

  ```
  #进入项目目录
  cd vue-element-admin-api
  #打包
  mvn clean package
  #运行
  nohup ./vue-element-admin-api/target/vue-element-admin-api-1.0.0.jar
  ```

* 步骤四：初始化数据库

  ```
  #进入数据库初始化文件目录
  cd ./vue-element-admin-api/db
  #运行初始化文件
  mysql -uroot -p123456 -Dvue_element_admin < init.sql
  #mysql -u账号 -p密码 -D数据库名称 < sql文件路径
  ```

* 步骤五：访问主页

  http://localhost/xxxxx.html

## 接口API

* [GitHub API](https://developer.github.com/v3/)

## 引文

- [Springboot 2.0 reference](https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/htmlsingle/)
- [使用 Spring 5 的 WebFlux 开发反应式 Web 应用](https://www.ibm.com/developerworks/cn/java/spring5-webflux-reactive/index.html)
- [10 Best Practices for Writing Node.js REST APIs](https://blog.risingstack.com/10-best-practices-for-writing-node-js-rest-apis/)
- [JWT](https://jwt.io/)

## 版权

江苏传智播客教育科技股份有限公司 &nbsp;版权所有Copyright 2006-2018, All Rights Reserved

## 其他

我们推荐使用 Markdown 编写你的 README，请最好注意排版问题以增加文档可读性，推荐阅读 Coding 的 《文案排版规范》。
