<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  // 清除session
  session.invalidate();

  // 重定向到登录页面
  response.sendRedirect("login.jsp");
%>