<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setCharacterEncoding("UTF-8");

  final String path = request.getContextPath();
  request.setAttribute("path", path);
  request.setAttribute("status", response.getStatus());
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh">

<head>
  <meta charset="UTF-8" content="text/html" http-equiv="Content-Type" />
  <meta content="initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport" />
  <title>
    <c:choose>
      <c:when test="${status eq 401}">
        <c:out value="访问未授权" />
      </c:when>
      <c:when test="${status eq 404}">
        <c:out value="页面未找到" />
      </c:when>
      <c:when test="${status eq 500}">
        <c:out value="服务器运行时异常" />
      </c:when>
    </c:choose>
  </title>

  <link href="${path}/css/error.css" rel="stylesheet" type="text/css" />
  <script src="${path}/js/jquery-3.4.1.js" type="text/javascript"></script>
</head>

<body>
  <div id="wrapper">
    <h1>
      <c:choose>
        <c:when test="${status eq 401}">
          <c:out value="HTTP 401" />
        </c:when>
        <c:when test="${status eq 404}">
          <c:out value="HTTP 404" />
        </c:when>
        <c:when test="${status eq 500}">
          <c:out value="HTTP 500" />
        </c:when>
      </c:choose>
    </h1>
    <h2>
      <c:choose>
        <c:when test="${status eq 401}">
          <c:out value="未获得授权，此页面禁止被直接访问！" />
        </c:when>
        <c:when test="${status eq 404}">
          <c:out value="啊哦，你要找的页面走丢了……" />
        </c:when>
        <c:when test="${status eq 500}">
          <c:out value="啊哦，网页服务器崩溃了……" />
        </c:when>
      </c:choose>
    </h2>
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
