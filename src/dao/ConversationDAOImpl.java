package dao;



import model.Conversation;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 对话DAO的JDBC实现
 * @author jonysingl
 * @version 1.0
 * @since 2025-04-07
 */
public class ConversationDAOImpl implements ConversationDAO {

    private static final Logger LOGGER = Logger.getLogger(ConversationDAOImpl.class.getName());

    private static final String INSERT_CONVERSATION =
            "INSERT INTO conversations (user_id, title, model_type, last_message_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String SELECT_BY_ID =
            "SELECT * FROM conversations WHERE conversation_id = ?";

    private static final String SELECT_BY_USER_ID =
            "SELECT * FROM conversations WHERE user_id = ? ORDER BY last_message_at DESC";

    private static final String SELECT_BY_USER_ID_AND_MODEL_TYPE =
            "SELECT * FROM conversations WHERE user_id = ? AND model_type = ? ORDER BY last_message_at DESC";

    private static final String UPDATE_TITLE =
            "UPDATE conversations SET title = ?, updated_at = CURRENT_TIMESTAMP WHERE conversation_id = ?";

    private static final String UPDATE_LAST_MESSAGE_TIME =
            "UPDATE conversations SET last_message_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE conversation_id = ?";

    private static final String DELETE_CONVERSATION =
            "DELETE FROM conversations WHERE conversation_id = ?";

    private static final String DELETE_ALL_BY_USER_ID =
            "DELETE FROM conversations WHERE user_id = ?";

    private static final String COUNT_BY_USER_ID =
            "SELECT COUNT(*) FROM conversations WHERE user_id = ?";

    private static final String CHECK_BELONGS_TO_USER =
            "SELECT COUNT(*) FROM conversations WHERE conversation_id = ? AND user_id = ?";

    @Override
    public long create(Conversation conversation) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(INSERT_CONVERSATION, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, conversation.getUserId());
            ps.setString(2, conversation.getTitle());
            ps.setString(3, conversation.getModelType());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("创建对话失败，没有影响任何行");
            }

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                conversation.setConversationId(rs.getLong(1));
                return conversation.getConversationId();
            } else {
                throw new SQLException("创建对话失败，未获取到ID");
            }
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public Optional<Conversation> findById(long conversationId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_BY_ID);
            ps.setLong(1, conversationId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToConversation(rs));
            } else {
                return Optional.empty();
            }
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public List<Conversation> findByUserId(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Conversation> conversations = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_BY_USER_ID);
            ps.setInt(1, userId);

            rs = ps.executeQuery();
            while (rs.next()) {
                conversations.add(mapResultSetToConversation(rs));
            }
            return conversations;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public List<Conversation> findByUserIdAndModelType(int userId, String modelType) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Conversation> conversations = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(SELECT_BY_USER_ID_AND_MODEL_TYPE);
            ps.setInt(1, userId);
            ps.setString(2, modelType);

            rs = ps.executeQuery();
            while (rs.next()) {
                conversations.add(mapResultSetToConversation(rs));
            }
            return conversations;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    @Override
    public boolean updateTitle(long conversationId, String title) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(UPDATE_TITLE);
            ps.setString(1, title);
            ps.setLong(2, conversationId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public boolean updateLastMessageTime(long conversationId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(UPDATE_LAST_MESSAGE_TIME);
            ps.setLong(1, conversationId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public boolean delete(long conversationId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(DELETE_CONVERSATION);
            ps.setLong(1, conversationId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public int deleteAllByUserId(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(DELETE_ALL_BY_USER_ID);
            ps.setInt(1, userId);

            return ps.executeUpdate();
        } finally {
            DatabaseUtil.closeResources(null, ps);
        }
    }

    @Override
    public int countByUserId(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(COUNT_BY_USER_ID);
            ps.setInt(1, userId);

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
    public boolean belongsToUser(long conversationId, int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(CHECK_BELONGS_TO_USER);
            ps.setLong(1, conversationId);
            ps.setInt(2, userId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            DatabaseUtil.closeResources(rs, ps);
        }
    }

    /**
     * 将ResultSet映射到Conversation对象
     * @param rs ResultSet对象
     * @return Conversation对象
     * @throws SQLException 数据库操作异常
     */
    private Conversation mapResultSetToConversation(ResultSet rs) throws SQLException {
        Conversation conversation = new Conversation();
        conversation.setConversationId(rs.getLong("conversation_id"));
        conversation.setUserId(rs.getInt("user_id"));
        conversation.setTitle(rs.getString("title"));
        conversation.setModelType(rs.getString("model_type"));
        conversation.setCreatedAt(rs.getTimestamp("created_at"));
        conversation.setUpdatedAt(rs.getTimestamp("updated_at"));
        conversation.setLastMessageAt(rs.getTimestamp("last_message_at"));
        return conversation;
    }
}