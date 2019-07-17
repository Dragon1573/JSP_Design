package filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.UserInfo;

/**
 * @author Dragon1573
 * @date 2019/7/16
 */
@WebFilter(filterName = "Authenticate", urlPatterns = {"/private/*", "/servlets/private/*"})
public class Authenticate implements javax.servlet.Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // 获取所需对象
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        HttpSession session = request.getSession();

        // 设置编码格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 进行过滤
        final String anonymous = "Anonymous";
        UserInfo info = (UserInfo)session.getAttribute("certificate");
        if (info == null || anonymous.equals(info.getUsername()) || !info.isVerified()) {
            PrintWriter out = response.getWriter();
            out.println("<h1 style='text-align: center'>非法访问！正在返回主页</h1>");

            // 延时跳转
            response.setHeader("refresh", "3; url=../index.jsp");
        } else {
            chain.doFilter(req, resp);
        }
    }
}
