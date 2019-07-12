<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="certificate" class="entities.UserInfo" scope="session">
</jsp:useBean>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="css/index.css" />
  <link rel="shortcut icon" href="img/favicon.ico" type="image/canvasObject-icon" />
  <script type="text/javascript" src="js/jquery-3.4.1.js"></script>
  <script type="text/javascript" src="js/fetchComments.js"></script>
  <%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    if (request.getParameter("user") == null) {
      out.print("<title id='username'>佛大云服务</title>");
    } else {
      out.print("<title id='username'>" + request.getParameter("user") + "</title>");
    }
  %>
</head>

<body>
  <div class="mainContainer">
    <header>
      <div id="logo">
        <a href="index.jsp">
          <img alt="佛山科学技术学院" src="img/fosu-logo.png" />
        </a>
      </div>
      <div id="blank"></div>
      <div id="profile">
        <%
          if (certificate.isVerified() && !"Anonymous".equals(certificate.getUsername())) {
        %>
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
                <a href="repositories.jsp?user=<%=certificate.getUsername()%>">
                  你的仓库
                </a>
              </div>
              <div>
                <a href="comments.jsp?user=<%=certificate.getUsername()%>">
                  你的评论
                </a>
              </div>
              <div class="line"></div>
              <div>
                <a href="settings.jsp?user=<%=certificate.getUsername()%>">
                  用户设置
                </a>
              </div>
              <div>
                <a href="logout">退出账户</a>
              </div>
            </div>
          </div>
        </details>
        <%} else {%>
        <a href="login.jsp">登录</a>
        <%}%>
      </div>
    </header>
    <div class="mainContent">
      <div class="leftContainer">
        <h1>仓库列表</h1>
      </div>
      <div class="rightContainer">
        <h1>评论区</h1>
        <div class="comments">
          <table>
            <tbody id="comments"></tbody>
          </table>
        </div>
        <%
          if (certificate.isVerified() && !"Anonymous".equals(certificate.getUsername())) {
        %>
        <label for="sender">发表评论：</label>
        <textarea id="sender" class="comments"></textarea>
        <%
          }
        %>
      </div>
    </div>
    <footer>
      <div>Powered by
        <a href="https://github.com/Dragon1573/">@Dragon1573</a>
      </div>
    </footer>
  </div>
</body>

</html>
