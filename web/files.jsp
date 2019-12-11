<%--
  Created by JetBrains IntelliJ IDEA.
  User: Dragon1573
  Date: 2019/12/9
--%><!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="utils.Connector" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session" />
<html>

<head>
  <meta charset="UTF-8" http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="css/index.css" />
  <link rel="stylesheet" type="text/css" href="css/repository.css" />
  <link rel="stylesheet" type="text/css" href="css/settings.css" />
  <link rel="shortcut icon" href="img/favicon.ico" type="image/canvasObject-icon" />
  <script type="text/javascript" src="bootstrap/jquery.min.js"></script>
  <title>仓库详情 - ${param.user}/${param.repo}</title>
  <script type="application/javascript">
      function deleteFolders(username, repository, path) {
          let checked = confirm(
              "警告！\n" +
              "递归删除目录是破坏性操作！\n" +
              "是否继续？"
          );
          if (checked) {
              $.ajax({
                  url: "/JSP_Design/delete",
                  type: "POST",
                  dataType: "json",
                  data: {
                      "user": username,
                      "repo": repository,
                      "path": path
                  },
                  success: function (response) {
                      alert(response["SUCCESS"] ? "删除成功！" : "删除失败！");
                      if (response["EMPTY"]) {
                          // 仓库已删除
                          window.location.href = "repositories.jsp?user=${param.user}";
                      } else {
                          // 刷新当前目录
                          window.location.reload();
                      }
                  },
                  error: function () {
                      alert("错误！服务器连接异常！");
                      window.location.reload();
                  }
              });
          }
      }
  </script>
  <%
    // 设置请求编码方式
    request.setCharacterEncoding("UTF-8");
    // 获取属性迭代器
    Enumeration<String> enumeration = request.getAttributeNames();
    while (enumeration.hasMoreElements()) {
      // 清空属性
      request.removeAttribute(enumeration.nextElement());
    }
    Connector connector = new Connector();
    request.setAttribute("array", connector.listFiles(
        request.getParameter("user"),
        request.getParameter("repo"),
        request.getParameter("path")
    ));
    // 设置相应编码方式
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
  %>
</head>

<body>
  <div class="mainContainer">
    <header>
      <div id="logo">
        <a href="http://web.fosu.edu.cn/">
          <img alt="佛山科学技术学院" src="img/fosu-logo.png" />
        </a>
      </div>
      <div id="blank">
        <table id="navigator">
          <tbody>
            <tr>
              <td>
                <a href="index.jsp">首页</a>
              </td>
              <td>
                <a href="repositories.jsp">仓库</a>
              </td>
              <td>
                <a href="comments.jsp">评论</a>
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
                    <a href="comments.jsp?user=${certificate.username}">
                      你的评论
                    </a>
                  </div>
                  <div class="line"></div>
                  <div>
                    <a href="private/account.jsp">
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
          <a href="login.jsp">登录</a>
        </c:otherwise> </c:choose>
      </div>
    </header>
    <div class="mainContent">
      <h1>${param.user}/${param.repo}</h1>
      <div class="comments">
        <table id="repoTable">
          <tbody id="repositories">
            <c:forEach var="file" items="${array}">
              <tr>
                <td style="width: 85%;">
                  <c:choose>

                    <c:when test="${file.isDir == true}">
                      <a
                          href="files.jsp?user=${param.user}&repo=${param.repo}&path=${file.Name}">
                          ${file.Name}
                      </a>
                    </c:when>

                    <c:otherwise>
                      <a
                          href="download?user=${param.user}&repo=${param.repo}&file=${file.Name}">
                          ${file.Name}
                      </a>
                    </c:otherwise>

                  </c:choose>
                </td>
                <!-- 只有用户正常登录、不是匿名、为仓库所有者时才允许删除 -->
                <c:if test="${certificate.verified == true &&
                !'Anonymous'.equals(certificate.username) &&
                certificate.username.equals(param.user)}">
                  <td style="width: 15%;">
                    <a
                        href="javascript:deleteFolders('${param.user}','${param.repo}','${file.Name}');">
                      递归删除
                    </a>

                  </td>
                </c:if>
              </tr>
            </c:forEach>
          </tbody>
          <script type="application/javascript">
              let $list = $("tbody#repositories");
              if ($list.html().trim().length === 0) {
                  $list.html(
                      "<tr>" +
                      "<td style='font-size:larger'>" +
                      "<span class='warning'>空目录！</span>" +
                      "</td>" +
                      "</tr>"
                  );
              }
          </script>
        </table>
      </div>
    </div>
  </div>
  <footer>
    <div>
      Powered by
      <a href="https://github.com/Dragon1573/">@Dragon1573</a>
    </div>
  </footer>
</body>

</html>
