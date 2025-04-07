package model;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * 消息实体类
 * @author jonysingl
 * @version 1.0
 * @since 2025-04-07
 */
public class Message {
    private Long messageId;
    private Long conversationId;
    private boolean isFromUser;  // 注意这里是布尔类型字段
    private String content;
    private Integer tokensUsed;
    private Timestamp createdAt;

    // 关联的对话对象，用于加载关联数据
    private Conversation conversation;

    // 默认构造函数
    public Message() {
    }

    // 带参数的构造函数
    public Message(Long messageId, Long conversationId, boolean isFromUser,
                   String content, Integer tokensUsed, Timestamp createdAt) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.isFromUser = isFromUser;
        this.content = content;
        this.tokensUsed = tokensUsed;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    // 布尔类型的getter通常使用is前缀，但为了与DAO实现一致，添加另一个getter方法
    public boolean isFromUser() {
        return isFromUser;
    }

    // 显式添加getIsFromUser方法解决兼容性问题
    public boolean getIsFromUser() {
        return isFromUser;
    }

    public void setFromUser(boolean fromUser) {
        this.isFromUser = fromUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(Integer tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(messageId, message.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", conversationId=" + conversationId +
                ", isFromUser=" + isFromUser +
                ", content='" + (content != null ? content.substring(0, Math.min(content.length(), 50)) + "..." : null) + '\'' +
                ", tokensUsed=" + tokensUsed +
                ", createdAt=" + createdAt +
                '}';
    }
}