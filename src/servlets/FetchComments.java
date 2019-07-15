package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import utils.Connector;

/**
 * 获取评论
 *
 * @author Dragon1573
 * @date 2019/7/11
 */
@WebServlet(name = "FetchComments", urlPatterns = {"/fetch"})
public class FetchComments extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设定编码格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // 获取用户名
        String username = request.getParameter("username");

        // 获取评论
        Connector connector = new Connector();
        ResultSet resultSet = connector.fetchComments(username);

        // 生成JSON数据表
        Vector<HashMap> vector = new Vector<>();
        try {
            while (resultSet != null && resultSet.next()) {
                HashMap<String, Object> jsonMap = new HashMap<>(3);
                jsonMap.put("SENDER", resultSet.getString("Sender"));
                jsonMap.put("DETAILS", resultSet.getString("Details"));

                // 将SQL Server中的时间转换为字符串
                Timestamp timestamp = resultSet.getTimestamp("DateTime");
                jsonMap.put("DATETIME", timestamp);

                // 将单个JSON数据加入表中
                vector.add(jsonMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 将JSON数组序列化并发送
        String jsonString = JSON.toJSONStringWithDateFormat(vector, "yyyy-MM-dd HH:mm:ss");
        PrintWriter out = response.getWriter();
        out.println(jsonString);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String message = request.getParameter("message");
        // 使用Alibaba FastJSON还原JSON对象时
        // 得到对象类型为 com.alibaba.fastjson.JSONObject
        // 其形式与HashMap类似
        JSONObject object = JSON.parseObject(message);

        // TODO 发送评论功能未完成
    }
}
