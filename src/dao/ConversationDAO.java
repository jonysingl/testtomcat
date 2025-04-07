package dao;


import model.Conversation;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * 对话数据访问对象接口
 * @author jonysingl
 * @version 1.0
 * @since 2025-04-07
 */
public interface ConversationDAO {

    /**
     * 创建新对话
     * @param conversation 对话对象
     * @return 创建的对话ID
     * @throws SQLException 数据库操作异常
     */
    long create(Conversation conversation) throws SQLException;

    /**
     * 根据ID查找对话
     * @param conversationId 对话ID
     * @return 包含对话的Optional，如果未找到则为empty
     * @throws SQLException 数据库操作异常
     */
    Optional<Conversation> findById(long conversationId) throws SQLException;

    /**
     * 根据用户ID获取该用户的所有对话，按最后消息时间倒序排列
     * @param userId 用户ID
     * @return 对话列表
     * @throws SQLException 数据库操作异常
     */
    List<Conversation> findByUserId(int userId) throws SQLException;

    /**
     * 根据用户ID和模型类型获取对话列表
     * @param userId 用户ID
     * @param modelType 模型类型
     * @return 对话列表
     * @throws SQLException 数据库操作异常
     */
    List<Conversation> findByUserIdAndModelType(int userId, String modelType) throws SQLException;

    /**
     * 更新对话标题
     * @param conversationId 对话ID
     * @param title 新标题
     * @return 是否更新成功
     * @throws SQLException 数据库操作异常
     */
    boolean updateTitle(long conversationId, String title) throws SQLException;

    /**
     * 更新最后消息时间
     * @param conversationId 对话ID
     * @return 是否更新成功
     * @throws SQLException 数据库操作异常
     */
    boolean updateLastMessageTime(long conversationId) throws SQLException;

    /**
     * 删除对话
     * @param conversationId 对话ID
     * @return 是否删除成功
     * @throws SQLException 数据库操作异常
     */
    boolean delete(long conversationId) throws SQLException;

    /**
     * 删除用户的所有对话
     * @param userId 用户ID
     * @return 删除的对话数量
     * @throws SQLException 数据库操作异常
     */
    int deleteAllByUserId(int userId) throws SQLException;

    /**
     * 获取对话数量
     * @param userId 用户ID
     * @return 对话数量
     * @throws SQLException 数据库操作异常
     */
    int countByUserId(int userId) throws SQLException;

    /**
     * 检查对话是否属于指定用户
     * @param conversationId 对话ID
     * @param userId 用户ID
     * @return 如果对话属于该用户则返回true，否则返回false
     * @throws SQLException 数据库操作异常
     */
    boolean belongsToUser(long conversationId, int userId) throws SQLException;
}