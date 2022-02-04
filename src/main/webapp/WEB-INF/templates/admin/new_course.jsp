<%@ page import="com.example.courses.persistence.entity.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.courses.persistence.entity.Language" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>New Course</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/form.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin/new_course.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <%
        List<User> teacherList = (List<User>) request.getAttribute("teachers");
        List<Language> languageList = (List<Language>) request.getAttribute("languages");
    %>

    <div class="title-box">
        <div class="container">
            <div class="title-row">
                <h1>New course</h1>
            </div>
        </div>
    </div>

    <div class="container">
        <form action="${pageContext.request.contextPath}/admin/course/new" method="post">
            <input class="form-input" type="text" name="course_title" placeholder="Title" autocomplete="off" required>
            <input class="form-input" type="text" name="course_subject" placeholder="Subject" autocomplete="off" required>

            <textarea class="form-input"
                      name="course_description"
                      placeholder="Description"
                      autocomplete="off"
            ></textarea>

            <select name="teacher_id" required>
                <option value="" selected disabled hidden>Choose teacher</option>
                <%
                    for(User teacher: teacherList){
                %>
                    <option  value="<%=teacher.getId()%>"><%=teacher.getFullName()%></option>
                <%
                    }
                %>
            </select>

            <select name="language_id" required>
                <option value="" selected disabled hidden>Choose language</option>
                <%
                    for(Language language: languageList){
                %>
                    <option  value="<%=language.getId()%>"><%=language.getName()%></option>
                <%
                    }
                %>
            </select>

            <input class="form-input" type="number" name="max_score" min="0" placeholder="Max score" autocomplete="off">

            <input class="form-input" type="datetime-local" name="start_date" required>
            <input class="form-input" type="datetime-local" name="end_date" required>

            <button class="form-submit">Submit</button>
        </form>
    </div>
</main>

<footer>

</footer>
</body>
</html>