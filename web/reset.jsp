<%--
  Created by JetBrains IntelliJ IDEA.
  User: Dragon1573
  Date: 2019/7/15
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session">
</jsp:useBean>
<html>

<head>
  <meta charset="UTF-8" />
  <link rel="stylesheet" type="text/css" href="css/forget.css" />
  <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico" />

  <script type="text/javascript" src="js/jquery-3.4.1.js"></script>
  <script type="text/javascript" src="js/signUpQuery.js"></script>
  <title>重置密码</title>
</head>

<body>
  <%
    if (!certificate.isVerified() || "Anonymous".equals(certificate.getUsername())) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("error404.html");
        dispatcher.forward(request, response);
    }
  %>
  <form name="newPassword" method="post" action="reset">
    <div class="forgetFrame">
      <%-- 新密码 --%>
      <label for="password">新密码</label>
      <input type="password" name="password" id="password" pattern="^\w{8,30}" placeholder="8～30位密码" required="required" />

      <%-- 重复确认 --%>
      <label for="confirm">确认密码</label>
      <p class="error" id="difference" hidden="hidden">密码不匹配，请重新输入！</p>
      <input type="password" name="confirm" id="confirm" placeholder="确认密码" required="required" oninput="confirmCheck(newPassword.password, newPassword.confirm);" />

      <%-- 提交 --%>
      <button type="submit">提交</button>
    </div>
  </form>
</body>

</html>
