<%--
  Created by IntelliJ IDEA.
  User: 32826
  Date: 2025/4/7
  Time: 22:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="dao.UserDAOImpl" %>
<%@ page import="model.User" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  try {
    // 获取表单数据
    String username = request.getParameter("username");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirm-password");

    // 验证密码是否匹配
    if (!password.equals(confirmPassword)) {
      response.sendRedirect("register.jsp?error=密码不匹配");
      return;
    }

    // 创建User对象
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);

    // 创建UserDAOImpl实例
    UserDAOImpl userDAO = new UserDAOImpl();

    // 检查用户名和邮箱是否已存在
    if (!userDAO.isUsernameAvailable(username)) {
      response.sendRedirect("register.jsp?error=用户名已存在");
      return;
    }
    if (!userDAO.isEmailAvailable(email)) {
      response.sendRedirect("register.jsp?error=邮箱已存在");
      return;
    }

    // 创建用户
    int userId = userDAO.create(user);

    // 注册成功，重定向到注册页面并显示成功消息
    response.sendRedirect("register.jsp?success=true");
  } catch (Exception e) {
    e.printStackTrace();
    response.sendRedirect("register.jsp?error=注册失败，请稍后重试：" + e.getMessage());
  }
%>

