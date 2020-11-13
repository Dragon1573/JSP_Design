package servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Connector;

/**
 * @author Dragon1573
 */
@WebServlet(name = "Download", urlPatterns = {"/download"})
public class Download extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置请求与响应的格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-msdownload");

        // 获取文件名及其内容
        final String username = request.getParameter("user");
        final String repository = request.getParameter("repo");
        String filename = request.getParameter("file");
        byte[] content = Connector.getFiles(username, repository, filename);
        filename = filename.substring(filename.lastIndexOf("/") + 1);
        filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);

        // 设置响应头并准备写入文件
        response.addHeader("Content-Disposition", "attachment; filename=" + filename);
        OutputStream stream = response.getOutputStream();
        stream.write(content);
        stream.flush();
        stream.close();
    }
}
