package utils;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 数据库连接工具
 *
 * @author Drogon1573
 */
public class Connector implements Serializable {
    private static final long serialVersionUID = -5284530935717575965L;
    private static DataSource source = null;

    static {
        try {
            Context context = (Context)(new InitialContext().lookup("java:comp/env"));
            // 查找数据源
            source = (DataSource)context.lookup("jdbc/sqlite3");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改电子邮箱
     *
     * @param username
     *     用户名
     * @param mail
     *     电子邮箱
     *
     * @return 修改成功
     */
    public static boolean changeEmail(String username, String mail) {
        boolean success = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "UPDATE Users SET Email = ? WHERE Username = ?"
            );
            statement.setString(1, mail);
            statement.setString(2, username);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 修改用户密码
     *
     * @param username
     *     用户名
     * @param news
     *     新密码（MD5）
     * @param old
     *     旧密码（MD5）
     *
     * @return 修改成功
     */
    public static boolean changePassword(String username, String news, String old) {
        boolean success = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "UPDATE Users SET Password = ? WHERE (Username = ? AND Password = ?)"
            );
            statement.setString(1, news);
            statement.setString(2, username);
            statement.setString(3, old);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 修改联系方式
     *
     * @param username
     *     用户名
     * @param news
     *     新联系方式
     *
     * @return 修改成功
     */
    public static boolean changePhone(String username, String news) {
        boolean success = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "UPDATE Users SET Phone = ? WHERE Username = ?"
            );
            statement.setString(1, news);
            statement.setString(2, username);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 修改密保
     *
     * @param username
     *     用户名
     * @param question
     *     新问题
     * @param answer
     *     新答案
     *
     * @return 修改成功
     */
    public static boolean changeProtection(String username, String question, String answer) {
        boolean success = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "UPDATE Users SET Question = ?, Answer = ? WHERE Username = ?"
            );
            statement.setString(1, question);
            statement.setString(2, answer);
            statement.setString(3, username);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 修改用户名
     *
     * @param news
     *     新用户名
     * @param old
     *     旧用户名
     *
     * @return 修改成功
     */
    public static boolean changeUsername(String news, String old) {
        boolean success = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "UPDATE Users SET Username = ? WHERE Username = ?"
            );
            statement.setString(1, news);
            statement.setString(2, old);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * @param timestamp
     *     字符串时间戳
     *
     * @return 记录是否已被删除
     */
    public static boolean deleteComments(String timestamp) {
        boolean isDeleted = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "DELETE FROM Comments WHERE DateTime = ?"
            );
            statement.setString(1, timestamp);
            isDeleted = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] 记录删除失败！");
            e.printStackTrace();
        }
        return isDeleted;
    }

    /**
     * 递归删除仓库目录
     */
    public static boolean deleteFolder(
        String username, String repository, String path
    ) {
        boolean success = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "DELETE FROM Repositories WHERE Username = ? AND Repository = ? AND Filename LIKE ?"
            );
            statement.setString(1, username);
            statement.setString(2, repository);
            statement.setString(3, path + "%");
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] 目录递归删除失败！");
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 删除仓库
     *
     * @param username
     *     用户名
     * @param repository
     *     仓库名
     *
     * @return 删除成功
     */
    public static boolean deleteRepositories(String username, String repository) {
        boolean success = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "DELETE FROM Repositories WHERE Username = ? AND Repository = ?"
            );
            statement.setString(1, username);
            statement.setString(2, repository);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 获取用户评论
     *
     * @param username
     *     用户名
     *
     * @return 用户评论
     */
    public static ResultSet fetchComments(String username) {
        final String anonymous = "Anonymous";
        ResultSet resultSet = null;
        try {
            Connection connection = getConnection();
            if (!anonymous.equals(username)) {
                PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                    "SELECT * FROM Comments WHERE Sender = ?"
                );
                statement.setString(1, username);
                resultSet = statement.executeQuery();
            } else {
                Statement statement = Objects.requireNonNull(connection).createStatement();
                resultSet = statement.executeQuery("SELECT * FROM Comments");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * 检索用户仓库
     *
     * @param username
     *     用户名
     *
     * @return 结果集
     */
    public static ResultSet fetchRepositories(String username) {
        String[] anonymous = {"佛大云服务", "Anonymous"};
        ResultSet resultSet = null;
        PreparedStatement statement;

        try {
            Connection connection = getConnection();
            if (Arrays.asList(anonymous).contains(username)) {
                // 匿名用户
                statement = Objects.requireNonNull(connection).prepareStatement(
                    "SELECT Username, Repository FROM Repositories GROUP BY Username, Repository"
                );
            } else {
                // 登录用户
                statement = Objects.requireNonNull(connection).prepareStatement(
                    "SELECT Username, Repository FROM Repositories "
                    + "WHERE Username = ? GROUP BY Username, Repository"
                );
                statement.setString(1, username);
            }
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * @param timeStamp
     *     字符串时间戳
     *
     * @return 以JSON封装的评论详情
     */
    public static JSONObject getComments(String timeStamp) {
        JSONObject object = new JSONObject();
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "SELECT * FROM Comments WHERE DateTime = ?"
            );
            statement.setString(1, timeStamp);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                object.put("Sender", resultSet.getString("Sender"));
                object.put("Title", resultSet.getString("Title"));
                object.put("Details", resultSet.getString("Details"));
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] 数据库请求异常！");
            e.printStackTrace();
        }
        return object;
    }

    private static Connection getConnection() {
        try {
            // 初始化上下文
            return source.getConnection();
        } catch (SQLException exception) {
            switch (exception.getClass().getTypeName()) {
                case "SQLException" -> System.err.println("[Error] 数据库连接异常！");
                case "ClassNotFoundException" -> System.err.println("[Error] 未找到数据库驱动器！");
                case "IOException" -> System.err.println("[Error] 配置文件读取错误！");
                default -> System.err.println("[Error] 未知错误！");
            }
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件
     *
     * @param username
     *     用户名
     * @param repository
     *     仓库名
     * @param filename
     *     文件名
     *
     * @return 二进制文件内容
     */
    public static byte[] getFiles(
        String username, String repository, String filename
    ) {
        byte[] content = null;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "SELECT Details FROM Repositories WHERE Username = ? AND Repository = ? AND Filename = ?"
            );
            statement.setString(1, username);
            statement.setString(2, repository);
            statement.setString(3, filename);
            ResultSet set = statement.executeQuery();
            while (set != null && set.next()) {
                content = set.getBytes("Details");
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("[Error] 文件读取异常！");
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 获取密保问题
     *
     * @param username
     *     用户名
     *
     * @return 密保问题
     */
    public static String getQuestion(String username) {
        String question = null;
        try {
            // 执行SQL语句
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "SELECT Question FROM Users WHERE Username = ?"
            );
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            // 提取密保问题
            if (resultSet != null && resultSet.next()) {
                question = resultSet.getString("Question");
            }
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return question;
    }

    /**
     * 获取仓库中的文件列表
     *
     * @param username
     *     用户名
     * @param repository
     *     仓库名
     * @param path
     *     相对路径名
     */
    public static JSONArray listFiles(
        String username, String repository, String path
    ) {
        JSONArray folders = new JSONArray();
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "SELECT Filename,Folder FROM Repositories "
                + "WHERE Username = ? AND Repository = ? AND Path = ? "
                + "GROUP BY Filename,Folder");
            statement.setString(1, username);
            statement.setString(2, repository);
            statement.setString(3, path);
            ResultSet set = statement.executeQuery();
            while (set != null && set.next()) {
                JSONObject file = new JSONObject();
                file.put("Name", set.getString("Filename"));
                file.put("isDir", set.getBoolean("Folder"));
                folders.add(file);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("[Error] 拉取文件列表异常！");
            e.printStackTrace();
        }
        return folders;
    }

    /**
     * 登录认证
     *
     * @param username
     *     用户名
     *
     * @return 受影响的行数
     */
    public static ResultSet login(String username) {
        ResultSet resultSet = null;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "SELECT Password FROM Users WHERE (Username = ? OR Phone = ? OR Email = ?)"
            );
            statement.setString(1, username);
            statement.setString(2, username);
            statement.setString(3, username);
            resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            System.err.println("错误：SQL语句异常！");
            exception.printStackTrace();
        }
        return resultSet;
    }

    /**
     * 重命名仓库
     *
     * @param username
     *     用户名
     * @param newName
     *     仓库名
     *
     * @return 操作成功
     */
    public static boolean renameRepositories(String username, String newName, String oldName) {
        boolean success = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "UPDATE Repositories SET Repository = ? WHERE (Username = ? AND Repository = ?)"
            );
            statement.setString(1, newName);
            statement.setString(2, username);
            statement.setString(3, oldName);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 密码重置
     *
     * @param password
     *     密码
     */
    public static void reset(String username, String password) {
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "UPDATE Users SET Password = ? WHERE Username = ?"
            );
            statement.setString(1, Md5Util.encrypt(password));
            statement.setString(2, username);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 密码重置身份认证
     *
     * @param username
     *     用户名
     * @param answer
     *     密保答案
     *
     * @return 校验标记
     */
    public static boolean resetVerify(String username, String answer) {
        boolean isPassed = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "SELECT COUNT(*) AS Flag FROM Users WHERE Username = ? AND Answer = ?"
            );
            statement.setString(1, username);
            statement.setString(2, answer);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                isPassed = (resultSet.getInt("Flag") > 0);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isPassed;
    }

    /**
     * 发送评论
     *
     * @param username
     *     用户名
     * @param comments
     *     评论内容
     *
     * @return boolean
     */
    public static boolean sendComments(
        String username, String title, String comments
    ) {
        // 数据存入标记
        boolean isSaved = false;
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "INSERT INTO Comments VALUES (?, ?, ?, CURRENT_TIME)"
            );
            statement.setString(1, username);
            statement.setString(2, title);
            statement.setString(3, comments);
            isSaved = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSaved;
    }

    /**
     * 用户注册
     *
     * @param profile
     *     用户档案
     *
     * @return 受影响的行数
     */
    public static int signUp(HashMap<String, String> profile) {
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, profile.get("Username"));
            statement.setString(2, Md5Util.encrypt(profile.get("Password")));

            /*
              用户信息缺失处理
              将用户的空缺信息填充为 NULL
             */
            if ("".equals(profile.get("Phone"))) {
                statement.setNull(3, Types.CHAR);
            } else {
                statement.setString(3, profile.get("Phone"));
            }
            if ("".equals(profile.get("Email"))) {
                statement.setNull(4, Types.VARCHAR);
            } else {
                statement.setString(4, profile.get("Email"));
            }
            if ("".equals(profile.get("Question"))) {
                statement.setNull(5, Types.VARCHAR);
            } else {
                statement.setString(5, profile.get("Question"));
            }
            if ("".equals(profile.get("Answer"))) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setString(6, profile.get("Answer"));
            }
            int affected = statement.executeUpdate();
            statement.close();
            return affected;
        } catch (SQLException exception) {
            System.err.println("错误：SQL语句异常！");
            exception.printStackTrace();
            return 0;
        }
    }

    /**
     * 用户唯一性校验
     *
     * @param username
     *     用户输入的注册用户名
     *
     * @return 受影响的行数
     */
    public static ResultSet uniqueCheck(String username) {
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "SELECT COUNT(*) AS Rows FROM Users WHERE Username = ?"
            );
            statement.setString(1, username);
            return statement.executeQuery();
        } catch (SQLException exception) {
            System.err.println("错误：SQL语句异常！");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * 上传文件
     *
     * @param username
     *     用户名
     * @param repoName
     *     存储库名
     * @param path
     *     相对路径
     * @param fileName
     *     文件名
     * @param details
     *     二进制文件内容
     */
    public static void uploadFiles(
        String username, String repoName, String path, String fileName, byte[] details, boolean isDir
    ) {
        if (details == null) {
            // 没有内容的目录/空文件将由其他4项内容生成MD5校验值
            details = (username + repoName + path + fileName).getBytes();
        }
        try {
            PreparedStatement statement = Objects.requireNonNull(getConnection()).prepareStatement(
                "INSERT INTO Repositories VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, username);
            statement.setString(2, repoName);
            statement.setString(3, path);
            statement.setString(4, fileName);
            statement.setString(5, Md5Util.encrypt(details));
            statement.setBytes(6, details);
            statement.setBoolean(7, isDir);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
