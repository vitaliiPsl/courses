<%@ page import="com.example.courses.persistence.entity.Role" %>
<%@ page import="com.example.courses.persistence.entity.CourseStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/course/course"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title><fmt:message key="label.title"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/table.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <c:set var="courseDTO" value="${requestScope.course}"/>
    <c:set var="course" value="${courseDTO.getCourse()}"/>
    <c:set var="subject" value="${courseDTO.getSubject()}"/>
    <c:set var="teacher" value="${courseDTO.getTeacher()}"/>
    <c:set var="students" value="${courseDTO.getStudents()}"/>

    <div class="content-block">
        <div class="container">
            <div class="content">
                <div class="info-block">
                    <h1 class="course-name">
                        <c:out value="${course.getTitle()}"/>
                    </h1>
                    <h3 class="course-subject">
                        <c:out value="${subject.getSubject()}"/>
                    </h3>
                    <p class="course-description">
                        <c:out value="${course.getDescription()}"/>
                    </p>
                    <p class="teacher"><fmt:message key="label.teacher"/>
                        <a href="${pageContext.request.contextPath}/user?user_id=${teacher.getId()}">
                            <span>
                                <c:out value="${teacher.getFullName()}"/>
                            </span>
                        </a>
                    </p>

                    <%--Check if user is not authenticated and if it is a student--%>
                    <c:if test="${sessionScope.user == null || sessionScope.user.getRole().equals(Role.STUDENT)}">

                        <%--If course is completed, than student can download certificate--%>
                        <c:if test="${sessionScope.user != null && course.getCourseStatus().equals(CourseStatus.COMPLETED)}">
                            <button class="certificate-btn">
                                <a href="${pageContext.request.contextPath}/certificate?course_id=${course.getId()}"
                                   download>
                                    <fmt:message key="label.certificate"/>
                                </a>
                            </button>
                        </c:if>

                        <%--If course hasn't started yet--%>
                        <c:if test="${course.getCourseStatus().equals(CourseStatus.NOT_STARTED)}">
                            <div class="enroll-block">
                                    <%--Show enroll button if student isn't already enrolled--%>
                                <c:if test="${requestScope.student_course == null}">
                                    <form action="${pageContext.request.contextPath}/enroll?course_id=${course.getId()}"
                                          method="post">
                                        <button type="submit" class="enroll-btn">
                                            <span>
                                                <fmt:message key="label.enroll_btn_enroll"/>
                                            </span>

                                                <%--Start date--%>
                                            <span>
                                               <fmt:message key="label.enroll_btn_starts"/>
                                               <c:out value="${course.getStartDate().toLocalDate()}"/>
                                            </span>
                                        </button>
                                    </form>
                                </c:if>

                                    <%--If student is already enrolled, then show 'enrolled' message--%>
                                <c:if test="${requestScope.student_course != null}">
                                    <div class="enrolled">
                                        <span>
                                            <fmt:message key="label.enroll_status_enrolled"/>
                                        </span>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>

                        <%--Number of student that already enrolled--%>
                        <div class="enrolled-count">
                            <span class="enrolled-span">
                                <fmt:message key="label.enroll_number_of_students"/>
                            </span>
                            <span>
                                <c:out value="${students.size()}"/>
                            </span>
                        </div>
                    </c:if>
                </div>

                <div class="image-block">
                    <div class="image-wrapper">
                        <img src="${pageContext.request.contextPath}/image?image_type=course&image_name=${course.getImageName()}"
                             alt=""/>
                    </div>
                </div>
            </div>

            <%--If user is ADMIN or TEACHER--%>
            <c:if test="${sessionScope.user != null && !sessionScope.user.getRole().equals(Role.STUDENT)}">
                <div class="students-block">
                    <h1>
                        <fmt:message key="label.table.title"/>
                    </h1>

                    <%--If there are students--%>
                    <c:if test="${!students.isEmpty()}">
                        <form action="${pageContext.request.contextPath}/teacher/course/score?course_id=${course.getId()}"
                              method="post">

                            <table class="students-table">
                                <tr>
                                    <th class="first-name-header">
                                        <fmt:message key="label.table.first_name"/>
                                    </th>

                                    <th class="last-name-header">
                                        <fmt:message key="label.table.last_name"/>
                                    </th>

                                    <th class="email-header">
                                        <fmt:message key="label.table.email"/>
                                    </th>

                                    <c:if test="${sessionScope.user.getRole().equals(Role.ADMIN)}">
                                        <th class="block-header">
                                            <fmt:message key="label.table.block_status"/>
                                        </th>
                                    </c:if>

                                        <%--If it is a teacher that teaches this course --%>
                                    <c:if test="${sessionScope.user.getRole().equals(Role.TEACHER) && course.getTeacherId() == sessionScope.user.getId()}">

                                        <%--If course has already started yet--%>
                                        <c:if test="${!course.getCourseStatus().equals(CourseStatus.NOT_STARTED)}">
                                            <th class="score-header">
                                                <fmt:message key="label.table.score"/>
                                            </th>
                                        </c:if>
                                    </c:if>
                                </tr>

                                <c:forEach var="student" items="${students}">
                                    <tr>
                                        <td class="first-name">
                                            <a href="${pageContext.request.contextPath}/user?user_id=${student.getId()}">
                                                <c:out value="${student.getFirstName()}"/>
                                            </a>
                                        </td>
                                        <td class="last-name">
                                            <a href="${pageContext.request.contextPath}/user?user_id=${student.getId()}">
                                                <c:out value="${student.getLastName()}"/>
                                            </a>
                                        </td>
                                        <td class="email">
                                            <a href="${pageContext.request.contextPath}/user?user_id=${student.getId()}">
                                                <c:out value="${student.getEmail()}"/>
                                            </a>
                                        </td>

                                        <c:if test="${sessionScope.user.getRole().equals(Role.ADMIN)}">
                                            <td class="block">
                                                <c:if test="${student.isBlocked()}">
                                                    <button class="block-button">
                                                        <a href="${pageContext.request.contextPath}/admin/unblock?student_id=${student.getId()}">
                                                            <fmt:message key="label.table.btn.unblock"/>
                                                        </a>
                                                    </button>
                                                </c:if>
                                                <c:if test="${!student.isBlocked()}">
                                                    <button class="block-button">
                                                        <a href="${pageContext.request.contextPath}/admin/block?student_id=${student.getId()}">
                                                            <fmt:message key="label.table.btn.block"/>
                                                        </a>
                                                    </button>
                                                </c:if>
                                            </td>
                                        </c:if>

                                            <%--If user is teacher--%>
                                        <c:if test="${sessionScope.user.getRole().equals(Role.TEACHER) && course.getTeacherId() == sessionScope.user.getId()}">

                                            <%--If course has already started--%>
                                            <c:if test="${!course.getCourseStatus().equals(CourseStatus.NOT_STARTED)}">
                                                <td class="student-score">
                                                        <%--If course is in progress, than show scores input--%>
                                                    <c:if test="${course.getCourseStatus().equals(CourseStatus.IN_PROGRESS)}">
                                                        <input type="number" min="0" max="${course.getMaxScore()}"
                                                               placeholder="<c:out value="${requestScope.scores.getOrDefault(student.getId(), 0)}/${course.getMaxScore()}"/>"
                                                               name="score_${student.getId()}">
                                                    </c:if>
                                                        <%--If course is completed, then show scores--%>
                                                    <c:if test="${course.getCourseStatus().equals(CourseStatus.COMPLETED)}">
                                                        <span>
                                                                <c:out value="${requestScope.scores.getOrDefault(student.getId(), 0)}"/>/<c:out value="${course.getMaxScore()}"/>
                                                        </span>
                                                    </c:if>
                                                </td>
                                            </c:if>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </table>

                                <%--If it is a teacher, then show button to save scores--%>
                            <c:if test="${sessionScope.user.getRole().equals(Role.TEACHER) && course.getTeacherId() == sessionScope.user.getId()}">
                                <c:if test="${course.getCourseStatus().equals(CourseStatus.IN_PROGRESS)}">
                                    <button class="btn" type="submit">
                                        <fmt:message key="label.table.btn.save_scores"/>
                                    </button>
                                </c:if>
                            </c:if>
                        </form>
                    </c:if>

                    <c:if test="${requestScope.students.isEmpty()}">
                        <div class="no-students"></div>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>

    <c:if test="${sessionScope.user != null}">
        <div class="action-block">
            <div class="container">
                <div class="status-box">
                    <h3>
                        <fmt:message key="label.action_row_status"/>
                    </h3>
                    <h3>
                        <c:out value="${course.getCourseStatus().getStatus()}"/>
                    </h3>
                </div>

                <c:if test="${sessionScope.user.getRole().equals(Role.STUDENT) && course.getCourseStatus().equals(CourseStatus.COMPLETED)}">
                    <div class="score-box">
                        <h3>
                            <fmt:message key="label.action_row_score"/>
                        </h3>
                        <h3>
                            <c:out value="${requestScope.student_course.getScore()}/${course.getMaxScore()}"/>
                        </h3>
                    </div>
                </c:if>

                <c:if test="${sessionScope.user.getRole().equals(Role.ADMIN)}">
                    <div class="manage-box">
                        <button class="manage-btn edit-btn">
                            <a href="${pageContext.request.contextPath}/admin/course/edit?course_id=${course.getId()}">
                                <fmt:message key="label.action_row_edit_btn"/>
                            </a>
                        </button>
                        <button class="manage-btn delete-btn">
                            <a href="${pageContext.request.contextPath}/admin/course/delete?course_id=${course.getId()}">
                                <fmt:message key="label.action_row_delete_btn"/>
                            </a>
                        </button>
                    </div>
                </c:if>
            </div>
        </div>
    </c:if>
</main>
</body>
</html>