package dao;



import model.Message;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 消息DAO的JDBC实现
 * @author jonysingl
 * @version 1.0
 * @since 2025-04-07
 */
public class MessageDAOImpl implements MessageDAO {

    private static final Logger LOGGER = Logger.getLogger(MessageDAOImpl.class.getName());

    private static final String INSERT_MESSAGE =
            "INSERT INTO messages (conversation_id, is_from_user, content, tokens_used) VALUES (?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT * FROM messages WHERE message_id = ?";

    private static final String SELECT_BY_CONVERSATION_ID =
            "SELECT * FROM messages WHERE conversation_id = ? ORDER BY created_at ASC";

    private static final String SELECT_RECENT_BY_CONVERSATION_ID =
            "SELECT * FROM messages WHERE conversation_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";

    private static final String UPDATE_CONTENT =
            "UPDATE messages SET content = ? WHERE message_id = ?";

    private static final String UPDATE_TOKENS_USED =
            "UPDATE messages SET tokens_used = ? WHERE message_id = ?";

    private static final String DELETE_MESSAGE =
            "DELETE FROM messages WHERE message_id = ?";

    private static final String DELETE_BY_CONVERSATION_ID =
            "DELETE FROM messages WHERE conversation_id = ?";

    private static final String COUNT_BY_CONVERSATION_ID =
            "SELECT COUNT(*) FROM messages WHERE conversation_id = ?";

    private static final String SUM_TOKENS_BY_USER =
            "SELECT SUM(m.tokens_used) FROM messages m " +
                    "JOIN conversations c ON m.conversation_id = c.conversation_id " +
                    "WHERE c.user_id = ?";

    @Override
    public long create(Message message) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(INSERT_MESSAGE, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, message.getConversationId());
            ps.setBoolean(2, message.isFromUser());
            ps.setString(3, message.getContent());
            ps.setInt(4, message.getTokensUsed() != null ? message.getTokensUsed() : 0);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("创建消息失败，没有影响任何行");
            }

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                message.setMessageId(rs.getLong(1));

                // 更新对话的最后消息时间
                updateConversationLastMessageTime(conn, message.getConversationId());

                return message.getMessageId();
            } else {
                throw new SQLException("创建消息失败，未获取到ID");
            }
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public Optional<Message> findById(long messageId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_BY_ID);
            ps.setLong(1, messageId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToMessage(rs));
            } else {
                return Optional.empty();
            }
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public List<Message> findByConversationId(long conversationId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_BY_CONVERSATION_ID);
            ps.setLong(1, conversationId);

            rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            return messages;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public List<Message> findRecentByConversationId(long conversationId, int limit, int offset) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_RECENT_BY_CONVERSATION_ID);
            ps.setLong(1, conversationId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            return messages;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public boolean updateContent(long messageId, String content) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(UPDATE_CONTENT);
            ps.setString(1, content);
            ps.setLong(2, messageId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public boolean updateTokensUsed(long messageId, int tokensUsed) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(UPDATE_TOKENS_USED);
            ps.setInt(1, tokensUsed);
            ps.setLong(2, messageId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public boolean delete(long messageId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(DELETE_MESSAGE);
            ps.setLong(1, messageId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public int deleteByConversationId(long conversationId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(DELETE_BY_CONVERSATION_ID);
            ps.setLong(1, conversationId);

            return ps.executeUpdate();
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public int countByConversationId(long conversationId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(COUNT_BY_CONVERSATION_ID);
            ps.setLong(1, conversationId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public long getTotalTokensUsedByUser(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SUM_TOKENS_BY_USER);
            ps.setInt(1, userId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    /**
     * 更新对话的最后消息时间
     * @param conn 数据库连接
     * @param conversationId 对话ID
     * @throws SQLException 数据库操作异常
     */
    private void updateConversationLastMessageTime(Connection conn, long conversationId) throws SQLException {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("UPDATE conversations SET last_message_at = CURRENT_TIMESTAMP, " +
                    "updated_at = CURRENT_TIMESTAMP WHERE conversation_id = ?");
            ps.setLong(1, conversationId);
            ps.executeUpdate();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "关闭PreparedStatement失败", e);
                }
            }
        }
    }

    /**
     * 将ResultSet映射到Message对象
     * @param rs ResultSet对象
     * @return Message对象
     * @throws SQLException 数据库操作异常
     */
    private Message mapResultSetToMessage(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setMessageId(rs.getLong("message_id"));
        message.setConversationId(rs.getLong("conversation_id"));
        message.setFromUser(rs.getBoolean("is_from_user"));
        message.setContent(rs.getString("content"));
        message.setTokensUsed(rs.getInt("tokens_used"));
        message.setCreatedAt(rs.getTimestamp("created_at"));
        return message;
    }
}