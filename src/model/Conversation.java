package model;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * 对话实体类
 * @author jonysingl
 * @version 1.0
 * @since 2025-04-07
 */
public class Conversation {
    private Long conversationId;
    private Integer userId;
    private String title;
    private String modelType;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastMessageAt;

    // 关联的用户对象，用于加载关联数据
    private User user;

    // 默认构造函数
    public Conversation() {
    }

    // 带参数的构造函数
    public Conversation(Long conversationId, Integer userId, String title, String modelType,
                        Timestamp createdAt, Timestamp updatedAt, Timestamp lastMessageAt) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.title = title;
        this.modelType = modelType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastMessageAt = lastMessageAt;
    }

    // Getters and Setters
    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(Timestamp lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return Objects.equals(conversationId, that.conversationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationId);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "conversationId=" + conversationId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", modelType='" + modelType + '\'' +
                ", createdAt=" + createdAt +
                ", lastMessageAt=" + lastMessageAt +
                '}';
    }
}