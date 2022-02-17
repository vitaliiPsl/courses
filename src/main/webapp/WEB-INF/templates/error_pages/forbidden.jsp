<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/error_page/error_page"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/error_page/error_page.css">
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

    <main>
        <div class="container">
                <span class="code">403</span>
                <h1 class="message"><fmt:message key="label.forbidden"/></h1>
                <a href="${pageContext.request.contextPath}/" class="home_link"><fmt:message key="label.home_page"/></a>
        </div>
    </main>
</body>
</html>
