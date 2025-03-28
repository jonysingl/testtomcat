<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
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

  // 在实际应用中，你应该将用户信息存储到数据库中
  // 这里为了演示，我们只进行简单的验证并返回成功信息

  // 假设注册成功
  response.sendRedirect("register.jsp?success=true");
%>