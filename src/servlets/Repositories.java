package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import utils.Connector;

/**
 * @author Dragon1573
 * @date 2019/7/17
 */
@WebServlet(name = "Repositories", urlPatterns = {"/repositories"})
public class Repositories extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 指定编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // 获取请求字段
        String username = request.getParameter("username");

        // 连接数据库
        Connector connector = new Connector();
        ResultSet resultSet = connector.fetchRepositories(username);

        // 创建JSON数组
        ArrayList<JSONObject> list = new ArrayList<>();
        try {
            while (resultSet != null && resultSet.next()) {
                JSONObject object = new JSONObject();
                object.put("USERNAME", resultSet.getString("Username"));
                object.put("REPOSITORY", resultSet.getString("Repository"));
                list.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 转换为JSON字符串
        String jsonStr = JSON.toJSONString(list);

        // 输出
        PrintWriter out = response.getWriter();
        out.println(jsonStr);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 指定编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // 获取请求字段
        String method = request.getParameter("method");
        String username = request.getParameter("username");
        String newName = request.getParameter("new_name");
        String oldName = request.getParameter("old_name");

        Connector connector = new Connector();
        boolean success = false;

        switch (method) {
            case "rename":
                success = connector.renameRepositories(username, newName, oldName);
                break;

            case "delete":
                success = connector.deleteRepositories(username, oldName);
                break;

            default:
                break;
        }

        JSONObject object = new JSONObject();
        object.put("SUCCESS", success);
        String json = JSON.toJSONString(object);
        response.getWriter().println(json);
    }
}
