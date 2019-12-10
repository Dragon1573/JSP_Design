package servlets.hidden;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import utils.Connector;

/**
 * @author Dragon1573
 */
@WebServlet(name = "Delete", urlPatterns = {"/delete"})
public class Delete extends HttpServlet {
    @Override
    protected void doPost(
        final HttpServletRequest request, final HttpServletResponse response
    ) throws ServletException, IOException {
        // 设置编码格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        // 连接数据库并执行递归删除
        Connector connector = new Connector();
        final boolean success = connector.deleteFolder(
            request.getParameter("user"),
            request.getParameter("repo"),
            request.getParameter("path")
        );
        JSONObject object = new JSONObject();
        object.put("SUCCESS", success);
        final String json = JSON.toJSONString(object);
        response.getWriter().println(json);
    }
}
