<%@ page import="com.example.courses.persistence.entity.User" %>
<%@ page import="com.example.courses.persistence.entity.Role" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/header.css">
</head>
<body>
<header>
    <%
        User user = (User) session.getAttribute("user");
    %>
    <div class="container">
        <div class="nav-bar">
            <div class="title">
                <a href="${pageContext.request.contextPath}"><h1>Final Project</h1></a>
            </div>
            <div class="nav">
                <ul>
                    <%
                        if (user == null) {
                    %>
                        <li><a href="${pageContext.request.contextPath}/courses">Courses</a></li>
                        <div class="vr"></div>
                        <li><a href="${pageContext.request.contextPath}/auth/log_in">Log In</a></li>
                        <li><a href="${pageContext.request.contextPath}/auth/sign_up">Sign Up</a></li>
                    <%
                        } else {
                    %>
                    <%
                        if (user.getRole().equals(Role.ADMIN)) {
                    %>
                        <li><a href="${pageContext.request.contextPath}/courses">Courses</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/course/new">New Course</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/new_user">New Account</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/students">Students</a></li>
                    <%
                        }
                    %>
                        <div class="vr"></div>
                        <li><a href="${pageContext.request.contextPath}/auth/log_out">Log Out</a></li>
                    <%
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>
</header>
</body>
</html>
