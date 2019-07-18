<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setCharacterEncoding("UTF-8");

  final String path = request.getContextPath();
  request.setAttribute("path", path);
%>
<!DOCTYPE html>
<html lang="zh">

<head>
  <meta charset="UTF-8" content="text/html" http-equiv="Content-Type" />
  <meta content="initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport" />
  <title>访问未授权</title>

  <link href="${path}/css/error.css" rel="stylesheet" type="text/css" />
  <script src="${path}/js/jquery-3.4.1.js" type="text/javascript"></script>
</head>

<body>
  <div id="wrapper">
    <h1>HTTP 401</h1>
    <h2>未获得授权，此页面禁止被直接访问！</h2>
    <p>
      <a href="${path}/index.jsp">首页</a>
    </p>
    <p>
      <a href="https://github.com/Dragon1573/JSP_Design">Github</a>
    </p>
  </div>
  <canvas></canvas>
  <script src="${path}/js/error.js" type="text/javascript"></script>
  <canvas id="christmasCanvas"></canvas>
</body>

</html>
