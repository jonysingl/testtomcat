package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据库工具类，负责管理数据库连接和基本操作
 * @author jonysingl
 * @version 1.0
 * @since 2025-04-07
 */
public class DatabaseUtil {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    // 数据库连接信息 - 生产环境中应该从配置文件获取
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/chatsql?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    // 连接池相关参数
    private static final int MAX_POOL_SIZE = 10;
    private static final int TIMEOUT = 30000; // 30秒

    // 使用ThreadLocal存储当前线程的数据库连接，支持事务
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    static {
        try {
            // 注册JDBC驱动
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "无法加载JDBC驱动", e);
            throw new RuntimeException("无法加载JDBC驱动", e);
        }
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException 如果获取连接失败
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = connectionHolder.get();

        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            conn.setAutoCommit(true);
            connectionHolder.set(conn);
        }

        return conn;
    }

    /**
     * 开始事务
     * @throws SQLException 如果开始事务失败
     */
    public static void beginTransaction() throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
    }

    /**
     * 提交事务
     * @throws SQLException 如果提交事务失败
     */
    public static void commitTransaction() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn != null && !conn.getAutoCommit()) {
            conn.commit();
            conn.setAutoCommit(true);
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                if (!conn.getAutoCommit()) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "回滚事务失败", e);
            }
        }
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "关闭连接失败", e);
            } finally {
                connectionHolder.remove();
            }
        }
    }

    /**
     * 关闭资源
     * @param rs ResultSet对象
     * @param ps PreparedStatement对象
     */
    public static void closeResources(ResultSet rs, PreparedStatement ps) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "关闭ResultSet失败", e);
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "关闭PreparedStatement失败", e);
            }
        }
    }

    /**
     * 关闭所有资源，包括连接
     * @param rs ResultSet对象
     * @param ps PreparedStatement对象
     * @param conn Connection对象
     */
    public static void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) {
        closeResources(rs, ps);

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "关闭Connection失败", e);
            }
        }
    }
}