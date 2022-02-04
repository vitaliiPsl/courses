<%@ page import="java.util.*" %>
<%@ page import="com.example.courses.persistence.entity.User" %>
<%@ page import="com.example.courses.utils.TimeUtils" %>
<%@ page import="com.example.courses.DTO.CourseDTO" %>
<%@ page import="com.example.courses.persistence.entity.Course" %>
<%@ page import="com.example.courses.persistence.entity.Language" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Courses</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_list.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_row.css">
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <%
        List<CourseDTO> courseDTOList = (List<CourseDTO>) request.getAttribute("courses");
    %>

    <div class="content-box">
        <div class="container">
            <%
                for (CourseDTO courseDTO : courseDTOList) {
                    Course course = courseDTO.getCourse();
                    Language language = courseDTO.getLanguage();
                    User teacher = courseDTO.getTeacher();
            %>
            <div class="course">
                <div class="img-box">
                    <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt=""/>
                </div>
                <div class="info-box">
                    <div class="info-row">
                        <a href="">
                            <h2><%=course.getTitle()%>
                            </h2>
                        </a>
                    </div>
                    <div class="info-row">
                        <p class="subject-row">Subject:</p>
                        <span class="subject"><%=course.getSubject()%></span>
                    </div>
                    <div class="info-row">
                        <p class="teacher-row">Teacher:</p>
                        <span class="teacher"><%=teacher.getFullName()%></span>
                    </div>
                    <div class="info-row">
                        <p class="language-row">Language:</p>
                        <span class="language"><%=language.getName()%></span>
                    </div>
                    <div class="info-row">
                        <p class="duration-row">Duration:</p>
                        <span class="duration">
                        <%= TimeUtils.calculateDuration(course.getStartDate(), course.getEndDate())%>
                    </span>
                    </div>
                </div>
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

</body>
</html>