<%@ page import="com.example.courses.persistence.entity.Role" %>
<%@ page import="com.example.courses.persistence.entity.CourseStatus" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie['lang'].value}"/>
<fmt:setBundle basename="i18n/user_courses/user_courses"/>

<!DOCTYPE html>
<html lang="${cookie['lang'].value}">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_list.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_row.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/css/courses/user_courses.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <div class="status-filter-box">
        <div class="container">
            <div class="status-filters">
                <button data-status="" class="status-filter selected">
                    <fmt:message key="label.status_all"/>
                </button>
                <button data-status="not started" class="status-filter">
                    <fmt:message key="label.status_not_started"/>
                </button>
                <button data-status="in progress" class="status-filter">
                    <fmt:message key="label.status_in_progress"/>
                </button>
                <button data-status="completed" class="status-filter">
                    <fmt:message key="label.status_completed"/>
                </button>
            </div>
        </div>
    </div>

    <div class="content-box">
        <div class="container">
            <c:forEach var="courseDTO" items="${requestScope.courses}">
                <c:set var="course" value="${courseDTO.getCourse()}"/>
                <div class="course" data-status="${course.getCourseStatus().getStatus()}">
                    <div class="img-box">
                        <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt=""/>
                    </div>
                    <div class="info-box">
                        <div class="info-row">
                            <a href="${pageContext.request.contextPath}/course?course_id=${course.getId()}">
                                <h2>${course.getTitle()}</h2>
                            </a>
                        </div>
                        <div class="info-row">
                            <p class="subject-row">
                                <fmt:message key="label.subject"/>
                            </p>
                            <span class="subject">${courseDTO.getSubject().getSubject()}</span>
                        </div>
                        <div class="info-row">
                            <p class="status-row">
                                <fmt:message key="label.status"/>
                            </p>
                            <span class="status">${course.getCourseStatus().getStatus()}</span>
                        </div>
                    </div>

                    <c:if test="${sessionScope.user.getRole().equals(Role.STUDENT)}">
                        <c:if test="${course.getCourseStatus().equals(CourseStatus.COMPLETED)}">
                    <div class="score-box">
                        <h3>
                            <fmt:message key="label.score"/>
                        </h3>
                        <h3>
                            ${requestScope.scores.getOrDefault(course.getId(), 0)} / ${course.getMaxScore()}
                        </h3>
                    </div>
                        </c:if>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </div>
</main>

<footer>
</footer>

<script src="${pageContext.request.contextPath}/static/js/user_courses.js"></script>
</body>
</html>