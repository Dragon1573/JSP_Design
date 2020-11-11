# 2019～2020年第1学期JSP课程设计

[![GitHub License](https://img.shields.io/github/license/Dragon1573/JSP_Design)](https://github.com/Dragon1573/JSP_Design)
[![https://img.shields.io/badge/CSDN-%40Legend__1949-red?style=social](https://img.shields.io/badge/CSDN-%40Legend__1949-red?style=social)](https://me.csdn.net/u011367208)

## 简介

&emsp;&emsp;本项目是一个 JavaWeb 网页应用程序，参考 GitHub 制作一个可用于局域网内的小型代码托管平台。

&emsp;&emsp;此平台能够实现代码仓库的上传与下载，并通过技术手段模拟目录树浏览效果。它还提供讨论区，提供评论的新建与删除。

## 目录

- [系统要求](#系统要求)
- [项目需求](#项目需求)
- [导入及配置说明](#导入及配置说明)

## 系统要求

- [Java SE Development Kit 14 及后续版本](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Apache Maven 3.x](https://maven.apache.org/download.cgi)
- [Apache Tomcat 9.0.x 及后续版本](https://tomcat.apache.org/download-90.cgi)

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

&emsp;&emsp;本项目已于2020年11月9日重构为`Apache Maven`项目，兼容`JetBrains IntelliJ IDEA`、`Eclipse IDE`以及其他支持`Maven`的集成开发环境。

&emsp;&emsp;以下步骤以`JetBrains IntelliJ IDEA`为例：

1. 将本项目整体克隆至本地（此处使用`${PROJECT_ROOT}`表示）
2. 在`IntelliJ IDEA`主界面上，打开并以`Maven`项目的形式导入本项目
3. 编辑运行配置，将『应用程序服务器』切换为您本地使用的`Apache Tomcat`服务器
3. 启动服务器，访问[主页 - 佛大云服务](http://localhost/JSP_Design)
