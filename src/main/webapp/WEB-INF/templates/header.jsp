<%@ page import="com.example.courses.persistence.entity.Role" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie['lang'].value}"/>
<fmt:setBundle basename="i18n/header/header"/>

<!DOCTYPE html>
<html lang="${cookie['lang'].value}">
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/header.css">
</head>
<body>
<header>

    <div class="container">
        <div class="nav-bar">
            <div class="title">
                <a href="${pageContext.request.contextPath}"><h1>Final Project</h1></a>
            </div>
            <div class="nav">
                <ul>
                    <c:if test="${sessionScope.user == null}">
                        <li><a href="${pageContext.request.contextPath}/courses">
                            <fmt:message key="label.courses"/>
                        </a>
                        </li>
                        <div class="vr"></div>
                        <li><a href="${pageContext.request.contextPath}/auth/log_in">
                            <fmt:message key="label.log_in"/>
                        </a></li>
                        <li><a href="${pageContext.request.contextPath}/auth/sign_up">
                            <fmt:message key="label.sign_up"/>
                        </a></li>
                    </c:if>

                    <c:if test="${sessionScope.user != null}">
                        <c:choose>
                            <c:when test="${sessionScope.user.getRole().equals(Role.ADMIN)}">
                                <li><a href="${pageContext.request.contextPath}/courses">
                                    <fmt:message key="label.courses"/>
                                </a></li>
                                <li><a href="${pageContext.request.contextPath}/admin/course/new">
                                    <fmt:message key="label.new_course"/>
                                </a></li>
                                <li><a href="${pageContext.request.contextPath}/admin/new_user">
                                    <fmt:message key="label.new_user"/>
                                </a></li>
                                <li><a href="${pageContext.request.contextPath}/admin/students">
                                    <fmt:message key="label.students"/>
                                </a></li>
                            </c:when>
                            <c:when test="${sessionScope.user.getRole().equals(Role.TEACHER)}">
                                <li><a href="${pageContext.request.contextPath}/user_courses">
                                    <fmt:message key="label.user's_courses"/>
                                </a></li>
                            </c:when>
                            <c:when test="${sessionScope.user.getRole().equals(Role.STUDENT)}">
                                <li><a href="${pageContext.request.contextPath}/courses">
                                    <fmt:message key="label.courses"/>
                                </a></li>
                                <li><a href="${pageContext.request.contextPath}/user_courses">
                                    <fmt:message key="label.user's_courses"/>
                                </a></li>
                            </c:when>
                        </c:choose>

                        <div class="vr"></div>
                        <li><a href="${pageContext.request.contextPath}/auth/log_out">
                            <fmt:message key="label.log_out"/>
                        </a></li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
</header>
</body>
</html>
