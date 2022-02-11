<%@ page import="com.example.courses.persistence.entity.Role" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie['lang'].value}"/>
<fmt:setBundle basename="i18n/profile/profile"/>
<!DOCTYPE html>
<html lang="${cookie['lang'].value}">
<head>
    <title><fmt:message key="label.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/header.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/filter.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/form.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_row.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/profile.css"/>
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main class="background" style="background-image: url('${pageContext.request.contextPath}/static/images/1.jpg')">

    <div class="container">
        <div class="info-block">
            <div class="image-block">
                <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt=""/>
            </div>

            <div class="data-block">
                <div class="data-row">
                    <span class="description-span"><fmt:message key="label.info_first_name"/></span>
                    <span class="info-span">${requestScope.user.getFirstName()}</span>
                </div>

                <div class="data-row">
                    <span class="description-span"><fmt:message key="label.info_last_name"/></span>
                    <span class="info-span">${requestScope.user.getLastName()}</span>
                </div>

                <div class="data-row">
                    <span class="description-span"><fmt:message key="label.info_email"/></span>
                    <span class="info-span">${requestScope.user.getEmail()}</span>
                </div>

                <div class="data-row">
                    <span class="description-span"><fmt:message key="label.info_role"/></span>
                    <span class="info-span">${requestScope.user.getRole().getRole()}</span>
                </div>

                <c:if test="${requestScope.user.getId() == sessionScope.user.getId()}">
                    <div class="action-row">
                        <button>
                            <a href=""><fmt:message key="label.btn_edit"/></a>
                        </button>
                    </div>
                </c:if>
                <c:if test="${sessionScope.user.getRole().equals(Role.ADMIN)}">
                    <div class="action-row">
                        <c:if test="${!requestScope.user.isBlocked()}">
                            <button>
                                <a href="${pageContext.request.contextPath}/admin/block?student_id=${requestScope.user.getId()}">
                                    <fmt:message key="label.btn_block"/>
                                </a>
                            </button>
                        </c:if>
                        <c:if test="${requestScope.user.isBlocked()}">
                            <button>
                                <a href="${pageContext.request.contextPath}/admin/unblock?student_id=${requestScope.user.getId()}">
                                    <fmt:message key="label.btn_unblock"/>
                                </a>
                            </button>
                        </c:if>
                    </div>
                </c:if>
            </div>

        </div>

        <div class="courses-block">
            <div class="courses-title-block">
                <h2>
                    <fmt:message key="label.user's_courses"/>
                </h2>
            </div>

            <c:forEach var="course" items="${requestScope.courses}">
                <div class="course">
                    <div class="info-box">
                        <div class="info-row">
                            <a href="${pageContext.request.contextPath}/course?course_id=${course.getId()}">
                                <h2>${course.getTitle()}</h2>
                            </a>
                        </div>
                        <div class="info-row">
                            <p class="subject-row"><fmt:message key="label.course_subject"/></p>
                            <span class="subject">${course.getSubject()}</span>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</main>

<footer>
</footer>
</body>
</html>