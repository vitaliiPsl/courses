<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/auth/auth"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/auth/register.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/form.css" />

    <script src="https://www.google.com/recaptcha/api.js"></script>
    <title><fmt:message key="label.log_in"/></title>
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main class="background" style="background-image: url('${pageContext.request.contextPath}/static/images/1.jpg')">
    <jsp:include page="/WEB-INF/templates/error.jsp"/>

    <div class="container">
        <div class="log-in-box">
            <h1><fmt:message key="label.log_in"/></h1>

            <form action="${pageContext.request.contextPath}/auth/log_in" method="post">

                <div class="form-row">
                    <input type="email" name="email" placeholder="<fmt:message key="label.email"/>" class="form-input" required/>
                </div>

                <div class="form-row">
                    <input type="password" name="password" placeholder="<fmt:message key="label.password"/>" class="form-input" required/>
                </div>

                <div class="form-row">
                    <div class="g-recaptcha recaptcha" data-sitekey="6LeZGkUfAAAAAA81FghGqs59eCpkuUdUM2egGdJi"></div>
                </div>

                <button class="form-submit"><fmt:message key="label.log_in"/></button>
            </form>

            <div class="question-block">
                <a class="question" href="${pageContext.request.contextPath}/auth/sign_up">
                    <fmt:message key="label.do_not_have_account"/>
                </a>
            </div>
        </div>
    </div>
</main>

<footer></footer>
</body>

</html>
