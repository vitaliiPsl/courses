<%@ page import="java.util.List" %>
<%@ page import="com.example.courses.persistence.entity.User" %>
<%@ page import="com.example.courses.persistence.entity.Language" %>
<%@ page import="com.example.courses.persistence.entity.Course" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/form.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin/new_course.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <%
        Course course = (Course) request.getAttribute("course");
        List<User> teacherList = (List<User>) request.getAttribute("teachers");
        List<Language> languageList = (List<Language>) request.getAttribute("languages");
    %>

    <div class="title-box">
        <div class="container">
            <div class="title-row">
                <h1>Edit course</h1>
            </div>
        </div>
    </div>

    <div class="container">
        <form action="${pageContext.request.contextPath}/admin/course/edit?course_id=<%=course.getId()%>" method="post">
            <input class="form-input" type="text" name="course_title" value="<%=course.getTitle()%>" placeholder="Title"
                   autocomplete="off" required>
            <input class="form-input" type="text" name="course_subject" value="<%=course.getSubject()%>"
                   placeholder="Subject" autocomplete="off"
                   required>

            <textarea class="form-input"
                      name="course_description"
                      placeholder="Description"
                      autocomplete="off"
            ><%=course.getDescription()%></textarea>

            <select name="teacher_id" required>
                <option value="" disabled hidden>Choose teacher</option>
                <%
                    for (User teacher : teacherList) {
                        if (teacher.getId() == course.getTeacherId()) {
                %>
                <option value="<%=teacher.getId()%>" selected>
                    <%=teacher.getFullName()%>
                </option>
                <%
                } else {
                %>
                <option value="<%=teacher.getId()%>">
                    <%=teacher.getFullName()%>
                </option>
                <%
                        }
                    }
                %>
            </select>

            <select name="language_id" required>
                <option value="" disabled hidden>Choose language</option>
                <%
                    for (Language language : languageList) {
                        if (language.getId() == course.getLanguageId()) {
                %>
                <option value="<%=language.getId()%>" selected>
                    <%=language.getName()%>
                </option>
                <%
                } else {
                %>
                <option value="<%=language.getId()%>">
                    <%=language.getName()%>
                </option>
                <%
                        }
                    }
                %>
            </select>

            <input class="form-input" type="number" name="max_score" value="<%=course.getMaxScore()%>" min="0"
                   placeholder="Max score" autocomplete="off">

            <input class="form-input" type="datetime-local" name="start_date" value="<%=course.getStartDate()%>">
            <input class="form-input" type="datetime-local" name="end_date" value="<%=course.getEndDate()%>">

            <button class="form-submit">Submit</button>
        </form>
    </div>
</main>

<footer>

</footer>
</body>
</html>