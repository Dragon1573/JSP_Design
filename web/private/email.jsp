<%--
  Created by JetBrains IntelliJ IDEA.
  User: Dragon1573
  Date: 2019/7/18
--%><!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session" />
<%
  request.setCharacterEncoding("UTF-8");

  request.setAttribute("path", request.getContextPath());
%>
<html>

<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="../css/settings.css" />
  <link rel="icon" href="../img/favicon.ico" />
  <script type="text/javascript" src="${path}/js/jquery-3.4.1.js"></script>
  <script type="text/javascript" src="${path}/js/platform.js"></script>
  <title>用户设置</title>
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
                <a href="../repositories.jsp">仓库</a>
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
                  <a href="../repositories.jsp?user=${certificate.username}">
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
                  <a href="${path}/logout">退出账户</a>
                </div>
              </div>
            </div>
          </details>
        </c:when> <c:otherwise>
          <a href="../login.jsp">登录</a>
        </c:otherwise> </c:choose>
      </div>
    </header>
    <div class="body">
      <div class="left">
        <nav>
          <h3 class="label">个人设置</h3>
          <a class="item" href="account.jsp">账户</a>
          <a class="item" href="security.jsp">安全性</a>
          <a class="item selected" href="email.jsp">电子邮箱</a>
          <a class="item" href="repositories.jsp">代码仓库</a>
        </nav>
      </div>
      <div class="right">
        <h1 class="subTitle">修改电子邮箱</h1>
        <form id="change_email" accept-charset="UTF-8" method="post">
          <dl>
            <dt>
              <label for="new_email">新电子邮箱</label>
            </dt>
            <dd>
              <input type="email" id="new_email" name="new_email" />
              <input type="hidden" id="old_email" name="old_email" value="" />
            </dd>
          </dl>
          <button type="button" onclick="updateProfile('Email', new_email, old_email);">提交</button>
        </form>
      </div>
    </div>
  </div>
  <footer>
    <div>
      Powered by
      <a href="https://github.com/Dragon1573">@Dragon1573</a>
    </div>
  </footer>
</body>

</html>
