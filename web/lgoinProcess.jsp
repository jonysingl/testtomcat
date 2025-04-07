<%--
  Created by IntelliJ IDEA.
  User: 32826
  Date: 2025/4/7
  Time: 22:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="dao.UserDAOImpl" %>
<%@ page import="model.User" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // 当前时间: 2025-04-07 14:45:08
  // 当前用户: jonysingl

  // 获取表单数据
  String username = request.getParameter("username");
  String password = request.getParameter("password");

  // 基本验证
  if (username == null || username.trim().isEmpty() ||
          password == null || password.trim().isEmpty()) {
    response.sendRedirect("login.jsp?error=true");
    return;
  }

  // 创建UserDAOImpl实例
  UserDAOImpl userDAO = new UserDAOImpl();

  try {
    // 尝试通过用户名查找用户
    Optional<User> userOptional = userDAO.findByUsername(username);

    // 如果未找到，尝试通过邮箱查找（支持用户名或邮箱登录）
    if (!userOptional.isPresent() && username.contains("@")) {
      userOptional = userDAO.findByEmail(username);
    }

    // 用户不存在
    if (!userOptional.isPresent()) {
      System.out.println("用户不存在");
      response.sendRedirect("login.jsp?error=true");
      return;
    }

    User user = userOptional.get();

    // 直接比较明文密码
    if (password.equals(user.getPassword())) {
      // 密码正确，更新最后登录时间
      userDAO.updateLastLogin(user.getUserId());

      // 在会话中保存用户信息
      session.setAttribute("userId", user.getUserId());
      session.setAttribute("username", user.getUsername());
      session.setAttribute("email", user.getEmail());

      // 重定向到首页或用户主页
      response.sendRedirect("index.jsp");
    } else {
      // 密码错误
      response.sendRedirect("login.jsp?error=true");
    }

  } catch (SQLException e) {
    // 数据库操作异常
    e.printStackTrace();
    response.sendRedirect("login.jsp?error=true");
  }
%>