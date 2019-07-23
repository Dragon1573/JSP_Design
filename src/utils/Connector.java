package utils;

import java.io.Serializable;
import java.sql.*;
import java.util.HashMap;

/**
 * 数据库连接工具
 *
 * @author Drogon1573
 * @date 2019/07/01
 */
public class Connector implements Serializable {
    private static final long serialVersionUID = -5284530935717575965L;
    private Connection connection = null;

    /**
     * 初始化数据库连接
     */
    public Connector() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=Local_Git;", "sa", "123456");
        } catch (ClassNotFoundException exception) {
            System.err.println("错误：驱动未找到！");
            exception.printStackTrace();
        } catch (SQLException exception) {
            System.err.println("错误：无法连接到数据库！");
            exception.printStackTrace();
        }
    }

    /**
     * 登录认证
     *
     * @param username 用户名
     * @return 受影响的行数
     */
    public ResultSet login(String username) {
        ResultSet resultSet = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT [Password] FROM [dbo].[Users] WHERE ([Username] = ? OR [Phone] = ? OR [Email] = ?)");
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
     * 用户唯一性校验
     *
     * @param username 用户输入的注册用户名
     * @return 受影响的行数
     */
    public ResultSet uniqueCheck(String username) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT COUNT(*) AS [Rows] FROM [dbo].[Users] WHERE [Username] = ?");
            statement.setString(1, username);
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
     * @param profile 用户档案
     * @return 受影响的行数
     */
    public int signUp(HashMap<String, String> profile) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO [dbo].[Users] VALUES (?, ?, ?, ?, ?, ?)");
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

            return statement.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("错误：SQL语句异常！");
            exception.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取密保问题
     *
     * @param username 用户名
     * @return 密保问题
     */
    public String getQuestion(String username) {
        String question = null;
        try {
            // 执行SQL语句
            PreparedStatement statement = connection.prepareStatement("SELECT [Question] FROM [dbo].[Users] WHERE [Username] = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // 提取密保问题
            if (resultSet != null && resultSet.next()) {
                question = resultSet.getString("Question");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return question;
    }

    /**
     * 获取用户评论
     *
     * @param username 用户名
     * @return 用户评论
     */
    public ResultSet fetchComments(String username) {
        final String anonymous = "佛大云服务";

        ResultSet resultSet = null;
        try {
            if (!anonymous.equals(username)) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM [Comments] WHERE [Sender] = ?");
                statement.setString(1, username);
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
     * @param username 用户名
     * @param answer   密保答案
     * @return 校验标记
     */
    public boolean resetVerify(String username, String answer) {
        boolean isPassed = false;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS [Flag] FROM [Users] WHERE [Username] = ? AND [Answer] = ?");
            statement.setString(1, username);
            statement.setString(2, answer);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet != null && resultSet.next()) {
                isPassed = (resultSet.getInt("Flag") > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isPassed;
    }

    /**
     * 密码重置
     *
     * @param password 密码
     */
    public void reset(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE [Users] SET [Password] = ? WHERE [Username] = ?");
            statement.setString(1, Md5Util.encrypt(password));
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送评论
     *
     * @param username 用户名
     * @param comments 评论内容
     * @return boolean
     */
    public boolean sendComments(String username, String comments) {
        // 数据存入标记
        boolean isSaved = false;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO [Comments] VALUES (?, ?, SYSDATETIME())");
            statement.setString(1, username);
            statement.setString(2, comments);
            isSaved = (statement.executeUpdate() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSaved;
    }

    /**
     * 检索用户仓库
     *
     * @param username 用户名
     * @return 结果集
     */
    public ResultSet fetchRepositories(String username) {
        final String anonymous = "佛大云服务";
        ResultSet resultSet = null;
        PreparedStatement statement;

        try {
            if (anonymous.equals(username)) {
                // 匿名用户
                statement = connection.prepareStatement("SELECT [Username],[Repository] FROM [Repositories] GROUP BY [Username], [Repository]");
                resultSet = statement.executeQuery();
            } else {
                // 登录用户
                statement = connection.prepareStatement("SELECT [Username], [Repository] FROM [Repositories] WHERE [Username] = ? GROUP BY [Username], [Repository]");
                statement.setString(1, username);
                resultSet = statement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * 修改用户名
     *
     * @param news 新用户名
     * @param old  旧用户名
     * @return 修改成功
     */
    public boolean changeUsername(String news, String old) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE [Users] SET [Username] = ? WHERE [Username] = ?");
            statement.setString(1, news);
            statement.setString(2, old);
            success = (statement.executeUpdate() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 修改用户密码
     *
     * @param username 用户名
     * @param news     新密码（MD5）
     * @param old      旧密码（MD5）
     * @return 修改成功
     */
    public boolean changePassword(String username, String news, String old) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE [Users] SET [Password] = ? WHERE ([Username] = ? AND [Password] = ?)");
            statement.setString(1, news);
            statement.setString(2, username);
            statement.setString(3, old);
            success = (statement.executeUpdate() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 修改联系方式
     *
     * @param username 用户名
     * @param news     新联系方式
     * @return 修改成功
     */
    public boolean changePhone(String username, String news) {
        boolean success = false;
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE [Users] SET [Phone] = ? WHERE [Username] = ?");
            statement.setString(1, news);
            statement.setString(2, username);
            success = (statement.executeUpdate() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
}
