<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/new_course/new_course"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <title>
        <fmt:message key="label.title_new_course"/>
    </title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/form.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin/new_course.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <jsp:include page="/WEB-INF/templates/error.jsp"/>

    <div class="title-box">
        <div class="container">
            <div class="title-row">
                <h1><fmt:message key="label.title_new_course"/></h1>
            </div>
        </div>
    </div>

    <div class="container">
        <form action="${pageContext.request.contextPath}/admin/course/new" method="post" enctype="multipart/form-data">
            <input class="form-input" type="text" name="course_title" placeholder="<fmt:message key="label.course_title"/>" autocomplete="off" required>

            <textarea class="form-input"
                      name="course_description"
                      placeholder="<fmt:message key="label.course_description"/>"
                      autocomplete="off"
                      maxlength="512"
            ></textarea>


            <select name="subject_id">
                <option value="" selected disabled hidden>
                    <fmt:message key="label.course_choose_subject"/>
                </option>

                <c:forEach var="subject" items="${requestScope.subjects}">
                    <option value="${subject.getId()}">
                            <c:out value="${subject.getSubject()}"/>
                    </option>
                </c:forEach>
            </select>

            <select name="teacher_id" required>
                <option value="" selected disabled hidden>
                    <fmt:message key="label.course_choose_teacher"/>
                </option>

                <c:forEach var="teacher" items="${requestScope.teachers}">
                    <option value="${teacher.getId()}">
                            <c:out value="${teacher.getFullName()}"/>
                    </option>
                </c:forEach>
            </select>

            <select name="language_id" required>
                <option value="" selected disabled hidden><fmt:message key="label.course_choose_language"/></option>

                <c:forEach var="language" items="${requestScope.languages}">
                    <option value="${language.getId()}">
                            <c:out value="${language.getName()}"/>
                    </option>
                </c:forEach>
            </select>

            <input class="form-input" type="number" name="max_score" min="0" placeholder="<fmt:message key="label.course_max_score"/>" autocomplete="off">

            <input class="form-input" type="datetime-local" name="start_date" required>
            <input class="form-input" type="datetime-local" name="end_date" required>

            <div class="form-row image-input-row">
                <input id="image-input" type="file" name="file" accept="image/*">
                <label id="image-input-label" for="image-input">Choose image...</label>
            </div>

            <button class="form-submit">
                <fmt:message key="label.button_submit"/>
            </button>
        </form>
    </div>
</main>

<footer></footer>
<script src="${pageContext.request.contextPath}/static/js/course_image_input.js"></script>
</body>
</html>