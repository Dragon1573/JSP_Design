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
@WebServlet(name = "Remove", urlPatterns = {"/modify"})
public class Remove extends HttpServlet {
    @Override
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        Connector connector = new Connector();
        JSONObject object = new JSONObject();
        object.put(
            "SUCCESS",
            connector.deleteComments(request.getParameter("timestamp"))
        );
        response.sendRedirect("comments.jsp?user=" + request.getParameter("user"));
    }
}
