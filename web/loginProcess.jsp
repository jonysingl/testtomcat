<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  // 简单的登录处理逻辑
  String username = request.getParameter("username");
  String password = request.getParameter("password");

  // 在实际应用中，你应该在数据库中验证用户名和密码
  // 这里为了演示，使用简单的硬编码验证
  if("admin".equals(username) && "password".equals(password)) {
    // 登录成功，设置session
    session.setAttribute("username", username);
    response.sendRedirect("index.jsp");
  } else {
    // 登录失败，重定向到登录页面并显示错误信息
    response.sendRedirect("login.jsp?error=true");
  }
%>