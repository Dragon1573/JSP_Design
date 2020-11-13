package servlets.hidden;

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
@WebServlet(name = "Remove", urlPatterns = {"/modify"})
public class Remove extends HttpServlet {
    @Override
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response
    )
        throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 移除评论
        boolean isRemoved = Connector.deleteComments(request.getParameter("timestamp"));

        // 输出
        try (PrintWriter writer = response.getWriter()) {
            JSONObject object = new JSONObject(1);
            object.put("SUCCESS", isRemoved);
            writer.println(JSON.toJSONString(object));
        }
    }
}
