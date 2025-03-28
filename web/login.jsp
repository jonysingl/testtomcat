<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 智能聊天助手</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
        }

        body {
            background-color: #f7f9fc;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .login-card {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            width: 340px;
            padding: 30px;
        }

        .login-header {
            text-align: center;
            margin-bottom: 25px;
        }

        .login-header h1 {
            color: #333;
            font-size: 24px;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .login-header p {
            color: #666;
            font-size: 14px;
        }

        .input-group {
            margin-bottom: 16px;
        }

        .input-group label {
            display: block;
            margin-bottom: 6px;
            font-size: 14px;
            color: #555;
        }

        .input-group input {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            transition: border 0.3s;
        }

        .input-group input:focus {
            border-color: #4a6fa5;
            outline: none;
            box-shadow: 0 0 0 2px rgba(74, 111, 165, 0.1);
        }

        .password-container {
            position: relative; /* 确保子元素绝对定位参考此容器 */
        }

        .toggle-password {
            position: absolute;
            right: 10px; /* 保持水平位置 */
            top: 50%; /* 垂直居中 */
            transform: translateY(-50%); /* 精确垂直居中 */
            cursor: pointer;
            color: #999;
        }

        .login-btn {
            background-color: #4a6fa5;
            color: white;
            border: none;
            border-radius: 6px;
            padding: 12px;
            font-size: 15px;
            font-weight: 500;
            width: 100%;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .login-btn:hover {
            background-color: #5e97f6;
        }

        .signup-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: #666;
        }

        .signup-link a {
            color: #4a6fa5;
            text-decoration: none;
            font-weight: 500;
        }

        .signup-link a:hover {
            text-decoration: underline;
        }

        .alert {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
            font-size: 14px;
        }
    </style>
</head>
<body>
<div class="login-card">
    <div class="login-header">
        <h1>欢迎回来</h1>
        <p>请登录您的账户</p>
    </div>

    <%-- 显示登录错误信息 --%>
    <% if(request.getParameter("error") != null) { %>
    <div class="alert" style="background-color: #ffebee; color: #c62828; border: 1px solid #ffcdd2;">
        用户名或密码错误，请重试。
    </div>
    <% } %>

    <form action="loginProcess.jsp" method="post">
        <div class="input-group">
            <label for="username">用户名</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="input-group">
            <label for="password">密码</label>
            <div class="password-container">
                <input type="password" id="password" name="password" required>
                <span class="toggle-password" onclick="togglePasswordVisibility()">👁️</span>
            </div>
        </div>
        <button type="submit" class="login-btn">登录</button>
    </form>
    <div class="signup-link">
        还没有账户？ <a href="register.jsp">立即注册</a>
    </div>
</div>

<script>
    function togglePasswordVisibility() {
        const passwordInput = document.getElementById('password');
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
        } else {
            passwordInput.type = 'password';
        }
    }
</script>
</body>
</html>