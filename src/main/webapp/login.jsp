<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session" />
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/login.css" />
    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon" />
    <title>用户登录</title>
    <script type="text/javascript" src="bootstrap/jquery.min.js"></script>
    <script type="text/javascript" src="bootstrap/md5.min.js"></script>
    <script type="text/javascript">
      /* @formatter:off */
      function e() {let d = $('form'), i = d.find('#password'), n = d.find('#confirm'), o = b64_hmac_md5(i, i);i.value = o, void 0 !== n && (n.value = o), d.submit()}
      /* @formatter:on */
    </script>
  </head>
  <body>
    <header>
      <h1>佛山大学源代码托管平台</h1>
    </header>
    <form name="form" method="post" action="login">
      <div class="loginFrame">
        <label for="username">用户名</label>
        <input type="text" name="username" id="username" placeholder="用户名/手机号/邮箱地址" required="required" />
        <label for="password">密码</label>
        <a style="float: right;" href="forget.jsp">忘记密码？</a>
        <input type="password" name="password" id="password" required="required" />
        <c:if test="${!certificate.verified}">
          <div style="color: red; font-weight: bold;">登陆失败：用户名或密码错误！</div>
        </c:if>
        <input type="submit" id="login" value="登录" onclick="e()" />
      </div>
    </form>
    <div class="signUpFrame">
      新用户？
      <a href="signUp.jsp">创建一个账户</a>
    </div>
  </body>
</html>
