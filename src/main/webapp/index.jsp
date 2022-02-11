<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${cookie['lang'].value}"/>
<fmt:setBundle basename="i18n/index/index"/>

<!DOCTYPE html>
<html lang="${cookie['lang'].value}">
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        main{
            justify-content: center;
            align-items: center;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <div class="container">
        <c:if test="${sessionScope.user != null}">
            <h1><fmt:message key="label.welcome"/>, ${user.getFirstName()}</h1>
        </c:if>
        <c:if test="${sessionScope.user == null}">
            <h1><fmt:message key="label.welcome"/></h1>
        </c:if>
    </div>
</main>

<footer>

</footer>
</body>
</html>