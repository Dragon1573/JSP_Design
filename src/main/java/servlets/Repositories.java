package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "Repositories", urlPatterns = {"/repositories"})
public class Repositories extends HttpServlet {
    private static final long serialVersionUID = 7370782377273050355L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        // 指定编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println(JSON.toJSONString(Connector.fetchRepositories(request.getParameter("username"))));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        // 指定编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // 获取请求字段
        String method = request.getParameter("method");
        String username = request.getParameter("username");
        String newName = request.getParameter("new_name");
        String oldName = request.getParameter("old_name");

        boolean success = switch (method) {
            case "rename" -> Connector.renameRepositories(username, newName, oldName);
            case "delete" -> Connector.deleteRepositories(username, oldName);
            default -> false;
        };

        try (PrintWriter writer = response.getWriter()) {
            JSONObject object = new JSONObject();
            object.put("SUCCESS", success);
            writer.println(JSON.toJSONString(object));
        }
    }
}
