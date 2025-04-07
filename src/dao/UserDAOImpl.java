package dao;



import model.User;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用户DAO的JDBC实现
 * @author jonysingl
 * @version 1.0
 * @since 2025-04-07
 */
public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());

    private static final String INSERT_USER =
            "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT * FROM users WHERE user_id = ?";

    private static final String SELECT_BY_USERNAME =
            "SELECT * FROM users WHERE username = ?";

    private static final String SELECT_BY_EMAIL =
            "SELECT * FROM users WHERE email = ?";

    private static final String SELECT_BY_RESET_TOKEN =
            "SELECT * FROM users WHERE reset_token = ? AND reset_token_expires > NOW()";

    private static final String UPDATE_USER =
            "UPDATE users SET username = ?, email = ?, password = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";

    private static final String UPDATE_LAST_LOGIN =
            "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";

    private static final String UPDATE_PASSWORD =
            "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";

    private static final String SET_RESET_TOKEN =
            "UPDATE users SET reset_token = ?, reset_token_expires = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";

    private static final String CLEAR_RESET_TOKEN =
            "UPDATE users SET reset_token = NULL, reset_token_expires = NULL, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";

    private static final String DELETE_USER =
            "DELETE FROM users WHERE user_id = ?";

    private static final String SELECT_ALL =
            "SELECT * FROM users ORDER BY created_at DESC";

    private static final String CHECK_USERNAME =
            "SELECT COUNT(*) FROM users WHERE username = ?";

    private static final String CHECK_EMAIL =
            "SELECT COUNT(*) FROM users WHERE email = ?";

    @Override
    public int create(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("创建用户失败，没有影响任何行");
            }

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
                return user.getUserId();
            } else {
                throw new SQLException("创建用户失败，未获取到ID");
            }
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public Optional<User> findById(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_BY_ID);
            ps.setInt(1, userId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            } else {
                return Optional.empty();
            }
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_BY_USERNAME);
            ps.setString(1, username);

            rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            } else {
                return Optional.empty();
            }
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_BY_EMAIL);
            ps.setString(1, email);

            rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            } else {
                return Optional.empty();
            }
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public Optional<User> findByResetToken(String resetToken) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_BY_RESET_TOKEN);
            ps.setString(1, resetToken);

            rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            } else {
                return Optional.empty();
            }
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public boolean update(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(UPDATE_USER);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getUserId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public boolean updateLastLogin(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(UPDATE_LAST_LOGIN);
            ps.setInt(1, userId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(UPDATE_PASSWORD);
            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public boolean setResetToken(int userId, String resetToken, Timestamp expiryTime) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SET_RESET_TOKEN);
            ps.setString(1, resetToken);
            ps.setTimestamp(2, expiryTime);
            ps.setInt(3, userId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public boolean clearResetToken(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(CLEAR_RESET_TOKEN);
            ps.setInt(1, userId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public boolean delete(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(DELETE_USER);
            ps.setInt(1, userId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_ALL);

            rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public boolean isUsernameAvailable(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(CHECK_USERNAME);
            ps.setString(1, username);

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public boolean isEmailAvailable(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(CHECK_EMAIL);
            ps.setString(1, email);

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    /**
     * 将ResultSet映射到User对象
     * @param rs ResultSet对象
     * @return User对象
     * @throws SQLException 数据库操作异常
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setResetToken(rs.getString("reset_token"));
        user.setResetTokenExpires(rs.getTimestamp("reset_token_expires"));
        return user;
    }
}