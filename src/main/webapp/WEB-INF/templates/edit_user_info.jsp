<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/edit_user_info/edit_user_info"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/register.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/form.css"/>
    <title><fmt:message key="label.title"/></title>
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main class="background"
      style="background-image: url('${pageContext.request.contextPath}/static/images/1.jpg')">

    <jsp:include page="/WEB-INF/templates/error.jsp"/>

    <div class="container">
        <div class="log-in-box">
            <h1><fmt:message key="label.title"/></h1>

            <form action="${pageContext.request.contextPath}/user/edit" method="post">
                <c:set var="user" value="${sessionScope.user}"/>
                <div class="form-group">
                    <input type="text" name="first_name" value="${user.getFirstName()}" placeholder="<fmt:message key="label.form.first_name"/>" class="form-input" required/>
                    <input type="text" name="last_name" value="${user.getLastName()}" placeholder="<fmt:message key="label.form.last_name"/>" class="form-input" required/>
                </div>

                <div class="form-row">
                    <input type="email" name="email" value="${user.getEmail()}" placeholder="<fmt:message key="label.form.email"/>" class="form-input" required/>
                </div>

                <button class="form-submit"><fmt:message key="label.form.btn_save"/></button>
            </form>

            <div class="question-block">

            </div>
        </div>
    </div>
</main>

<footer></footer>
</body>

</html>