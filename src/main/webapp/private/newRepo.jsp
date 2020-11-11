<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session" />
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" type="text/css" href="../css/index.css" />
  <link rel="stylesheet" type="text/css" href="../css/newRepo.css" />
  <link rel="shortcut icon" href="../img/favicon.ico" type="image/canvasObject-icon" />
  <script type="text/javascript" src="../bootstrap/jquery.min.js"></script>
  <title>上传文件</title>
  <%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
  %>
</head>

<body>
  <div class="mainContainer">
    <header>
      <div id="logo">
        <a href="http://web.fosu.edu.cn/">
          <img alt="佛山科学技术学院" src="../img/fosu-logo.png" />
        </a>
      </div>
      <div id="blank">
        <table id="navigator">
          <tbody>
            <tr>
              <td>
                <a href="../index.jsp">首页</a>
              </td>
              <td>
                <a href="repositories.jsp">仓库</a>
              </td>
              <td>
                <a href="../comments.jsp">评论</a>
              </td>
              <td>
                <a href="https://github.com/Dragon1573/JSP_Design/">GitHub</a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div id="profile">
        <c:choose> <c:when test="${certificate.verified && !'Anonymous'.equals(certificate.username)}">
          <details>
            <summary style="text-align: right;">
              <jsp:getProperty name="certificate" property="username" />
            </summary>
            <div class="menu">
              <div>
                <label>当前身份：</label>
                <p style="font-weight: bolder; font-size: 14pt;">
                  <jsp:getProperty name="certificate" property="username" />
                </p>
              </div>
              <div class="line"></div>
              <div>
                <div>
                  <a href="repositories.jsp?user=${certificate.username}">
                    你的仓库
                  </a>
                </div>
                <div>
                  <a href="../comments.jsp?user=${certificate.username}">
                    你的评论
                  </a>
                </div>
                <div class="line"></div>
                <div>
                  <a href="account.jsp">
                    用户设置
                  </a>
                </div>
                <div>
                  <a href="logout">退出账户</a>
                </div>
              </div>
            </div>
          </details>
        </c:when> <c:otherwise>
          <a href="../login.jsp">登录</a>
        </c:otherwise> </c:choose>
      </div>
    </header>
    <div class="mainContent">
      <h1>上传文件</h1>
      <div class="createRepository">
        <!-- 这是 Apache Common FileUpload 要求的表单样式 -->
        <form action="../upload" enctype="multipart/form-data" method="post">
          <div class="fill-box">
            <label for="repoName">仓库名：&emsp;</label>
            <input id="repoName" name="repoName" type="text" placeholder="存储库名称" />
          </div>
          <div class="fill-box">
            <label for="pathName">相对路径：</label>
            <input id="pathName" name="pathName" type="text" placeholder="文件相对路径" />
          </div>
          <div class="fill-box">
            <label for="fileName">文件名：&emsp;</label>
            <input id="fileName" name="fileName" type="file"/>
          </div>
          <div class="fill-box" style="text-align: center">
            <input id="upload" type="submit" value="上传" />
          </div>
        </form>
      </div>
    </div>
  </div>
</body>

</html>

