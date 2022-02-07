<%@ page import="java.util.List" %>
<%@ page import="com.example.courses.persistence.entity.Course" %>
<%@ page import="com.example.courses.persistence.entity.User" %>
<%@ page import="com.example.courses.persistence.entity.Role" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.example.courses.persistence.entity.CourseStatus" %>
<%@ page import="com.example.courses.persistence.postgres.StudentCourse" %>
<%@ page import="com.example.courses.DTO.CourseDTO" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/table.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <%
        User user = (User) session.getAttribute("user");
        CourseDTO courseDTO = (CourseDTO) request.getAttribute("course");
        StudentCourse studentCourse = (StudentCourse) request.getAttribute("student_course");
        Map<Long, Integer> scores = (Map<Long, Integer>) request.getAttribute("scores");
        Course course = courseDTO.getCourse();
        User teacher = courseDTO.getTeacher();
        List<User> studentList = courseDTO.getStudents();
    %>

    <div class="content-block">
        <div class="container">
            <div class="content">
                <div class="info-block">
                    <h1 class="course-name"><%= course.getTitle()%>
                    </h1>
                    <h3 class="course-subject"><%= course.getSubject()%>
                    </h3>
                    <p class="course-description">
                        <%= course.getDescription()%>
                    </p>
                    <p class="teacher">Teacher:
                        <span><%=teacher.getFullName()%></span>
                    </p>

                    <%
                        if (user == null || user.getRole().equals(Role.STUDENT)) {
                    %>
                        <div class="enroll-block">
                            <%
                                if(studentCourse == null){
                            %>
                            <form action="${pageContext.request.contextPath}/enroll?course_id=<%=course.getId()%>"
                                  method="post">
                                <button type="submit" class="enroll-btn">
                                    <span>Enroll</span>
                                    <span>Starts: <%=course.getStartDate().toLocalDate()%></span>
                                </button>
                            </form>

                            <%
                                } else {
                            %>
                                <div class="enrolled">
                                    <span>Enrolled</span>
                                </div>
                            <%
                                }
                            %>
                            <div class="enrolled-count">
                                <span class="enrolled-span">Number of students: </span>
                                <span> <%=studentList.size()%> </span>
                            </div>
                        </div>
                    <%
                        }
                    %>
                </div>

                <div class="image-block">
                    <div class="image-wrapper">
                        <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt=""/>
                    </div>
                </div>
            </div>

            <%
                if (user != null && user.getRole().equals(Role.TEACHER)) {
            %>
            <div class="students-block">
                <h1>Students</h1>
                <%
                    if (!studentList.isEmpty()) {
                %>
                <form action="${pageContext.request.contextPath}/teacher/course/score?course_id=<%=course.getId()%>"
                      method="post">
                    <table class="students-table">
                        <tr>
                            <th class="first-name-header">First name</th>
                            <th class="last-name-header">Last name</th>
                            <th class="score-header">Score</th>
                        </tr>

                        <%
                            for (User student : studentList) {
                        %>
                        <tr>
                            <td class="first-name">
                                <%= student.getFirstName()%>
                            </td>
                            <td class="last-name">
                                <%= student.getLastName()%>
                            </td>
                            <td class="student-score">
                                <input type="number" min="0" max="<%=course.getMaxScore()%>"
                                       placeholder="<%=scores.getOrDefault(student.getId(), 0)%>"
                                       name="score_<%=student.getId()%>">
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </table>

                    <button class="btn" type="submit">Save scores</button>
                </form>
                <%
                } else {
                %>
                <div class="no-students">
                    <h3>No one takes this course</h3>
                </div>
                <%
                    }
                %>
            </div>
            <%
                }
            %>

        </div>
    </div>

    <%
        if (user != null) {
    %>
    <div class="action-block">
        <div class="container">
            <div class="status-box">
                <h3>Status:</h3>
                <h3><%=course.getCourseStatus().getStatus()%>
                </h3>
            </div>
            <%
                if (user.getRole().equals(Role.STUDENT) && course.getCourseStatus().equals(CourseStatus.COMPLETED)) {
            %>
            <div class="score-box">
                <h3>Score: </h3>
                <h3><%=studentCourse.getScore()%>/<%=course.getMaxScore()%></h3>
            </div>
            <%
            } else if (user.getRole().equals(Role.ADMIN)) {
            %>
            <div class="manage-box">
                <button class="manage-btn edit-btn">
                    <a href="${pageContext.request.contextPath}/admin/course/edit?course_id=<%=course.getId()%>">
                        Edit
                    </a>
                </button>
                <button class="manage-btn delete-btn">
                    <a href="${pageContext.request.contextPath}/admin/course/delete?course_id=<%=course.getId()%>">
                        Delete
                    </a>
                </button>
            </div>
            <%
                    }
            %>
        </div>
    </div>
    <%
        }
    %>
</main>
</body>
</html>