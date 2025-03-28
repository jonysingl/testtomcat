<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>ChatBot 界面</title>
  <!-- 添加Font Awesome图标库 -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <style>
    :root {
      --primary-color: #4a6fa5;        /* 主色调：深蓝色 */
      --primary-light: #7798c8;        /* 浅蓝色 */
      --accent-color: #5e97f6;         /* 亮蓝色 */
      --user-message-color: #e1f0da;   /* 用户消息气泡颜色 */
      --bot-message-color: #f5f7fa;    /* 机器人消息气泡颜色 */
      --background-color: #f9fafc;     /* 背景色 */
      --sidebar-color: #2c3e50;        /* 侧边栏背景色 */
      --text-color: #333;              /* 主要文本颜色 */
      --light-text: #f9f9f9;           /* 浅色文本 */
      --border-radius: 12px;           /* 统一的圆角大小 */
    }

    html, body {
      height: 100%;
      margin: 0;
      overflow: hidden;
      font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
      background-color: var(--background-color);
      color: var(--text-color);
    }

    body, ul {
      padding: 0;
      list-style: none;
    }

    /* 登录按钮 - 位于界面右上角 */
    .login-button {
      position: fixed;
      top: 15px;
      right: 15px;
      font-size: 14px;
      color: white;
      background-color: var(--primary-color);
      padding: 8px 16px;
      border-radius: 20px;
      box-shadow: 0 2px 5px rgba(0,0,0,0.1);
      z-index: 2000; /* 确保显示在最上层 */
      cursor: pointer;
      transition: all 0.3s ease;
      text-decoration: none;
      display: flex;
      align-items: center;
    }

    .login-button:hover {
      background-color: var(--accent-color);
      box-shadow: 0 4px 8px rgba(0,0,0,0.2);
      transform: translateY(-2px);
    }

    .login-button .avatar {
      width: 24px;
      height: 24px;
      border-radius: 50%;
      background-color: white;
      color: var(--primary-color);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 8px;
      font-weight: bold;
      font-size: 12px;
    }

    /* 侧边栏样式 - 宽度更窄 */
    .sidebar {
      width: 250px; /* 更窄的侧边栏宽度 */
      background-color: var(--sidebar-color);
      color: var(--light-text);
      position: fixed;
      left: 0;
      top: 0;
      bottom: 0;
      z-index: 1000;
      transform: translateX(-100%);
      transition: transform 0.3s ease;
      display: flex;
      flex-direction: column;
    }

    /* 侧边栏激活状态 */
    .sidebar.active {
      transform: translateX(0);
    }

    /* 侧边栏切换按钮 */
    .toggle-button {
      position: fixed;
      left: 15px;
      top: 15px;
      background: var(--primary-color);
      color: white;
      border: none;
      width: 40px;
      height: 40px;
      border-radius: 50%;
      font-size: 20px;
      cursor: pointer;
      z-index: 1100;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
    }

    .toggle-button:hover {
      background-color: var(--accent-color);
    }
  </style>
</head>
<body>
<%
  // 检查用户是否已登录
  String username = (String) session.getAttribute("username");
  boolean isLoggedIn = (username != null);

  // 如果没有登录，重定向到聊天机器人页面
  if(!isLoggedIn) {
    response.sendRedirect("chatbot.jsp");
  }
%>

<% if(isLoggedIn) { %>
<!-- 这里是登录后的首页内容 -->
<div class="login-button" id="loginButton">
  <div class="avatar">
    <%= username.charAt(0) %>
  </div>
  <span><%= username %></span>
  <a href="logout.jsp" class="logout-link" style="margin-left: 8px; color: white; text-decoration: underline;">登出</a>
</div>

<!-- 侧边栏 -->
<div class="sidebar" id="sidebar">
  <div style="padding: 20px;">
    <h2>欢迎，<%= username %>!</h2>
    <p>您现在可以使用所有功能了</p>
  </div>
  <ul style="padding: 0 20px;">
    <li style="margin-bottom: 15px;"><a href="chatbot.jsp" style="color: white; text-decoration: none;">开始聊天</a></li>
    <li style="margin-bottom: 15px;"><a href="#" style="color: white; text-decoration: none;">设置</a></li>
    <li style="margin-bottom: 15px;"><a href="logout.jsp" style="color: white; text-decoration: none;">退出登录</a></li>
  </ul>
</div>

<!-- 侧边栏开关按钮 -->
<button class="toggle-button" id="toggleButton">
  <span id="toggleIcon">≡</span>
</button>

<!-- 内容区域 -->
<div style="padding: 60px 20px 20px 20px; text-align: center;">
  <h1>欢迎使用智能聊天助手</h1>
  <p>请点击右侧的按钮开始聊天</p>
  <a href="chatbot.jsp" style="display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #4a6fa5; color: white; text-decoration: none; border-radius: 5px;">开始聊天</a>
</div>
<% } %>

<script>
  // 侧边栏切换功能
  document.addEventListener('DOMContentLoaded', function() {
    const toggleButton = document.getElementById('toggleButton');
    const sidebar = document.getElementById('sidebar');

    if(toggleButton && sidebar) {
      toggleButton.addEventListener('click', function() {
        sidebar.classList.toggle('active');
      });
    }
  });
</script>
</body>
</html>