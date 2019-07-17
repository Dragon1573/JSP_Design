package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.UserInfo;
import utils.Connector;
import utils.Md5Util;

/**
 * Servlet：用户登录
 *
 * @author Dragon1573
 * @date 2019/7/7
 */
@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {
    private static final long serialVersionUID = -6477635060462144660L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        PrintWriter out = response.getWriter();
        out.println("警告：此方法仅用于Visio生成网站图，请使用POST方法！");
    }

    @Override
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        // 设置请求与回复编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 获取Session及其范围内的JavaBean
        HttpSession session = request.getSession(true);
        UserInfo certificate = (UserInfo)session.getAttribute("certificate");

        // 获取用户输入的登录信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //查询数据库，进行身份校验
        Connector connector = new Connector();
        ResultSet resultSet = connector.login(username);
        boolean success = false;
        try {
            while (resultSet != null && resultSet.next()) {
                String s = resultSet.getString(1);
                success = s.equals(Md5Util.encrypt(password));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        // 判断是否登陆成功
        String redirectUrl;
        if (success) {
            certificate.setUsername(username);
            certificate.setVerified(true);
            redirectUrl = "index.jsp?user=" + certificate.getUsername();
        } else {
            certificate.setUsername("Anonymous");
            certificate.setVerified(false);
            redirectUrl = "login.jsp";
        }

        // 覆盖Session中的JavaBean
        session.setAttribute("certificate", certificate);
        response.sendRedirect(redirectUrl);
    }
}
