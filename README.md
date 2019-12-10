# 2019～2020年第1学期JSP课程设计

## 简介

&emsp;&emsp;本项目是一个运行在 Apache Tomcat 9.0 上的本地站点，参考 GitHub 制作一个可用于局域网内的小型代码托管平台。

## 目录

- [简介](#简介)
- [目录](#目录)
- [项目配置](#项目配置)
- [项目需求](#项目需求)
- [导入及配置说明](#导入及配置说明)

## 项目配置

- 开发环境
  - 程序：[JetBrains IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/)
- 运行环境
  - [Apache Tomcat v9.0.21](https://tomcat.apache.org/download-90.cgi)
  - Dynamic Web Module 4.0
  - [Oracle Java SE Development Kit 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
  - JavaScript 1.0
  - jQuery 2.1.1
  - Alibaba FastJSON 1.2.60
  - Apache Jakarta Java Server Tag Library 1.1.2
  - Microsoft JDBC 7.2.1 for SQL Server JRE11
  - Apache Commons FileUpload 1.4
  - Apache Commons IO 2.6
- 数据库
  - 服务器：[SQL Server 2017 Express](https://www.microsoft.com/zh-cn/sql-server/sql-server-downloads)
  - 管理工具：[SQL Server Management Studio 17.9.1](https://docs.microsoft.com/zh-cn/sql/ssms/download-sql-server-management-studio-ssms?view=sql-server-2017)
- 浏览器
  - Google Chrome

## 项目需求

1. 代码仓库

   - 每一位用户都有专属于自己的“库房区域”（个人文件夹），用户在个人库房区中可以创建任意多个代码仓库（项目文件夹）。
   - 仓库以目录形式展现。
   - 能够将文件上传至指定目录，当路径不存在时，可递归创建目录。
   - 可以删除指定目录下的文件，当指定对象为目录时，可以递归删除目录。

2. 评论管理

   - 未登录的访客用户不具备任何权限，没有评论功能。
   - 仓库所有者与已登录的访客用户对所有仓库拥有评论功能，任何登录用户可以发表评论。

3. 用户管理

   - 尚未登陆的用户都是访客身份。平台为他们提供注册、忘记密码等功能。
   - 每一位正常登陆平台的用户都是其下属仓库的所有者。平台为他们提供修改登陆密码、启用/修改密保（包括但不限于手机号码、电子邮箱地址、密码保护问题）、修改登录名的功能。
   
## 导入及配置说明

1. 在 JetBrains IntelliJ IDEA 中打开项目
2. 
