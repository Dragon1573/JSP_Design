<%--
  Created by JetBrains IntelliJ IDEA.
  User: Dragon1573
  Date: 2019/7/15
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="certificate" class="entities.UserInfo" />
<html>
  <head>
    <meta charset="UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/forget.css" />
    <link rel="shortcut icon" type="image/x-icon" href="../img/favicon.ico" />
    <script type="text/javascript" src="../bootstrap/jquery.min.js"></script>
    <script type="text/javascript" src="../js/platform.js"></script>
    <script type="text/javascript" src="../js/encrypt.js"></script>
    <title>重置密码</title>
  </head>
  <body>
    <form name="newPassword" method="post" action="${pageContext.request.contextPath}/reset">
      <div class="forgetFrame">
        <%-- 新密码 --%>
        <label for="password">新密码</label>
        <input type="password" name="password" id="password" pattern="^\w{8,30}" placeholder="8～30位密码"
               required="required" />
        <%-- 重复确认 --%>
        <label for="confirm">确认密码</label>
        <p class="error" id="difference" hidden="hidden">密码不匹配，请重新输入！</p>
        <input type="password" name="confirm" id="confirm" placeholder="确认密码" required="required"
               oninput="confirmCheck(newPassword.password, newPassword.confirm);" />
        <%-- 提交 --%>
        <button type="submit" onclick="encryptPassword()">提交</button>
      </div>
    </form>
  </body>
</html>
