<%@ page import="dao.UserDAOImpl" %>
<%@ page import="model.User" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  try {
    // 获取表单数据
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    System.out.println("username: " + username);
    System.out.println("password: " + password);
    // 创建UserDAOImpl实例
    UserDAOImpl userDAO = new UserDAOImpl();

    // 使用已有的findByUsername方法查找用户
    Optional<User> userOptional = userDAO.findByUsername(username);

    // 检查用户是否存在及密码是否匹配
    if(userOptional.isPresent() && password.equals(userOptional.get().getPassword())) {
      User user = userOptional.get();

      // 记录登录时间
      try {
        userDAO.updateLastLogin(user.getUserId());
      } catch (Exception e) {
        // 记录登录时间失败，但不影响用户登录
        e.printStackTrace();
      }

      // 登录成功，设置session
      session.setAttribute("username", user.getUsername());
      session.setAttribute("userId", user.getUserId());
      response.sendRedirect("chatbot.jsp");
    } else {
      // 登录失败，重定向到登录页面并显示错误信息
      response.sendRedirect("login.jsp?error=true");
    }
  } catch (Exception e) {
    e.printStackTrace();
    // 记录错误信息到日志
    out.println("登录过程中发生错误: " + e.getMessage());
    // 重定向到登录页面显示错误
    response.sendRedirect("login.jsp?error=true&message=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
  }
%>