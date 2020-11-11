package entities;

import java.io.Serializable;

/**
 * 存储用户信息，用于登陆
 *
 * @author Drogon1573
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 4982980163500402507L;
    private String username = "Anonymous";
    private boolean verified = true;

    /**
     * 设置用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 获取用户名
     *
     * @param username
     *     - 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取登陆状态
     *
     * @return 登陆状态
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * 设置登陆状态
     *
     * @param verified
     *     - 登陆状态
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
