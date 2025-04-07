<%@ page  language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册 - 智能聊天助手</title>
    <style>
        :root {
            --primary-color: #4a6fa5;        /* 主色调：深蓝色 */
            --primary-light: #7798c8;        /* 浅蓝色 */
            --accent-color: #5e97f6;         /* 亮蓝色 */
            --background-color: #f9fafc;     /* 背景色 */
            --text-color: #333;              /* 主要文本颜色 */
            --light-text: #f9f9f9;           /* 浅色文本 */
            --border-radius: 12px;           /* 统一的圆角大小 */
            --error-color: #e53935;          /* 错误提示颜色 */
            --success-color: #43a047;        /* 成功提示颜色 */
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
            background-color: var(--background-color);
            color: var(--text-color);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
        }

        .register-container {
            width: 360px;
            background-color: white;
            border-radius: var(--border-radius);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        .register-header {
            background-color: var(--primary-color);
            color: white;
            padding: 25px 0;
            text-align: center;
        }

        .register-header h1 {
            margin: 0;
            font-size: 22px;
            font-weight: 500;
        }

        .register-header p {
            margin-top: 5px;
            font-size: 14px;
            opacity: 0.8;
        }

        .register-header .logo {
            width: 70px;
            height: 70px;
            margin: 0 auto 15px;
            background-color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .logo img {
            width: 50px;
            height: 50px;
        }

        .register-form {
            padding: 30px;
        }

        .form-group {
            margin-bottom: 20px;
            position: relative;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-size: 14px;
            color: #666;
        }

        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            transition: all 0.3s;
        }

        .form-group input:focus {
            border-color: var(--primary-color);
            outline: none;
            box-shadow: 0 0 0 3px rgba(74, 111, 165, 0.1);
        }

        .error-message {
            color: var(--error-color);
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }

        .btn-register {
            background-color: var(--primary-color);
            color: white;
            border: none;
            width: 100%;
            padding: 12px;
            font-size: 16px;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .btn-register:hover {
            background-color: var(--accent-color);
        }

        .login-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
        }

        .login-link a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
        }

        .login-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="register-container">
    <div class="register-header">
        <div class="logo">
            <!-- 如果有 logo，可以放在这里 -->
            <i class="fas fa-robot" style="font-size: 30px; color: var(--primary-color);"></i>
        </div>
        <h1>创建新账户</h1>
        <p>加入智能聊天助手，开始您的智能对话之旅</p>
    </div>

    <%-- 注册消息展示 --%>
    <% if(request.getParameter("error") != null) { %>
    <div class="alert" style="background-color: #ffebee; color: #c62828; border: 1px solid #ffcdd2; padding: 10px; margin: 15px;">
        <%= request.getParameter("error") %>
    </div>
    <% } %>
    <% if(request.getParameter("success") != null) { %>
    <div class="alert" style="background-color: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; padding: 10px; margin: 15px;">
        注册成功！请前往<a href="login.jsp">登录页面</a>。
    </div>
    <% } %>

    <div class="register-form">
        <form action="registerProcess.jsp" method="post" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="username">用户名</label>
                <input type="text" id="username" name="username" required>
                <span class="error-message" id="username-error"></span>
            </div>
            <div class="form-group">
                <label for="email">电子邮箱</label>
                <input type="email" id="email" name="email" required>
                <span class="error-message" id="email-error"></span>
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" id="password" name="password" required>
                <span class="error-message" id="password-error"></span>
            </div>
            <div class="form-group">
                <label for="confirm-password">确认密码</label>
                <input type="password" id="confirm-password" name="confirm-password" required>
                <span class="error-message" id="confirm-password-error"></span>
            </div>
            <button type="submit" class="btn-register">注册</button>
        </form>
        <div class="login-link">
            已有账户？<a href="login.jsp">立即登录</a>
        </div>
    </div>
</div>

<script>
    function validateForm() {
        let isValid = true;
        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirm-password').value;

        // 验证用户名
        if(username.length < 3) {
            document.getElementById('username-error').textContent = '用户名长度至少为3个字符';
            document.getElementById('username-error').style.display = 'block';
            isValid = false;
        } else {
            document.getElementById('username-error').style.display = 'none';
        }

        // 验证邮箱格式
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if(!emailRegex.test(email)) {
            document.getElementById('email-error').textContent = '请输入有效的邮箱地址';
            document.getElementById('email-error').style.display = 'block';
            isValid = false;
        } else {
            document.getElementById('email-error').style.display = 'none';
        }

        // 验证密码强度
        if(password.length < 6) {
            document.getElementById('password-error').textContent = '密码长度至少为6个字符';
            document.getElementById('password-error').style.display = 'block';
            isValid = false;
        } else {
            document.getElementById('password-error').style.display = 'none';
        }

        // 验证密码匹配
        if(password !== confirmPassword) {
            document.getElementById('confirm-password-error').textContent = '两次输入的密码不一致';
            document.getElementById('confirm-password-error').style.display = 'block';
            isValid = false;
        } else {
            document.getElementById('confirm-password-error').style.display = 'none';
        }
        return isValid;
    }
</script>
</body>
</html>