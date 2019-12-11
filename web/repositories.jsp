<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session" />
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="css/index.css" />
  <link rel="stylesheet" type="text/css" href="css/repository.css" />
  <link rel="shortcut icon" href="img/favicon.ico" type="image/canvasObject-icon" />
  <script type="text/javascript" src="bootstrap/jquery.min.js"></script>
  <script type="text/javascript" src="js/platform.js"></script>
  <script type="text/javascript">
      $(function () {
          if ("${param.user}" === "") {
              // 页面加载时首次刷新
              fetchRepositories(false, "Anonymous");
              // 每15秒刷新一次仓库列表
              setInterval(fetchRepositories, 15000, false, "Anonymous");
          } else {
              fetchRepositories(false, "${param.user}");
              setInterval(fetchRepositories, 15000, false, "${param.user}");
          }
      });
  </script>
  <%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
  %>
  <c:choose> <c:when test="${param.user eq null}">
    <title id="username">公共仓库 - 佛大云服务</title>
  </c:when> <c:otherwise>
    <title id="username">${param.user} - 佛大云服务</title>
  </c:otherwise> </c:choose>
</head>

<body>
  <div class="mainContainer">
    <header>
      <div id="logo">
        <a href="http://web.fosu.edu.cn/">
          <img alt="佛山科学技术学院" src="img/fosu-logo.png" />
        </a>
      </div>
      <div id="blank">
        <table id="navigator">
          <tbody>
            <tr>
              <td>
                <a href="index.jsp">首页</a>
              </td>
              <td>
                <a href="repositories.jsp">仓库</a>
              </td>
              <td>
                <a href="comments.jsp">评论</a>
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
                  <a href="comments.jsp?user=${certificate.username}">
                    你的评论
                  </a>
                </div>
                <div class="line"></div>
                <div>
                  <a href="private/account.jsp">
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
          <a href="login.jsp">登录</a>
        </c:otherwise> </c:choose>
      </div>
    </header>
    <div class="mainContent">
      <h1>仓库列表</h1>
      <div class="comments">
        <!-- 确认用户已经登录成功 -->
        <c:if test="${certificate.verified && !'Anonymous'.equals(certificate.username)}">
          <div class="searchBar" style="text-align: right;">
            <a class="newRepository" href="private/newRepo.jsp">上传</a>
          </div>
        </c:if>
        <table id="repoTable">
          <tbody id="repositories"></tbody>
        </table>
      </div>
    </div>
  </div>
  <footer>
    <div>
      Powered by
      <a href="https://github.com/Dragon1573/">@Dragon1573</a>
    </div>
  </footer>
</body>

</html>
