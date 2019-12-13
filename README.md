# 2019～2020年第1学期JSP课程设计

[![GitHub License](https://img.shields.io/github/license/Dragon1573/JSP_Design)](https://github.com/Dragon1573/JSP_Design)
[![https://img.shields.io/badge/CSDN-%40Legend__1949-red?style=social](https://img.shields.io/badge/CSDN-%40Legend__1949-red?style=social)](https://me.csdn.net/u011367208)

## 简介

&emsp;&emsp;本项目是一个 JavaWeb 网页应用程序，参考 GitHub 制作一个可用于局域网内的小型代码托管平台。

&emsp;&emsp;此平台能够实现代码仓库的上传与下载，并通过技术手段模拟目录树浏览效果。它还提供讨论区，提供评论的新建与删除。

## 目录

- [项目配置](#项目配置)
- [Java 依赖项](#java-依赖项)
- [项目需求](#项目需求)
- [导入及配置说明](#导入及配置说明)

## 项目配置

- 开发环境
  - 程序：[JetBrains IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/)
- 运行环境
  - [Apache Tomcat v9.0.21](https://tomcat.apache.org/download-90.cgi)
  - Dynamic Web Module 4.0
  - [Oracle Java SE Development Kit 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
  - jQuery 2.1.1
- 数据库
  - 服务器：[SQL Server 2017 Express](https://www.microsoft.com/zh-cn/sql-server/sql-server-downloads)
  - 管理工具：[SQL Server Management Studio 17.9.1](https://docs.microsoft.com/zh-cn/sql/ssms/download-sql-server-management-studio-ssms?view=sql-server-2017)
- 浏览器
  - Google Chrome

## Java 依赖项

```xml
<!--
  请注意，本项目不是 Maven 项目，以下内容仅用于在 Maven 中索引依赖项。
  如果您想下载 JAR 软件包，可通过 https://maven.aliyun.com/mvn/search 进行 GAV 搜索下载。
-->
<dependencies>
  <dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>7.2.1.jre11</version>
  </dependency>
  <dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.60</version>
  </dependency>
  <dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.6</version>
  </dependency>
  <dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
  </dependency>
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
  </dependency>
</dependencies>
```

## 项目需求

1. 代码仓库

   - 每个用户可以创建任意个代码仓库，每个仓库拥有独立的目录树。
   - 能够将文件上传至指定目录，当路径不存在时，可递归创建目录。
   - 可以删除指定目录下的文件，当指定对象为目录时，可以递归删除目录。
   - 未登录的用户可以自由访问任意仓库并进行文件下载，但无法进行上传与删除操作。

2. 评论管理

   - 未登录的访客用户可以自由浏览评论及其详情页。
   - 任何已登录用户可以发表评论，或删除由自己发送的评论。

3. 用户管理

   - 权限控制采用类似 Linux 的细粒度控制方法：
     - 每个用户对其发布的任何数据拥有超级权限；
     - 每个用户对其他数据拥有访客权限；
     - 未登录用户只拥有访客权限。
   - 允许未登录用户注册账户或忘记密码。
   - 允许已登录用户：
     - 修改登录密码
     - 启用/修改密保（包括但不限于手机号码、电子邮箱地址、密码保护问题）
     - 修改登录名
   
## 导入及配置说明

&emsp;&emsp;对于没有使用 `Apache Maven` 的 JavaWeb 模块， IDEA 不支持将其导出为 Eclipse 项目。强制导出会使模块设置丢失，为 Eclipse 项目配置带来极大的不便。如您确有使用本项目的需要，请自行尝试导入。

1. 在 IDEA 中打开项目
2. 将项目的 JDK 切换为您已安装的版本
3. 在 `Library` 选项卡中，下载并安装本项目所需的依赖项
4. 启动服务器，访问主页 http://localhost/JSP_Design/index.jsp
