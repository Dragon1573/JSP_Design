package servlets;

import entities.UserInfo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet：用户退出
 *
 * @Author: Dragon1573
 * @Date: 2019/7/7
 */
@WebServlet(name = "Logout", urlPatterns = {"/logout"})
public class Logout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        // 获取Session与JavaBean
        HttpSession session = request.getSession();
        UserInfo certificate = (UserInfo)session.getAttribute("certificate");

        // 更新JavaBean内容
        certificate.setUsername("Anonymous");
        certificate.setVerified(true);

        // 更新Session中的JavaBean
        session.setAttribute("certificate", certificate);

        // 重定向
        response.sendRedirect("index.jsp");
    }
}
