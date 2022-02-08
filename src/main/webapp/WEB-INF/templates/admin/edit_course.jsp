<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <c:set var="course" value="${requestScope.course}"/>
    <div class="title-box">
        <div class="container">
            <div class="title-row">
                <h1>Edit course</h1>
            </div>
        </div>
    </div>

    <div class="container">
        <form action="${pageContext.request.contextPath}/admin/course/edit?course_id=${course.getId()}" method="post">
            <input class="form-input" type="text" name="course_title" value="${course.getTitle()}" placeholder="Title"
                   autocomplete="off" required>
            <input class="form-input" type="text" name="course_subject" value="${course.getSubject()}"
                   placeholder="Subject" autocomplete="off"
                   required>

            <textarea class="form-input"
                      name="course_description"
                      placeholder="Description"
                      autocomplete="off"
            >${course.getDescription()}</textarea>

            <select name="teacher_id" required>
                <option value="" disabled hidden>Choose teacher</option>
                <c:forEach var="teacher" items="${requestScope.teachers}">
                    <option value="${teacher.getId()}" ${teacher.getId() == course.getTeacherId() ? 'selected' : ''}>
                            ${teacher.getFullName()}
                    </option>
                </c:forEach>
            </select>

            <select name="language_id" required>
                <option value="" disabled hidden>Choose language</option>
                <c:forEach var="language" items="${requestScope.languages}">
                    <option value="${language.getId()}" ${language.getId() == course.getLanguageId() ? 'selected' : ''}>
                            ${language.getName()}
                    </option>
                </c:forEach>
            </select>

            <input class="form-input" type="number" name="max_score" value="${course.getMaxScore()}" min="0"
                   placeholder="Max score" autocomplete="off">

            <input class="form-input" type="datetime-local" name="start_date" value="${course.getStartDate()}">
            <input class="form-input" type="datetime-local" name="end_date" value="${course.getEndDate()}">

            <button class="form-submit">Submit</button>
        </form>
    </div>
</main>

<footer>

</footer>
</body>
</html>