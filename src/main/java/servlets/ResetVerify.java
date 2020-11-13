package servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.UserInfo;
import utils.Connector;

/**
 * @author Dragon1573
 */
@WebServlet(name = "ResetVerify", urlPatterns = {"/forget"})
public class ResetVerify extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String answer = request.getParameter("answer");

        HttpSession session = request.getSession();
        if (Connector.resetVerify(username, answer)) {
            UserInfo info = (UserInfo)session.getAttribute("certificate");
            info.setVerified(true);
            info.setUsername(username);
            session.setAttribute("certificate", info);
            response.sendRedirect("private/reset.jsp");
        } else {
            session.setAttribute("Error", false);
            response.sendRedirect("forget.jsp");
        }
    }
}
