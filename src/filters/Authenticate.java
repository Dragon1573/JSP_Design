package filters;

import java.io.IOException;

import javax.servlet.*;
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
public class Authenticate implements Filter {
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
            response.sendError(401);
        } else {
            chain.doFilter(req, resp);
        }
    }
}
