<%--
  Created by JetBrains IntelliJ IDEA.
  User: Dragon1573
  Date: 2019/12/12
--%><!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session" />
<%@ page import="java.util.Objects" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="utils.Connector" %>
<%request.setCharacterEncoding("UTF-8");
  response.setCharacterEncoding("UTF-8");
  response.setContentType("text/html; charset=UTF-8");
  final JSONObject object = Connector.getComments(request.getParameter("timestamp"));
  final boolean[] authorized = {
    certificate.isVerified() && !"Anonymous".equals(certificate.getUsername()), Objects.equals(
    certificate.getUsername(), object.getString("Sender"))
  };%>
<html>
  <head>
    <meta charset="UTF-8" http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/index.css" />
    <link rel="stylesheet" type="text/css" href="css/repository.css" />
    <link rel="stylesheet" type="text/css" href="css/settings.css" />
    <link rel="shortcut icon" href="img/favicon.ico" type="image/canvasObject-icon" />
    <script type="text/javascript" src="bootstrap/jquery.min.js"></script>
    <title>评论详情 - 佛大云服务</title>
    <style rel="stylesheet" type="text/css">
        div.details, div.details * {
            text-align: left;
        }

        p {
            padding: 5px 0;
            font-size: larger;
        }

        label {
            float: unset;
            font-size: large;
        }
    </style>
    <script type="application/javascript">
        function removeComments() {
            let checked = confirm("警告！\n" + "删除评论是破坏性操作，一旦删除不可撤销！\n" + "是否继续？");
            if (checked) {
                $.ajax({
                           url: "modify",
                           type: "POST",
                           dataType: "json",
                           data: {"timestamp": "${param.timestamp}"},
                           success: function (response) {
                               if (response["SUCCESS"]) {
                                   alert("删除成功");
                               } else {
                                   alert("错误：删除失败！");
                               }
                               window.location.href = "comments.jsp";
                           },
                           error: function () {
                               alert("错误：服务器异常！")
                           }
                       });
            }
        }
    </script>
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
          <c:choose> <c:when test="<%=authorized[0]%>">
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
        <h1>评论详情</h1>
        <div class="comments">
          <div class="details" style="width: fit-content;">
            <form action="javascript:void(0);" name="details">
              <table style="font-size: larger;">
                <tbody>
                  <tr>
                    <td style="width: fit-content">用户名：</td>
                    <td style="width: fit-content; max-width: 525px">
                      <%=object.getString("Sender")%>
                    </td>
                  </tr>
                  <tr>
                    <td style="width: fit-content">标题：</td>
                    <td style="max-width: 525px; width: fit-content;">
                      <%=object.getString("Title")%>
                    </td>
                  </tr>
                  <tr>
                    <td style="width: fit-content">内容：</td>
                    <td style="width: fit-content; max-width: 525px;">
                      <%=object.getString("Details")%>
                    </td>
                  </tr>
                </tbody>
              </table>
              <label>
                <input name="timestamp" type="hidden" value="${param.timestamp}" />
              </label>
              <label>
                <input name="user" type="hidden" value="${certificate.username}" />
              </label>
              <c:if test='<%=authorized[1]%>'>
                <div style="text-align: center;">
                  <button type="submit" onclick="removeComments();">删除</button>
                </div>
              </c:if>
            </form>
          </div>
        </div>
      </div>
    </div>
    <footer>
      <div>
        Powered by
        <a href="https://github.com/Dragon1573">@Dragon1573</a>
      </div>
    </footer>
  </body>
</html>
