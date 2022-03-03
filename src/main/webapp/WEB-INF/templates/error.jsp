<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="sessionScope.lang">
<body>

<c:if test="${requestScope.error != null}">
    <div class="container">
        <div class="error">
                <c:out value="${requestScope.error}"/>
        </div>
    </div>
</c:if>

</body>
</html>
