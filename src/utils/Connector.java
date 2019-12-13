package utils;

import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 数据库连接工具
 *
 * @author Drogon1573
 */
public class Connector implements Serializable {
    private static final long serialVersionUID = -5284530935717575965L;
    private Connection connection = null;

    public Connector() {
        try {
            Properties properties = new Properties();
            // 载入配置文件
            properties.loadFromXML(Objects.requireNonNull(this
                .getClass()
                .getClassLoader()
                .getResourceAsStream("configurations.xml")));
            // 加载数据库驱动器
            Class.forName(properties.getProperty("driver"));
            // 生成连接字符串
            final String
                url =
                properties.getProperty("type")
                + ":"
                + properties.getProperty("server")
                + "://"
                + properties.getProperty("host")
                + "\\"
                + properties.getProperty("instance")
                + ":"
                + properties.getProperty("port")
                + ";DatabaseName="
                + properties.getProperty("database")
                + ";";
            // 尝试连接
            connection = DriverManager.getConnection(
                url,
                properties.getProperty("username"),
                properties.getProperty("password")
            );
        } catch (SQLException | ClassNotFoundException | IOException exception) {
            switch (exception.getClass().getTypeName()) {
                case "SQLException":
                    System.err.println("[Error] 数据库连接异常！");
                    break;
                case "ClassNotFoundException":
                    System.err.println("[Error] 未找到数据库驱动器！");
                    break;
                case "IOException":
                    System.err.println("[Error] 配置文件读取错误！");
                    break;
                default:
                    System.err.println("[Error] 未知错误！");
            }
            exception.printStackTrace();
        }
    }

    /**
     * 登录认证
     *
     * @param username
     *     用户名
     *
     * @return 受影响的行数
     */
    public ResultSet login(String username) {
        ResultSet resultSet = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT [Password] FROM [dbo].[Users] WHERE ([Username] = ? OR [Phone] "
                + "= ? OR [Email] = ?)");
            statement.setNString(1, username);
            statement.setNString(2, username);
            statement.setNString(3, username);
            resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            System.err.println("错误：SQL语句异常！");
            exception.printStackTrace();
        }
        return resultSet;
    }

    /**
     * 用户唯一性校验
     *
     * @param username
     *     用户输入的注册用户名
     *
     * @return 受影响的行数
     */
    public ResultSet uniqueCheck(String username) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) AS [Rows] FROM [dbo].[Users] WHERE [Username] = ?");
            statement.setNString(1, username);
            return statement.executeQuery();
        } catch (SQLException exception) {
            System.err.println("错误：SQL语句异常！");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * 用户注册
     *
     * @param profile
     *     用户档案
     *
     * @return 受影响的行数
     */
    public int signUp(HashMap<String, String> profile) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO [dbo].[Users] VALUES (?, ?, ?, ?, ?, ?)");
            statement.setNString(1, profile.get("Username"));
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
                statement.setNString(5, profile.get("Question"));
            }
            if ("".equals(profile.get("Answer"))) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setNString(6, profile.get("Answer"));
            }
            final int affected = statement.executeUpdate();
            statement.close();
            return affected;
        } catch (SQLException exception) {
            System.err.println("错误：SQL语句异常！");
            exception.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取密保问题
     *
     * @param username
     *     用户名
     *
     * @return 密保问题
     */
    public String getQuestion(String username) {
        String question = null;
        try {
            // 执行SQL语句
            PreparedStatement statement = connection.prepareStatement(
                "SELECT [Question] FROM [dbo].[Users] WHERE [Username] = ?");
            statement.setNString(1, username);
            ResultSet resultSet = statement.executeQuery();
            // 提取密保问题
            if (resultSet != null && resultSet.next()) {
                question = resultSet.getNString("Question");
            }
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return question;
    }

    /**
     * 获取用户评论
     *
     * @param username
     *     用户名
     *
     * @return 用户评论
     */
    public ResultSet fetchComments(String username) {
        final String anonymous = "Anonymous";
        ResultSet resultSet = null;
        try {
            if (!anonymous.equals(username)) {
                PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM [Comments] WHERE [Sender] = ?");
                statement.setNString(1, username);
                resultSet = statement.executeQuery();
            } else {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM [Comments]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
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
    public boolean resetVerify(String username, String answer) {
        boolean isPassed = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) AS [Flag] FROM [Users] WHERE [Username] = ? AND "
                + "[Answer] = ?");
            statement.setNString(1, username);
            statement.setNString(2, answer);
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
     * 密码重置
     *
     * @param password
     *     密码
     */
    public void reset(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE [Users] SET [Password] = ? WHERE [Username] = ?");
            statement.setString(1, Md5Util.encrypt(password));
            statement.setNString(2, username);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public boolean sendComments(
        final String username, final String title, final String comments
    ) {
        // 数据存入标记
        boolean isSaved = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO [Comments] VALUES (?, ?, ?, SYSDATETIME())");
            statement.setNString(1, username);
            statement.setNString(2, title);
            statement.setNString(3, comments);
            isSaved = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSaved;
    }

    /**
     * 检索用户仓库
     *
     * @param username
     *     用户名
     *
     * @return 结果集
     */
    public ResultSet fetchRepositories(String username) {
        final String[] anonymous = {"佛大云服务", "Anonymous"};
        ResultSet resultSet = null;
        PreparedStatement statement;

        try {
            if (Arrays.asList(anonymous).contains(username)) {
                // 匿名用户
                statement = connection.prepareStatement(
                    "SELECT [Username], [Repository] FROM [Repositories] GROUP BY "
                    + "[Username], [Repository]");
            } else {
                // 登录用户
                statement = connection.prepareStatement(
                    "SELECT [Username], [Repository] FROM [Repositories] WHERE "
                    + "[Username] = ? GROUP BY [Username], [Repository]");
                statement.setNString(1, username);
            }
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
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
    public boolean deleteRepositories(String username, String repository) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM [Repositories] WHERE [Username] = ? AND [Repository] = ?");
            statement.setNString(1, username);
            statement.setNString(2, repository);
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
    public boolean changeUsername(String news, String old) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE [Users] SET [Username] = ? WHERE [Username] = ?");
            statement.setNString(1, news);
            statement.setNString(2, old);
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
    public boolean changePassword(String username, String news, String old) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE [Users] SET [Password] = ? WHERE ([Username] = ? AND [Password]"
                + " = ?)");
            statement.setString(1, news);
            statement.setNString(2, username);
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
    public boolean changePhone(String username, String news) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE [Users] SET [Phone] = ? WHERE [Username] = ?");
            statement.setString(1, news);
            statement.setNString(2, username);
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
    public boolean changeProtection(String username, String question, String answer) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE [Users] SET [Question] = ?, [Answer] = ? WHERE [Username] = ?");
            statement.setNString(1, question);
            statement.setNString(2, answer);
            statement.setNString(3, username);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
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
    public boolean changeEmail(String username, String mail) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE [Users] SET [Email] = ? WHERE [Username] = ?");
            statement.setString(1, mail);
            statement.setNString(2, username);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
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
    public boolean renameRepositories(String username, String newName, String oldName) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE [Repositories] SET [Repository] = ? WHERE ([Username] = ? AND "
                + "[Repository] = ?)");
            statement.setNString(1, newName);
            statement.setNString(2, username);
            statement.setNString(3, oldName);
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
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
    public void uploadFiles(
        final String username,
        final String repoName,
        final String path,
        final String fileName,
        byte[] details,
        boolean isDir
    ) {
        if (details == null) {
            // 没有内容的目录/空文件将由其他4项内容生成MD5校验值
            details = (username + repoName + path + fileName).getBytes();
        }
        try {
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO [dbo].[Repositories] VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setNString(1, username);
            statement.setNString(2, repoName);
            statement.setNString(3, path);
            statement.setNString(4, fileName);
            statement.setString(5, Md5Util.encrypt(details));
            statement.setBytes(6, details);
            statement.setBoolean(7, isDir);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public JSONArray listFiles(
        final String username, final String repository, final String path
    ) {
        JSONArray folders = new JSONArray();
        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT [Filename],[Folder] "
                + "FROM [dbo].[Repositories] "
                + "WHERE [Username] = ? "
                + "AND [Repository] = ? "
                + "AND [Path] = ? "
                + "GROUP BY [Filename],[Folder]");
            statement.setNString(1, username);
            statement.setNString(2, repository);
            statement.setNString(3, path);
            ResultSet set = statement.executeQuery();
            while (set != null && set.next()) {
                JSONObject file = new JSONObject();
                file.put("Name", set.getNString("Filename"));
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
    public byte[] getFiles(
        final String username, final String repository, final String filename
    ) {
        byte[] content = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT [Details] FROM [dbo].[Repositories] WHERE [Username] = ? AND "
                + "[Repository] = ? AND [Filename] = ?");
            statement.setNString(1, username);
            statement.setNString(2, repository);
            statement.setNString(3, filename);
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
     * 递归删除仓库目录
     */
    public boolean deleteFolder(
        final String username, final String repository, final String path
    ) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM [dbo].[Repositories] "
                + "WHERE [Username] = ? "
                + "AND [Repository] = ? "
                + "AND [Filename] LIKE ?");
            statement.setNString(1, username);
            statement.setNString(2, repository);
            statement.setNString(3, path + "%");
            success = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] 目录递归删除失败！");
            e.printStackTrace();
        }
        return success;
    }

    /**
     * @param timeStamp
     *     字符串时间戳
     *
     * @return 以JSON封装的评论详情
     */
    public JSONObject getComments(final String timeStamp) {
        JSONObject object = new JSONObject();
        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM [dbo].[Comments] WHERE [DateTime] = ?");
            statement.setString(1, timeStamp);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                object.put("Sender", resultSet.getNString("Sender"));
                object.put("Title", resultSet.getNString("Title"));
                object.put("Details", resultSet.getNString("Details"));
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] 数据库请求异常！");
            e.printStackTrace();
        }
        return object;
    }

    /**
     * @param timestamp
     *     字符串时间戳
     *
     * @return 记录是否已被删除
     */
    public boolean deleteComments(final String timestamp) {
        boolean isDeleted = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM [dbo].[Comments] WHERE [DateTime] = ?");
            statement.setString(1, timestamp);
            isDeleted = (statement.executeUpdate() > 0);
            statement.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] 记录删除失败！");
            e.printStackTrace();
        }
        return isDeleted;
    }
}
