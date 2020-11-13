package servlets.hidden;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import entities.UserInfo;
import utils.Connector;
import utils.Md5Util;

/**
 * @author Dragon1573
 */
@WebServlet(name = "Settings", urlPatterns = {"/settings"})
public class Settings extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置编码格式
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        // 获取表单数据
        String fields = request.getParameter("fields");
        String news = request.getParameter("news");
        String old = request.getParameter("old");
        UserInfo info = (UserInfo)session.getAttribute("certificate");
        String username = info.getUsername();

        // 变更数据库
        boolean success = false;
        switch (fields) {
            case "Username":
                success = Connector.changeUsername(news, username);
                info.setUsername(news);
                break;

            case "Password":
                String newPass = Md5Util.encrypt(news);
                String oldPass = Md5Util.encrypt(old);
                success = Connector.changePassword(username, newPass, oldPass);
                break;

            case "Phone":
                success = Connector.changePhone(username, news);
                break;

            case "Protection":
                success = Connector.changeProtection(username, news, old);
                break;

            case "Email":
                success = Connector.changeEmail(username, news);
                break;

            default:
                break;
        }

        // 生成JSON数据集并输出
        JSONObject object = new JSONObject();
        object.put("SUCCESS", success);
        String json = JSON.toJSONString(object);
        response.getWriter().println(json);
    }
}
