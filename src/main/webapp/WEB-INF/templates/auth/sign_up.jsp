<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/register.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/form.css"/>
    <title>Sign Up</title>
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main class="background"
      style="background-image: url('${pageContext.request.contextPath}/static/images/2.jpg')">

    <div class="container">
        <jsp:include page="/WEB-INF/templates/error.jsp"/>

        <div class="log-in-box">
            <h1>Sign Up</h1>

            <form action="${pageContext.request.contextPath}/auth/sign_up" method="post">

                <div class="form-group">
                    <input type="text" name="first_name" placeholder="First Name" class="form-input" required/>
                    <input type="text" name="last_name" placeholder="Last Name" class="form-input" required/>
                </div>

                <div class="form-row">
                    <input type="email" name="email" placeholder="E-mail" class="form-input" required/>
                </div>

                <div class="form-row">
                    <input type="password" name="password" placeholder="Password" class="form-input" required/>
                </div>
                <div class="form-row">
                    <input type="password" placeholder="Repeat Password" class="form-input" required/>
                </div>

                <button class="form-submit">Sign Up</button>
            </form>

            <div class="question-block">
                <a class="question" href="${pageContext.request.contextPath}/auth/log_in">Already have an account?</a>
            </div>
        </div>
    </div>
</main>

<footer></footer>
</body>

</html>