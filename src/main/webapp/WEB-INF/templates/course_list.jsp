<%@ page import="java.util.*" %>
<%@ page import="com.example.courses.persistence.entity.User" %>
<%@ page import="com.example.courses.utils.TimeUtils" %>
<%@ page import="com.example.courses.DTO.CourseDTO" %>
<%@ page import="com.example.courses.persistence.entity.Course" %>
<%@ page import="com.example.courses.persistence.entity.Language" %>
<%@ page import="com.example.courses.persistence.entity.Role" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Courses</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/filter.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_list.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_row.css">
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <%
        List<CourseDTO> courseDTOList = (List<CourseDTO>) request.getAttribute("courses");
    %>

    <div class="filter-box">
        <div class="container">
            <form action="${pageContext.request.contextPath}/courses">

                <div class="search-row">
                    <%
                        String query = (String) request.getAttribute("query");
                        if (query != null) {
                    %>
                    <input type="text" id="search-input" name="query" placeholder="What do you want to learn?" value="<%=query%>"
                           autocomplete="off"/>
                    <%
                    } else {
                    %>
                    <input type="text" name="query" placeholder="What do you want to learn?" autocomplete="off"/>
                    <%
                        }
                    %>
                    <button type="submit" class="search-btn">Search</button>
                </div>

                <div class="filter-row">
                    <span class="filter-by-span">Filter By</span>

                    <div class="filters">
                        <%
                            Map<String, List<String>> filters = (Map<String, List<String>>) request.getAttribute("filters");
                            Map<String, List<String>> appliedFilters = (Map<String, List<String>>) request.getAttribute("applied_filters");

                            for (Map.Entry<String, List<String>> filter : filters.entrySet()) {
                                List<String> applied = appliedFilters.getOrDefault(filter.getKey(), new ArrayList<>());
                        %>

                        <div class="filter">
                            <div class="filter-control">
                                <h5><%=filter.getKey()%>
                                </h5>
                                <div class="arrow"></div>
                            </div>
                            <div class="filter-menu hidden">
                                <%
                                    for (String value : filter.getValue()) {
                                %>
                                <div class="option">
                                    <%
                                        if (applied.contains(value)) {
                                    %>
                                    <input class="filter-checkbox" type="checkbox" name="<%=filter.getKey()%>"
                                           value="<%=value%>" checked/>
                                    <%
                                    } else {
                                    %>
                                    <input class="filter-checkbox" type="checkbox" name="<%=filter.getKey()%>"
                                           value="<%=value%>"/>
                                    <%
                                        }
                                    %>
                                    <label><%=value%></label>
                                </div>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="content-box">
        <div class="container">
            <%
                for (CourseDTO courseDTO : courseDTOList) {
                    Course course = courseDTO.getCourse();
                    Language language = courseDTO.getLanguage();
                    User teacher = courseDTO.getTeacher();
            %>
            <div class="course">
                <div class="img-box">
                    <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt=""/>
                </div>
                <div class="info-box">
                    <div class="info-row">
                        <a href="${pageContext.request.contextPath}/course?course_id=<%=course.getId()%>">
                            <h2><%=course.getTitle()%></h2>
                        </a>
                    </div>
                    <div class="info-row">
                        <p class="subject-row">Subject:</p>
                        <span class="subject"><%=course.getSubject()%></span>
                    </div>
                    <div class="info-row">
                        <p class="teacher-row">Teacher:</p>
                        <span class="teacher"><%=teacher.getFullName()%></span>
                    </div>
                    <div class="info-row">
                        <p class="language-row">Language:</p>
                        <span class="language"><%=language.getName()%></span>
                    </div>
                    <div class="info-row">
                        <p class="duration-row">Duration:</p>
                        <span class="duration">
                        <%= TimeUtils.calculateDuration(course.getStartDate(), course.getEndDate())%>
                    </span>
                    </div>
                </div>

                <%
                    User user = (User) session.getAttribute("user");
                    if (user != null && user.getRole().equals(Role.ADMIN)) {
                %>
                <div class="manage-box">
                    <button class="manage-btn edit-btn">
                        <a href="${pageContext.request.contextPath}/admin/course/edit?course_id=<%=course.getId()%>">
                            Edit
                        </a>
                    </button>
                    <button class="manage-btn delete-btn">
                        <a href="${pageContext.request.contextPath}/admin/course/delete?course_id=<%=course.getId()%>">
                            Delete
                        </a>
                    </button>
                </div>
                <%
                    }
                %>
            </div>
            <%
                }
            %>
        </div>
    </div>
</main>

<footer>
    <div class="container"></div>
</footer>

<script src="${pageContext.request.contextPath}/static/js/course_list.js"></script>
</body>
</html>