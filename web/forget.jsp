<%--
  Created by JetBrains IntelliJ IDEA.
  User: Drogon1573
  Date: 2019/7/5
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session">
</jsp:useBean>
<jsp:useBean id="connector" class="utils.Connector" scope="session">
</jsp:useBean>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="css/forget.css"/>
  <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon"/>
  <title>密码找回</title>

  <!-- 引入最新版的jQuery -->
  <script type="text/javascript" src="js/jquery-3.4.1.js"></script>
  <script type="text/javascript" src="js/forgetQuery.js"></script>
</head>

<body>
  <header>
    <h1>密码找回</h1>
  </header>
  <form name="resetForm" method="post" action="forget">
    <div class="forgetFrame">
      <label for="username">用户名</label>
      <input type="text" name="username" id="username"
             onchange="checkUser(resetForm.username);" required="required"/>
      <div class="error" id="errorMessage"></div>

      <%-- 密保问题 --%>
      <label id="question" hidden="hidden" for="answer"></label>
      <input type="text" name="answer" id="answer" hidden="hidden"
             required="required"/>

      <button type="submit">找回</button>
    </div>
  </form>
</body>

</html>
