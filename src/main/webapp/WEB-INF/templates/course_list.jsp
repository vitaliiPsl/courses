<%@ page import="com.example.courses.utils.TimeUtils" %>
<%@ page import="com.example.courses.persistence.entity.Role" %>
<%@ page import="com.example.courses.service.CourseSortingService" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Courses 👀</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/filter.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_list.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_row.css">
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <div class="filter-box">
        <div class="container">
            <form action="${pageContext.request.contextPath}/courses">
                <div class="search-row">
                    <input type="text" id="search-input" name="query"
                           data-request-url="${pageContext.request.contextPath}/courses/search?query="
                           data-course-url="${pageContext.request.contextPath}/course?course_id="
                           placeholder="What do you want to learn?" value="${requestScope.query}"
                           autocomplete="off"/>
                    <button type="submit" class="search-btn">Search</button>
                    <div class="hints-block"></div>
                </div>

                <div class="filter-row">
                    <div class="filter-block">
                        <span class="filter-by-span">Filter By</span>
                        <div class="filters">
                            <c:forEach var="filter" items="${requestScope.filters.entrySet()}">
                                <div class="filter">
                                    <div class="filter-control">
                                        <h5>${filter.getKey()}</h5>
                                        <div class="arrow"></div>
                                    </div>
                                    <div class="filter-menu hidden">
                                        <c:forEach var="value" items="${filter.getValue()}">
                                            <c:set var="checked" value="${requestScope.applied_filters.get(filter.getKey()).contains(value)}"/>
                                            <div class="option">
                                                <input class="filter-checkbox" type="checkbox" name="${filter.getKey()}"
                                                       value="${value}"
                                                    ${checked ? 'checked' : ''}/>
                                                <label>${value}</label>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="sorting-block">
                        <span class="sort-by-span">Sort By</span>
                        <div class="sorting">

                            <div class="filter sort">
                                <div class="filter-control sort">
                                    <h5>Sort By</h5>
                                    <div class="arrow"></div>
                                </div>
                                <div class="sort-menu hidden">
                                    <c:forEach var="option" items="${requestScope.sorting_options}">
                                        <c:set var="selected" value="${option.equals(requestScope.applied_sorting)}"/>
                                        <div class="option sort-option ${selected ? 'selected' : ''}">
                                            <input class="filter-checkbox" type="checkbox"
                                                   name="<%=CourseSortingService.REQUEST_PARAMETER_SORTING%>"
                                                   value="${option}" ${selected ? 'checked' : ''}
                                                   hidden
                                            >
                                            <label>${option}</label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>

                            <div class="filter order">
                                <div class="filter-control">
                                    <h5>Order</h5>
                                    <div class="arrow"></div>
                                </div>
                                <div class="sort-menu hidden">
                                    <c:forEach var="option" items="${requestScope.sorting_order_options}">
                                        <c:set var="selected" value="${option.equals(requestScope.applied_sorting_order)}"/>
                                        <div class="option order-option ${selected ? 'selected' : ''}">
                                            <input class="filter-checkbox" type="checkbox"
                                                   name="<%=CourseSortingService.REQUEST_PARAMETER_ORDER%>"
                                                   value="${option}" ${selected ? 'checked' : ''}
                                                   hidden>
                                            <label>${option}</label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="content-box">
        <div class="container">
            <c:forEach var="courseDTO" items="${requestScope.courses}">
                <c:set var="course" value="${courseDTO.getCourse()}"/>
                <c:set var="language" value="${courseDTO.getLanguage()}"/>
                <c:set var="teacher" value="${courseDTO.getTeacher()}"/>
                <c:set var="students" value="${courseDTO.getStudents()}"/>

                <div class="course">
                    <div class="img-box">
                        <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt=""/>
                    </div>
                    <div class="info-box">
                        <div class="title-row">
                            <a href="${pageContext.request.contextPath}/course?course_id=${course.getId()}">
                                <h2>${course.getTitle()}</h2>
                            </a>
                        </div>
                        <div class="info-row">
                            <div class="info">
                                <p class="subject-row">Subject:</p>
                                <span class="subject">${course.getSubject()}</span>
                            </div>
                        </div>
                        <div class="info-row">
                            <div class="info">
                                <p class="teacher-row">Teacher:</p>
                                <span class="teacher">${teacher.getFullName()}</span>
                            </div>
                            <div class="info">
                                <p class="language-row">Language:</p>
                                <span class="language">${language.getName()}</span>
                            </div>
                        </div>
                        <div class="info-row">
                            <div class="info">
                                <p class="duration-row">Duration:</p>
                                <span class="duration">
                                        ${TimeUtils.calculateDuration(course.getStartDate(), course.getEndDate())}
                                </span>
                            </div>
                            <div class="info">
                                <p class="number-of-students-row">Students:</p>
                                <span class="number_of_students">${students.size()}</span>
                            </div>
                        </div>
                        <div class="info-row">
                        </div>
                    </div>

                    <c:if test="${sessionScope.user.getRole().equals(Role.ADMIN)}">
                        <div class="manage-box">
                            <button class="manage-btn edit-btn">
                                <a href="${pageContext.request.contextPath}/admin/course/edit?course_id=${course.getId()}">
                                    Edit
                                </a>
                            </button>
                            <button class="manage-btn delete-btn">
                                <a href="${pageContext.request.contextPath}/admin/course/delete?course_id=${course.getId()}">
                                    Delete
                                </a>
                            </button>
                        </div>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </div>
</main>

<footer>
    <div class="container"></div>
</footer>

<script src="${pageContext.request.contextPath}/static/js/course_list.js"></script>
<script src="${pageContext.request.contextPath}/static/js/course_search.js"></script>
</body>
</html>