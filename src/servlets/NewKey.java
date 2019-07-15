package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.UserInfo;
import utils.Connector;

/**
 * @author Dragon1573
 * @date 2019/7/15
 */
@WebServlet(name = "NewKey", urlPatterns = {"/reset"})
public class NewKey extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 取出Session
        HttpSession session = request.getSession();
        UserInfo info = (UserInfo)session.getAttribute("certificate");

        String username = info.getUsername();
        String password = request.getParameter("password");

        Connector connector = new Connector();
        connector.reset(username, password);
        response.sendRedirect("index.jsp?user=" + username);
    }
}
