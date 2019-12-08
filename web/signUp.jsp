<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session">
</jsp:useBean>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="css/signUp.css" />
  <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon" />
  <title>新用户注册</title>
  <script type="text/javascript" src="bootstrap/jquery.min.js"></script>
  <script type="text/javascript" src="js/platform.js"></script>
</head>

<body>
  <div class="signUpFrame">
    <form name="form" action="signUp" method="post">
      <h1>创建账户</h1>
      <div class="profile">

        <label for="username">用户名</label>
        <!-- 没有错误时，报错信息不显示 -->
        <p class="error" id="unique" hidden="hidden">错误：用户已存在，请重试！</p>
        <input type="text" name="username" id="username"
               onchange="uniqueCheck(form.username);"
               pattern="^[0-9a-zA-Z_]{2, 20}$"
               placeholder="2～20位，支持数字、字母、下划线" required="required" />

        <label for="password">密码</label>
        <input type="password" name="password" id="password" pattern="^\w{8,30}"
               placeholder="8～30位任意密码" required="required" />

        <label for="confirm">确认密码</label>
        <p class="error" id="difference" hidden="hidden">密码不匹配，请重新输入！</p>
        <input type="password" name="confirm" id="confirm" placeholder="确认密码"
               required="required"
               oninput="confirmCheck(form.password, form.confirm);" />

        <label for="phone">手机号码（可选）</label>
        <input type="tel" name="phone" id="phone" pattern="^\d{11}$"
               placeholder="11位手机号码" />

        <label for="mail">电子邮箱（可选）</label>
        <input type="email" name="mail" id="mail" pattern="(\w+)@(.+)\.(.+)"
               placeholder="小于25位字符长度" />

        <label for="question">密保问题（可选）</label>
        <input type="text" name="question" id="question" pattern=".{1,50}"
               placeholder="小于50字符长度" />

        <label for="answer">密保答案（可选）</label>
        <input type="text" name="answer" id="answer" pattern=".{1,50}"
               placeholder="小于50字符长度" />
      </div>
      <button type="submit">注册</button>
      <c:if test="${error eq 'SignUpFailedError'}">
        <p class="error">注册失败，请稍后重试……</p>
      </c:if>
    </form>
  </div>
</body>

</html>
