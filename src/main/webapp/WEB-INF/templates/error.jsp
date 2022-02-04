<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<body>
<%
    String error = (String)request.getAttribute("error");
    if(error != null){
%>
    <div class="error">
        <%=error%>
    </div>
<%
    }
%>
</body>
</html>
