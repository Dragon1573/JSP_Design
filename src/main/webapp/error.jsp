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
  <script src="${path}/bootstrap/jquery.min.js" type="text/javascript"></script>
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
  <canvas id="christmasCanvas"></canvas>
  <script src="${path}/js/platform.js" type="text/javascript"></script>
  <script type="text/javascript">
      document.addEventListener(
          "touchmove",
          function (e) {
              // 不执行默认方法
              e.preventDefault();
          }
      );


      let canvas = $("canvas")[0];
      let canvasObject = canvas.getContext('2d');
      let zoomRatio = window.devicePixelRatio || 1;
      let width = window.innerWidth;
      let height = window.innerHeight;
      let f = 90;
      let q;
      let u = Math.PI * 2;

      canvas.width = width * zoomRatio;
      canvas.height = height * zoomRatio;
      canvasObject.scale(zoomRatio, zoomRatio);
      canvasObject.globalAlpha = 0.6;

      document.onclick = drawCanvas;
      document.ontouchstart = drawCanvas;
      drawCanvas();

      snow();
  </script>
</body>

</html>
