package servlets.hidden;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import entities.UserInfo;
import utils.Connector;

/**
 * @author Dragon1573
 */
@WebServlet(name = "Upload", urlPatterns = {"/upload"})
public class Upload extends HttpServlet {
    private JSONObject object = new JSONObject();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 清空JSONObject对象
        object.clear();
        // 设置编码格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        // 获取Session
        HttpSession session = request.getSession();
        // 获取用户认证对象
        UserInfo info = (UserInfo)session.getAttribute("certificate");
        // 获取用户名
        object.put("Username", info.getUsername());
        // 获取表单信息和文件
        loadFiles(request);
        // 将文件存入SQL
        saveToSql(object);
        // 定向文件
        response.sendRedirect("repositories.jsp");
    }

    /**
     * 载入文件
     *
     * @param request
     *     请求
     */
    private void loadFiles(HttpServletRequest request) {
        // 获取磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 创建上传引擎
        ServletFileUpload upload = new ServletFileUpload(factory);

        // 尝试上传
        try {
            List<FileItem> list = upload.parseRequest(request);
            for (FileItem item : list) {
                // 字段是字符串信息
                // 获取属性名
                String name = item.getFieldName();
                if (item.isFormField()) {
                    // 获取字段值并插入JSON对象中
                    if ("pathName".equals(name)) {
                        String[] path = item.getString().split("/");
                        object.put(name, path);
                    } else {
                        object.put(name, item.getString());
                    }
                } else {
                    // 字段是文件信息，提取路径
                    String value = item.getName();
                    // 截取文件名。实际上，表单上传的信息只有文件名而不包含路径
                    int begin = value.lastIndexOf(File.separator);
                    String fileName = value.substring(begin + 1);
                    object.put("Filename", fileName);
                    // 获取字段文件流
                    try (InputStream inputStream = item.getInputStream()) {
                        // 全文件直接读入（文件不超过4GB，直接读入内存即可）
                        byte[] content = inputStream.readAllBytes();
                        object.put("Content-Bytes", content);
                    }
                }
            }
        } catch (FileUploadException e) {
            System.err.println("[Error] 文件上传异常！");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("[Error] 文件流读写异常！");
            e.printStackTrace();
        }
    }

    /**
     * @param folder 文件夹名称
     *
     * @return 文件夹名称是否合法
     */
    private boolean isLegal(final String folder) {
        // 正常的文件夹，其名称不应为空
        return (folder != null && !"".equals(folder));
    }

    /**
     * 将文件写入到数据库中
     *
     * @param object 以JSON封装的表单对象
     */
    private void saveToSql(final JSONObject object) {
        try {
            // 获取 JDBC 对象
            Connector connector = new Connector();
            // 构建文件夹
            String[] path = object.getObject("pathName", String[].class);
            // 合法目录至少有2段内容以上
            if (path.length >= 2) {
                if (isLegal(path[0])) {
                    // 由于相对路径以 / 开始，分割时的首个元素是空串
                    // 此处的合法路径就非法了
                    System.err.println("[Error] 非法路径！");
                    throw new FileUploadException();
                }
                for (int i = 1; i < path.length; i++) {
                    if (!isLegal(path[i])) {
                        System.err.println("[Error] 非法路径！");
                        throw new FileUploadException();
                    }
                    connector.uploadFiles(
                        object.getString("Username"),
                        object.getString("repoName"),
                        path[i - 1],
                        path[i],
                        null
                    );
                }
            }
            // 上传文件
            connector.uploadFiles(
                object.getString("Username"),
                object.getString("repoName"),
                path[path.length - 1],
                object.getString("Filename"),
                object.getBytes("Content-Bytes")
            );
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }
}
