<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/auth/register.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/form.css" />
    <title>Log In</title>
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main class="background" style="background-image: url('${pageContext.request.contextPath}/static/images/1.jpg')">
    <div class="container">
        <jsp:include page="/WEB-INF/templates/error.jsp"/>

        <div class="log-in-box">
            <h1>Log In</h1>

            <form action="${pageContext.request.contextPath}/auth/log_in" method="post">

                <div class="form-row">
                    <input type="email" name="email" placeholder="Email" class="form-input" required/>
                </div>

                <div class="form-row">
                    <input type="password" name="password" placeholder="Password" class="form-input" required/>
                </div>

                <button class="form-submit">Log In</button>
            </form>

            <div class="question-block">
                <a class="question" href="${pageContext.request.contextPath}/auth/sign_up">Don't have an account?</a>
            </div>
        </div>
    </div>
</main>

<footer></footer>
</body>

</html>
