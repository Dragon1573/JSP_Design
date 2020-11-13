package servlets.hidden;

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
 * @author Dragon1573
 */
@WebServlet(name = "Delete", urlPatterns = {"/delete"})
public class Delete extends HttpServlet {
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException {
        // 设置编码格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 连接数据库并执行递归删除
        final boolean success = Connector.deleteFolder(
            request.getParameter("user"),
            request.getParameter("repo"),
            request.getParameter("path")
        );
        JSONObject object = new JSONObject();
        object.put("SUCCESS", success);

        // 检查用户是否还有仓库
        JSONArray array = Connector.fetchRepositories(request.getParameter("user"));
        object.put("EMPTY", array.isEmpty());

        try (PrintWriter writer = response.getWriter()) {
            writer.println(JSON.toJSONString(object));
        }
    }
}
