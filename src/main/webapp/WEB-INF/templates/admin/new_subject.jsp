<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/new_subject/new_subject"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <title>
        <fmt:message key="label.title"/>
    </title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/form.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin/new_course.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin/new_subject.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <jsp:include page="/WEB-INF/templates/error.jsp"/>

    <div class="title-box">
        <div class="container">
            <div class="title-row">
                <h1><fmt:message key="label.title"/></h1>
            </div>
        </div>
    </div>

    <div class="container">
        <form action="${pageContext.request.contextPath}/admin/subject/new" method="post">

            <c:forEach var="lang" items="${requestScope.languages}">
                <div class="form-group subject-group">
                    <input class="form-input" type="text" name="subject_${lang.getLanguageCode()}"
                           placeholder="<fmt:message key="label.subject_placeholder"/>"
                           autocomplete="off" ${lang.isDefault() ? 'required' : ''}>
                    <span class="language-span">${lang.getLanguageCode()}</span>
                </div>
            </c:forEach>

            <button class="form-submit"><fmt:message key="label.submit_btn"/></button>
        </form>
    </div>
</main>

<footer></footer>
<script src="${pageContext.request.contextPath}/static/js/course_image_input.js"></script>
</body>
</html>