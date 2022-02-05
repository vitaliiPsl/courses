<%@ page import="java.util.List" %>
<%@ page import="com.example.courses.persistence.entity.Course" %>
<%@ page import="com.example.courses.persistence.entity.User" %>
<%@ page import="com.example.courses.persistence.entity.Role" %>
<%@ page import="com.example.courses.persistence.entity.CourseStatus" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
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
    <%
        List<Course> courseList = (List<Course>) request.getAttribute("courses");
        User user = (User) session.getAttribute("user");
    %>

    <div class="status-filter-box">
        <div class="container">
            <div class="status-filters">
                <button data-status="" class="status-filter selected">All courses</button>
                <button data-status="not started" class="status-filter">Not started yet</button>
                <button data-status="in progress" class="status-filter">In progress</button>
                <button data-status="completed" class="status-filter">Completed</button>
            </div>
        </div>
    </div>

    <div class="content-box">
        <div class="container">
            <%
                for (Course course : courseList) {
            %>
            <div class="course" data-status="<%=course.getCourseStatus().getStatus()%>">
                <div class="img-box">
                    <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt=""/>
                </div>
                <div class="info-box">
                    <div class="info-row">
                        <a href="${pageContext.request.contextPath}/course?course_id=<%=course.getId()%>">
                            <h2><%=course.getTitle()%>
                            </h2>
                        </a>
                    </div>
                    <div class="info-row">
                        <p class="subject-row">Subject:</p>
                        <span class="subject"><%=course.getSubject()%></span>
                    </div>
                    <div class="info-row">
                        <p class="status-row">Status:</p>
                        <span class="status">
                            <%=course.getCourseStatus().getStatus()%>
                        </span>
                    </div>
                </div>

                <%
                    if (user.getRole().equals(Role.STUDENT)) {
                        Map<Long, Integer> scores = (Map<Long, Integer>) request.getAttribute("scores");
                        if (course.getCourseStatus().equals(CourseStatus.COMPLETED)) {
                %>
                            <div class="score-box">
                                <h3>Score: </h3>
                                <h3>
                                    <%=scores.getOrDefault(course.getId(), 0)%> / <%=course.getMaxScore()%>
                                </h3>
                            </div>
                <%
                        }
                    }
                %>
            </div>
            <%
                }
            %>
        </div>
    </div>
</main>

<footer>
    <div class="container"></div>
</footer>

<script src="${pageContext.request.contextPath}/static/js/user_courses.js"></script>
</body>
</html>