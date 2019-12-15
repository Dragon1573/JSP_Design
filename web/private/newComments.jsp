<%--
  Created by JetBrains IntelliJ IDEA.
  User: Dragon1573
  Date: 2019/12/12
--%>

<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session" />
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" type="text/css" href="../css/index.css" />
  <link rel="shortcut icon" href="../img/favicon.ico" type="image/canvasObject-icon" />
  <script type="text/javascript" src="../bootstrap/jquery.min.js"></script>
  <script type="application/javascript" src="../js/platform.js"></script>
  <title>新建评论</title>
  <%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
  %>
  <style type="text/css">
    div.createComments {
      width: 1000px;
      padding: 5px;
      margin: 20px auto;
    }

    form, form * {
      text-align: left;
    }

    label {
      width: fit-content;
      margin: 20px auto;
    }

    input {
      width: available;
      margin: 20px auto;
    }

    textarea#details {
      width: 1000px;
      resize: none;
      height: 15em;
    }
  </style>
</head>

<body>
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
      <c:choose>
        <c:when test="${certificate.verified && !'Anonymous'.equals(certificate.username)}">
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
    <h1>新建评论</h1>
    <div class="createComments">
      <form name="commentForm" action="javascript:void(0)" method="post">
        <label>
          <input id="user" name="user" type="text" value="${certificate.username}"
                 hidden />
        </label>
        <label for="title">标题：</label>
        <input id="title" name="title" type="text" required />
        <label for="details"></label>
        <textarea id="details" name="details"></textarea>
        <div class="choices" style="margin: 20px auto; text-align: center;">
          <button type="submit" onclick="send();">
            提交
          </button>
          <button type="reset">清空</button>
        </div>
      </form>
      <script type="application/javascript">
          function send() {
              sendComments(
                  commentForm.user.value,
                  commentForm.title.value,
                  commentForm.details.value
              );
              window.location.href = "../comments.jsp";
          }
      </script>
    </div>
  </div>
</body>

</html>
