<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/auth/auth"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/register.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/form.css"/>
    <title><fmt:message key="label.sign_up"/></title>
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main class="background"
      style="background-image: url('${pageContext.request.contextPath}/static/images/1.jpg')">

    <jsp:include page="/WEB-INF/templates/error.jsp"/>

    <div class="container">
        <div class="log-in-box">
            <h1><fmt:message key="label.sign_up"/></h1>

            <form action="${pageContext.request.contextPath}/auth/sign_up" method="post">

                <div class="form-group">
                    <input type="text" name="first_name" placeholder="<fmt:message key="label.first_name"/>" class="form-input" required/>
                    <input type="text" name="last_name" placeholder="<fmt:message key="label.last_name"/>" class="form-input" required/>
                </div>

                <div class="form-row">
                    <input type="email" name="email" placeholder="<fmt:message key="label.email"/>" class="form-input" required/>
                </div>

                <div class="form-row">
                    <input id="password" type="password" name="password" placeholder="<fmt:message key="label.password"/>" class="form-input" required/>
                </div>
                <div class="form-row">
                    <input id="repeat-password" type="password" placeholder="<fmt:message key="label.repeat_password"/>" class="form-input" required/>
                    <p class="password-message hidden"><fmt:message key="label.repeat_password_message"/></p>
                </div>

                <button type="submit" class="form-submit"><fmt:message key="label.sign_up"/></button>
            </form>

            <div class="question-block">
                <a class="question" href="${pageContext.request.contextPath}/auth/log_in">
                    <fmt:message key="label.already_have_account"/>
                </a>
            </div>
        </div>
    </div>
</main>

<footer></footer>
<script src="${pageContext.request.contextPath}/static/js/confirm_password.js"></script>
</body>

</html>