# 2019～2020年第1学期JSP课程设计

## 简介

&emsp;&emsp;本项目是一个运行在Apache Tomcat 9.0上的本地站点，参考GitHub（~~也就是你现在正在使用的平台~~）制作一个可用于局域网内的小型代码托管平台。

## 目录

- [简介](#简介)
- [目录](#目录)
- [项目配置](#项目配置)
- [项目需求](#项目需求)

## 项目配置

- 开发环境
  - 程序：[JetBrains IntelliJ IDEA](https://www.jetbrains.com/idea/)
  - 版本：2019.1.3
  - 生成号：#IU-191.7479.19
- 运行环境
  - [Apache Tomcat v9.0](https://tomcat.apache.org/download-90.cgi)
  - Dynamic Web Module 4.0
  - [Oracle Java SE Development Kit 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
  - JavaScript 1.0
  - jQuery 3.4.1
  - Alibaba FastJSON 1.2.58
- 数据库
  - 服务器：[SQL Server 2017 Express](https://www.microsoft.com/zh-cn/sql-server/sql-server-downloads)
  - 管理工具：[SQL Server Management Studio 17.9.1](https://docs.microsoft.com/zh-cn/sql/ssms/download-sql-server-management-studio-ssms?view=sql-server-2017)
  - Java驱动程序：Microsoft JDBC for SQL Server 7.2 with JRE11
- 浏览器
  - Google Chrome
  - 75.0.3770.100（正式版本 x64）

## 项目需求

1. 代码仓库

   - 每一位用户都有专属于自己的“库房区域”（个人文件夹），用户在个人库房区中可以创建任意多个代码仓库（项目文件夹）。
   - 仓库管理页面提供克隆/拉取（Clone/Pull）、推送（Push）等[Git](https://git-scm.com/)基本功能。
   - 仓库详情页需要能够展示仓库的文件目录树，当点击纯文本文件时可以进行预览。
   - 仓库所有者可以在线删改远程仓库。

2. 评论管理

   - 未登录的访客用户不具备任何权限，没有评论功能。
   - 仓库所有者与已登录的访客用户对所有仓库拥有评论功能，任何登录用户可以发表/编辑/删除自己的评论，而仓库所有者有权编辑/删除他人对自己仓库的评论，管理员能够控制一切评论。

3. 用户管理

   - 尚未登陆的用户都是访客身份。平台为他们提供注册、忘记密码等功能。
   - 每一位正常登陆平台的用户都是其下属仓库的所有者。平台为他们提供修改登陆密码、启用/修改/关闭密保（包括但不限于手机号码、电子邮箱地址、密码保护问题）、修改/注销登录名的功能。
   - 平台拥有一名具备超级用户身份的管理员。管理员具备以上处除注销自己的登录名、关闭密保（对于管理员来说，这些是必须的）外的所有功能。

4. 备份与恢复

   - 管理员能够对全平台数据进行备份与恢复。
