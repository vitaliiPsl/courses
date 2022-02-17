<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/change_password/change_password"/>

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

      <form action="${pageContext.request.contextPath}/user/password" method="post">
        <div class="form-row">
          <input type="password" name="current_password" placeholder="<fmt:message key="label.form.current_password"/>" class="form-input" required/>
        </div>

        <div class="form-row">
          <input type="password" name="password" placeholder="<fmt:message key="label.form.new_password"/>" class="form-input" required/>
        </div>

        <div class="form-row">
          <input type="password" placeholder="<fmt:message key="label.form.repeat_password"/>" class="form-input" required/>
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