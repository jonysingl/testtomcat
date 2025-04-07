package dao;


import model.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * 消息数据访问对象接口
 * @author jonysingl
 * @version 1.0
 * @since 2025-04-07
 */
public interface MessageDAO {

    /**
     * 创建新消息
     * @param message 消息对象
     * @return 创建的消息ID
     * @throws SQLException 数据库操作异常
     */
    long create(Message message) throws SQLException;

    /**
     * 根据ID查找消息
     * @param messageId 消息ID
     * @return 包含消息的Optional，如果未找到则为empty
     * @throws SQLException 数据库操作异常
     */
    Optional<Message> findById(long messageId) throws SQLException;

    /**
     * 根据对话ID获取该对话的所有消息，按时间正序排列
     * @param conversationId 对话ID
     * @return 消息列表
     * @throws SQLException 数据库操作异常
     */
    List<Message> findByConversationId(long conversationId) throws SQLException;

    /**
     * 根据对话ID获取该对话的最近消息，带分页
     * @param conversationId 对话ID
     * @param limit 限制条数
     * @param offset 偏移量
     * @return 消息列表
     * @throws SQLException 数据库操作异常
     */
    List<Message> findRecentByConversationId(long conversationId, int limit, int offset) throws SQLException;

    /**
     * 更新消息内容
     * @param messageId 消息ID
     * @param content 新内容
     * @return 是否更新成功
     * @throws SQLException 数据库操作异常
     */
    boolean updateContent(long messageId, String content) throws SQLException;

    /**
     * 更新消息token使用量
     * @param messageId 消息ID
     * @param tokensUsed 使用的tokens数量
     * @return 是否更新成功
     * @throws SQLException 数据库操作异常
     */
    boolean updateTokensUsed(long messageId, int tokensUsed) throws SQLException;

    /**
     * 删除消息
     * @param messageId 消息ID
     * @return 是否删除成功
     * @throws SQLException 数据库操作异常
     */
    boolean delete(long messageId) throws SQLException;

    /**
     * 删除对话的所有消息
     * @param conversationId 对话ID
     * @return 删除的消息数量
     * @throws SQLException 数据库操作异常
     */
    int deleteByConversationId(long conversationId) throws SQLException;

    /**
     * 获取对话中的消息数量
     * @param conversationId 对话ID
     * @return 消息数量
     * @throws SQLException 数据库操作异常
     */
    int countByConversationId(long conversationId) throws SQLException;

    /**
     * 获取用户的总token使用量
     * @param userId 用户ID
     * @return token使用量
     * @throws SQLException 数据库操作异常
     */
    long getTotalTokensUsedByUser(int userId) throws SQLException;
}