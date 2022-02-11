<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie['lang'].value}"/>
<fmt:setBundle basename="i18n/edit_course/edit_course"/>
<!DOCTYPE html>

<html lang="${cookie['lang'].value}">
<head>
    <title>
        <fmt:message key="label.title_edit_course"/>
    </title>
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
                <h1>
                    <fmt:message key="label.title_edit_course"/>
                </h1>
            </div>
        </div>
    </div>

    <div class="container">
        <form action="${pageContext.request.contextPath}/admin/course/edit?course_id=${course.getId()}" method="post">
            <input class="form-input" type="text" name="course_title" value="${course.getTitle()}" placeholder="<fmt:message key="label.course_title"/>"
                   autocomplete="off" required>
            <input class="form-input" type="text" name="course_subject" value="${course.getSubject()}"
                   placeholder="<fmt:message key="label.course_subject"/>" autocomplete="off"
                   required>

            <textarea class="form-input"
                      name="course_description"
                      placeholder="<fmt:message key="label.course_description"/>"
                      autocomplete="off">${course.getDescription()}</textarea>

            <select name="teacher_id" required>
                <option value="" disabled hidden>
                    <fmt:message key="label.course_choose_teacher"/>
                </option>
                <c:forEach var="teacher" items="${requestScope.teachers}">
                    <option value="${teacher.getId()}" ${teacher.getId() == course.getTeacherId() ? 'selected' : ''}>
                            ${teacher.getFullName()}
                    </option>
                </c:forEach>
            </select>

            <select name="language_id" required>
                <option value="" disabled hidden>
                    <fmt:message key="label.course_choose_language"/>
                </option>
                <c:forEach var="language" items="${requestScope.languages}">
                    <option value="${language.getId()}" ${language.getId() == course.getLanguageId() ? 'selected' : ''}>
                            ${language.getName()}
                    </option>
                </c:forEach>
            </select>

            <input class="form-input" type="number" name="max_score" value="${course.getMaxScore()}" min="0"
                   placeholder="<fmt:message key="label.course_max_score"/>" autocomplete="off">

            <input class="form-input" type="datetime-local" name="start_date" value="${course.getStartDate()}">
            <input class="form-input" type="datetime-local" name="end_date" value="${course.getEndDate()}">

            <button class="form-submit">
                <fmt:message key="label.button_submit"/>
            </button>
        </form>
    </div>
</main>

<footer>

</footer>
</body>
</html>