package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import utils.Connector;

/**
 * 同步评论
 *
 * @author Dragon1573
 */
@WebServlet(name = "Comments", urlPatterns = {"/sync"})
public class Comments extends HttpServlet {
    private static final long serialVersionUID = -2856249976292985991L;

    /**
     * 获取评论
     *
     * @param request
     *     用户请求
     * @param response
     *     服务器响应
     *
     * @throws IOException
     *     I/O异常
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        // 设定编码格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // 获取用户名
        String username = request.getParameter("username");

        // 获取评论
        Connector connector = new Connector();
        ResultSet resultSet = connector.fetchComments(username);

        // 生成JSON数据表
        JSONArray vector = new JSONArray();
        try {
            while (resultSet != null && resultSet.next()) {
                JSONObject object = new JSONObject();
                object.put("SENDER", resultSet.getString("Sender"));
                object.put("TITLE", resultSet.getString("Title"));
                object.put("DETAILS", resultSet.getString("Details"));
                // 将SQL Server中的时间转换为字符串
                object.put("DATETIME", resultSet.getString("DateTime"));
                // 将单个JSON数据加入表中
                vector.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 输出JSON字符串
        try (PrintWriter writer = response.getWriter()) {
            writer.println(JSON.toJSONString(vector));
        }
    }

    /**
     * 发送评论
     *
     * @param request
     *     用户请求
     * @param response
     *     服务器响应
     *
     * @throws IOException
     *     I/O异常
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        // 设置编码格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 获取评论详情
        String username = request.getParameter("user");
        String title = request.getParameter("title");
        String comments = request.getParameter("details");

        // 访问数据库
        Connector connector = new Connector();
        boolean isSaved = connector.sendComments(username, title, comments);

        // 转换为JSON字符串
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("FLAG", isSaved);
        String jsonStr = JSON.toJSONString(jsonObject);

        // 输出
        PrintWriter out = response.getWriter();
        out.println(jsonStr);
        out.close();
    }
}
