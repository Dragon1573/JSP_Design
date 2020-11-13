package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.UserInfo;
import utils.Connector;

/**
 * Servlet：注册
 *
 * @author Dragon1573
 */
@WebServlet(name = "SignUp", urlPatterns = {"/signUp"})
public class SignUp extends HttpServlet {
    private static final long serialVersionUID = 735339428785258397L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("警告：此方法仅用于Visio生成网站图，请使用POST方法！");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        // 设置请求编码格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 获取Session及其JavaBean
        HttpSession session = request.getSession();
        UserInfo certificate = (UserInfo)session.getAttribute("certificate");

        // 设置跳转地址
        String responseUrl = null;

        // 设置用户档案
        HashMap<String, String> profile = new HashMap<>(7);
        profile.put("Username", request.getParameter("username"));
        profile.put("Password", request.getParameter("password"));
        profile.put("Confirm", request.getParameter("confirm"));
        profile.put("Phone", request.getParameter("phone"));
        profile.put("Email", request.getParameter("mail"));
        profile.put("Question", request.getParameter("question"));
        profile.put("Answer", request.getParameter("answer"));

        // 用户入库
        int effect = Connector.signUp(profile);
        if (effect <= 0) {
            // 用户信息未存入数据库
            request.setAttribute("error", "SignUpFailedError");
            responseUrl = "signUp.jsp";
        }

        // Session中没有任何错误信息
        if (responseUrl == null) {
            certificate.setUsername(request.getParameter("username"));
            certificate.setVerified(true);
            responseUrl = "index.jsp?user=" + certificate.getUsername();
        }

        // 重定向页面
        request.getRequestDispatcher(responseUrl).forward(request, response);
    }
}
