<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie['lang'].value}"/>
<fmt:setBundle basename="i18n/new_user/new_user"/>

<!DOCTYPE html>
<html lang="${cookie['lang'].value}">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/register.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/form.css" />
    <title>
        <fmt:message key="label.new_user"/>
    </title>
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main class="background" style="background-image: url('${pageContext.request.contextPath}/static/images/1.jpg')">
    <div class="container">
        <jsp:include page="/WEB-INF/templates/error.jsp"/>

        <div class="log-in-box">
            <h1>
                <fmt:message key="label.new_user"/>
            </h1>

            <form action="${pageContext.request.contextPath}/admin/new_user" method="post">

                <div class="form-group">
                    <input type="text" name="first_name" placeholder="<fmt:message key="label.first_name"/>" class="form-input" required/>
                    <input type="text" name="last_name" placeholder="<fmt:message key="label.last_name"/>" class="form-input" required/>
                </div>

                <div class="form-group">
                    <select name="role" id="" required>
                        <option value="" selected disabled hidden>
                            <fmt:message key="label.select_role"/>
                        </option>
                        <option value="admin">
                            <fmt:message key="label.role_admin"/>
                        </option>
                        <option value="teacher">
                            <fmt:message key="label.role_teacher"/>
                        </option>
                        <option value="student">
                            <fmt:message key="label.role_student"/>
                        </option>
                    </select>
                </div>

                <div class="form-row">
                    <input type="email" name="email" placeholder="<fmt:message key="label.email"/>" class="form-input" required/>
                </div>

                <div class="form-row">
                    <input type="text" name="password" placeholder="<fmt:message key="label.password"/>" class="form-input" required/>
                </div>

                <button class="form-submit">
                    <fmt:message key="label.button_submit"/>
                </button>
            </form>
        </div>
    </div>
</main>

<footer></footer>
</body>
</html>