<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>智能聊天助手</title>
    <!-- 添加Font Awesome图标库 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <!-- 引用CSS文件 -->
    <link rel="stylesheet" href="css/chatbot.css">
    <style>
        /* 在这里可以添加或覆盖样式 */
    </style>
</head>
<body>
<%
    // 检查用户是否已登录
    String username = (String) session.getAttribute("username");
    boolean isLoggedIn = (username != null);
%>

<!-- 登录按钮 -->
<% if(isLoggedIn) { %>
<div class="login-button" id="loginButton">
    <div class="avatar">
        <%= username.charAt(0) %>
    </div>
    <span><%= username %></span>
    <a href="logout.jsp" class="logout-link">登出</a>
</div>
<% } else { %>
<a href="login.jsp" class="login-button" id="loginButton">
    <div class="avatar">
        <i class="fa fa-user"></i>
    </div>
    <span id="loginText">登录</span>
</a>
<% } %>

<!-- 侧边栏 -->
<div class="sidebar" id="sidebar">
    <button id="newConversationButton">开启新对话</button>
    <div id="conversationHistory">
        <div class="history-title">对话历史</div>
        <!-- 历史记录将通过JavaScript动态添加 -->
        <% if(isLoggedIn) { %>
        <!-- 这里可以添加JSP代码来遍历并显示用户的历史对话 -->
        <% } else { %>
        <div class="history-item">
            <p>请登录以查看历史对话</p>
        </div>
        <% } %>
    </div>
</div>

<!-- 侧边栏开关按钮 -->
<button class="toggle-button" id="toggleButton">
    <span id="toggleIcon">≡</span>
</button>

<!-- 内容区域 -->
<div class="content" id="main">
    <!-- 聊天头部 -->
    <div class="chat-header">
        <h1>智能聊天助手</h1>
        <p>随时为您解答问题</p>
    </div>

    <!-- 聊天输出区域 -->
    <div class="chat-output" id="chatOutput"></div>

    <!-- 打字指示器 -->
    <div class="typing-indicator" id="typingIndicator" style="display: none;">
        <span></span>
        <span></span>
        <span></span>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input">
        <input type="text" id="chatInput" placeholder="请输入聊天内容">
        <button id="sendButton">发送</button>
    </div>
</div>

<script>
    // 当前用户状态和对话状态
    let currentUser = null;
    let currentConversationId = null;
    let currentMessages = [];

    <% if(isLoggedIn) { %>
    currentUser = {
        username: "<%= username %>"
    };
    <% } %>

    // 格式化日期函数
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    // 显示通知函数
    function showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.textContent = message;
        document.body.appendChild(notification);

        // 3秒后自动消失
        setTimeout(() => {
            notification.style.opacity = '0';
            setTimeout(() => notification.remove(), 300);
        }, 3000);
    }

    // 页面加载时初始化
    document.addEventListener('DOMContentLoaded', function() {
        // 显示欢迎消息
        showWelcomeMessage();

        // 绑定发送消息事件
        const sendButton = document.getElementById('sendButton');
        const chatInput = document.getElementById('chatInput');

        sendButton.addEventListener('click', sendMessage);
        chatInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendMessage();
            }
        });

        // 侧边栏切换
        const toggleButton = document.getElementById('toggleButton');
        const sidebar = document.getElementById('sidebar');
        toggleButton.addEventListener('click', function() {
            sidebar.classList.toggle('active');
            toggleButton.classList.toggle('active');
        });
    });

    // 显示欢迎消息
    function showWelcomeMessage() {
        const chatOutput = document.getElementById('chatOutput');
        chatOutput.innerHTML = '';

        var welcomeMessage = document.createElement('div');
        welcomeMessage.className = 'response-message';
        welcomeMessage.textContent = "您好！我是智能聊天助手，有什么可以帮您的吗？";
        addTimestamp(welcomeMessage);
        chatOutput.appendChild(welcomeMessage);
    }

    // 添加时间戳
    function addTimestamp(element) {
        const timestamp = document.createElement('div');
        timestamp.className = 'timestamp';
        timestamp.textContent = formatDate(new Date());
        element.appendChild(timestamp);
    }

    // 发送消息
    function sendMessage() {
        const chatInput = document.getElementById('chatInput');
        const chatOutput = document.getElementById('chatOutput');
        const message = chatInput.value.trim();

        if (message === '') return;

        // 显示用户消息
        const userMessage = document.createElement('div');
        userMessage.className = 'user-message';
        userMessage.textContent = message;
        addTimestamp(userMessage);
        chatOutput.appendChild(userMessage);

        // 清空输入框
        chatInput.value = '';

        // 显示正在输入指示器
        document.getElementById('typingIndicator').style.display = 'block';
        chatOutput.scrollTop = chatOutput.scrollHeight;

        // 模拟服务器响应
        setTimeout(() => {
            // 隐藏输入指示器
            document.getElementById('typingIndicator').style.display = 'none';

            // 添加机器人响应
            const botResponse = document.createElement('div');
            botResponse.className = 'response-message';

            // 根据用户输入生成简单的回复
            let reply = "我理解您的问题是关于\"" + message + "\"。我正在学习中，希望能更好地为您服务。";

            // 简单回复逻辑
            if (message.includes("你好") || message.includes("您好")) {
                reply = "您好！有什么可以帮助您的吗？";
            } else if (message.includes("天气")) {
                reply = "很抱歉，我暂时无法提供实时天气信息。不过我可以帮您解答生活中的其他问题！";
            } else if (message.includes("谢谢") || message.includes("感谢")) {
                reply = "不客气！我很高兴能帮到您。如果还有其他问题，随时可以问我。";
            } else if (message.includes("再见")) {
                reply = "再见！有需要随时来找我。";
            }

            botResponse.textContent = reply;
            addTimestamp(botResponse);
            chatOutput.appendChild(botResponse);

            // 滚动到最新消息
            chatOutput.scrollTop = chatOutput.scrollHeight;
        }, 1000);
    }
</script>
</body>
</html>