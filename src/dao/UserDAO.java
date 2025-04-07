package dao;


import model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问对象接口
 * @author jonysingl
 * @version 1.0
 * @since 2025-04-07
 */
public interface UserDAO {

    /**
     * 创建新用户
     * @param user 用户对象
     * @return 创建的用户ID
     * @throws SQLException 数据库操作异常
     */
    int create(User user) throws SQLException;

    /**
     * 根据ID查找用户
     * @param userId 用户ID
     * @return 包含用户的Optional，如果未找到则为empty
     * @throws SQLException 数据库操作异常
     */
    Optional<User> findById(int userId) throws SQLException;

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 包含用户的Optional，如果未找到则为empty
     * @throws SQLException 数据库操作异常
     */
    Optional<User> findByUsername(String username) throws SQLException;

    /**
     * 根据邮箱查找用户
     * @param email 邮箱地址
     * @return 包含用户的Optional，如果未找到则为empty
     * @throws SQLException 数据库操作异常
     */
    Optional<User> findByEmail(String email) throws SQLException;

    /**
     * 根据密码重置令牌查找用户
     * @param resetToken 密码重置令牌
     * @return 包含用户的Optional，如果未找到则为empty
     * @throws SQLException 数据库操作异常
     */
    Optional<User> findByResetToken(String resetToken) throws SQLException;

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否更新成功
     * @throws SQLException 数据库操作异常
     */
    boolean update(User user) throws SQLException;

    /**
     * 更新用户最后登录时间
     * @param userId 用户ID
     * @return 是否更新成功
     * @throws SQLException 数据库操作异常
     */
    boolean updateLastLogin(int userId) throws SQLException;

    /**
     * 更新用户密码
     * @param userId 用户ID
     * @param newPassword 新密码（已哈希）
     * @return 是否更新成功
     * @throws SQLException 数据库操作异常
     */
    boolean updatePassword(int userId, String newPassword) throws SQLException;

    /**
     * 设置密码重置令牌
     * @param userId 用户ID
     * @param resetToken 重置令牌
     * @param expiryTime 令牌过期时间
     * @return 是否设置成功
     * @throws SQLException 数据库操作异常
     */
    boolean setResetToken(int userId, String resetToken, java.sql.Timestamp expiryTime) throws SQLException;

    /**
     * 清除密码重置令牌
     * @param userId 用户ID
     * @return 是否清除成功
     * @throws SQLException 数据库操作异常
     */
    boolean clearResetToken(int userId) throws SQLException;

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 是否删除成功
     * @throws SQLException 数据库操作异常
     */
    boolean delete(int userId) throws SQLException;

    /**
     * 获取所有用户
     * @return 用户列表
     * @throws SQLException 数据库操作异常
     */
    List<User> findAll() throws SQLException;

    /**
     * 检查用户名是否可用
     * @param username 用户名
     * @return 如果用户名可用则为true，否则为false
     * @throws SQLException 数据库操作异常
     */
    boolean isUsernameAvailable(String username) throws SQLException;

    /**
     * 检查邮箱是否可用
     * @param email 邮箱
     * @return 如果邮箱可用则为true，否则为false
     * @throws SQLException 数据库操作异常
     */
    boolean isEmailAvailable(String email) throws SQLException;
}