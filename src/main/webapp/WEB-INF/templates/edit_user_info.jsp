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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/edit_user_info.css"/>
    <title>
        <fmt:message key="label.title"/>
    </title>
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main class="background"
      style="background-image: url('${pageContext.request.contextPath}/static/images/1.jpg')">

    <jsp:include page="/WEB-INF/templates/error.jsp"/>

    <div class="container">
        <div class="log-in-box">
            <h1><fmt:message key="label.title"/></h1>

            <form action="${pageContext.request.contextPath}/user/edit" method="post" enctype="multipart/form-data">
                <c:set var="user" value="${sessionScope.user}"/>
                <div class="form-group">
                    <input type="text" name="first_name" value="<c:out value="${user.getFirstName()}"/>" placeholder="<fmt:message key="label.form.first_name"/>" class="form-input" required/>
                    <input type="text" name="last_name" value="<c:out value="${user.getLastName()}"/>" placeholder="<fmt:message key="label.form.last_name"/>" class="form-input" required/>
                </div>

                <div class="form-row">
                    <input type="email" name="email" value="<c:out value="${user.getEmail()}"/>" placeholder="<fmt:message key="label.form.email"/>" class="form-input" required/>
                </div>

                <div class="form-row avatar-block">
                    <img id="avatar-image" src="${pageContext.request.contextPath}/image?image_type=user&image_name=${sessionScope.user.getImageName()}" alt=""/>
                    <input id="avatar-input" type="file" name="file" accept="image/*">
                </div>

                <button class="form-submit"><fmt:message key="label.form.btn_save"/></button>
            </form>

            <div class="question-block">

            </div>
        </div>
    </div>
</main>

<footer></footer>
<script src="${pageContext.request.contextPath}/static/js/profile_image_input.js"></script>
</body>
</html>