<%@ page import="com.example.courses.persistence.entity.Role" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/header/header"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
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
                        <li class="account-dropdown">
                            <div class="account-dropdown-control">
                                <div class="profile-img">
                                    <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt="">
                                </div>
                                <span class="user">${sessionScope.user.getFullName()}</span>
                                <div class="arrow"></div>
                            </div>
                            <div class="account-dropdown-menu hidden">
                                <a href="${pageContext.request.contextPath}/user?user_id=${sessionScope.user.getId()}"><fmt:message key="label.dropdown.my_profile"/></a>
                                <a href="${pageContext.request.contextPath}/auth/log_out">
                                    <fmt:message key="label.log_out"/>
                                </a>
                                <div class="language-switch">
                                    <a href="${requestScope['javax.servlet.forward.request_uri']}?lang=en" class="${sessionScope.lang == "en" ? 'selected' : ''}"><fmt:message key="label.dropdown.lang_english"/></a>
                                    <a href="${requestScope['javax.servlet.forward.request_uri']}?lang=uk" class="${sessionScope.lang == "uk" ? 'selected' : ''}"><fmt:message key="label.dropdown.lang_ukrainian"/></a>
                                </div>
                            </div>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
</header>
</body>
</html>
