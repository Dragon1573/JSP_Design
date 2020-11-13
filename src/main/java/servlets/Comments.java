package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

        // 输出JSON字符串
        try (PrintWriter writer = response.getWriter()) {
            String username = request.getParameter("username");
            JSONArray array = Connector.fetchComments(username);
            writer.println(JSON.toJSONString(array));
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
        boolean isSaved = Connector.sendComments(username, title, comments);

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
