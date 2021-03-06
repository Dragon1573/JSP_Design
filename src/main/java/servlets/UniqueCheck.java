package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import utils.Connector;

/**
 * 用户名唯一性检验
 *
 * @author Dragon1573
 */
@WebServlet(name = "UniqueCheck", urlPatterns = {"/check"})
public class UniqueCheck extends HttpServlet {
    private static final long serialVersionUID = -5094519874151185796L;

    private enum Info {
        /**
         * 用户唯一
         */
        UNIQUE,
        /**
         * 未设置密保
         */
        NO_PROTECTION,
        /**
         * 重复的用户
         */
        DUPLICATE
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置编码格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // 获取用户表单内容
        String username = request.getParameter("username");

        // 用户存在性检验
        boolean isExists = Connector.uniqueCheck(username);

        // 生成JSON
        Map<String, Object> jsonMap = new HashMap<>(2);
        if (isExists) {
            // 获取密保问题
            String question = Connector.getQuestion(username);
            if (question == null) {
                // 无密保问题
                jsonMap.put("FLAG", Info.NO_PROTECTION);
            } else {
                // 存在密保问题
                jsonMap.put("FLAG", Info.DUPLICATE);
                jsonMap.put("QUESTION", question);
            }
        } else {
            // 用户不存在
            jsonMap.put("FLAG", Info.UNIQUE);
        }

        // 生成JSON字符串
        String jsonString = JSON.toJSONString(jsonMap);

        // 输出
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonString);
        }
    }
}
