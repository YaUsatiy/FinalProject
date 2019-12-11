<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.local}" scope="session"/>
<fmt:setBundle basename="locale.pagecontent" var="locale"/>

<fmt:message bundle="${locale}" key="client_no_image" var="client_no_image"/>
<fmt:message bundle="${locale}" key="status_updated" var="status_updated"/>
<fmt:message bundle="${locale}" key="block_user" var="block_user"/>
<fmt:message bundle="${locale}" key="unblock_user" var="unblock_user"/>
<fmt:message bundle="${locale}" key="show_profile" var="show_profile"/>
<fmt:message bundle="${locale}" key="no_registered_clients" var="no_registered_clients"/>
<fmt:message bundle="${locale}" key="client_search_name" var="client_search_name"/>
<fmt:message bundle="${locale}" key="client_search_surname" var="client_search_surname"/>
<fmt:message bundle="${locale}" key="client_search_login" var="client_search_login"/>
<fmt:message bundle="${locale}" key="client_search_purchased_number" var="client_search_purchased_number"/>
<fmt:message bundle="${locale}" key="client_search_discount" var="client_search_discount"/>
<fmt:message bundle="${locale}" key="client_search_button" var="client_search_button"/>
<fmt:message bundle="${locale}" key="client_search_form" var="client_search_form"/>
<fmt:message bundle="${locale}" key="footer.copyright" var="footer"/>

<html>
<head>
    <link rel="shortcut icon" href="img/favicon/1.ico"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <title>Clients</title>
</head>
<body class="d-flex flex-column">
<jsp:include page="../menu.jsp">
    <jsp:param name="pageTopic" value="adminClients"/>
    <jsp:param name="currentPage" value="admin_clients"/>
</jsp:include>

<div class="text-center">
    <c:if test="${sessionScope.success eq true}">
        <p class="text-success">${status_updated}</p>
        <c:remove var="success" scope="session" />
    </c:if>
</div>

<button class="btn btn-primary mx-5 mb-2" data-toggle="collapse" data-target="#hide">${client_search_form}</button>
<div class="collapse" id="hide">
    <form method="post" class="text-center border border-light px-5" action="${pageContext.servletContext.contextPath}/controller?command=find_clients_by_filter">
        <input type="text" class="form-control mb-4" name="name" value="${param.name}" placeholder="${client_search_name}" title="${client_search_name}"/>
        <input type="text" class="form-control mb-4" name="surname" value="${param.surname}" placeholder="${client_search_surname}" title="${client_search_surname}"/>
        <input type="text" class="form-control mb-4" name="login" value="${param.login}" placeholder="${client_search_login}" title="${client_search_login}"/>
        <input type="text" class="form-control mb-4" name="membership_purchased_number" value="${param.membership_purchased_number}" placeholder="${client_search_purchased_number}" title="${client_search_purchased_number}"/>
        <input type="text" class="form-control mb-4" name="personal_discount" value="${param.personal_discount}" placeholder="${client_search_discount}" title="${client_search_discount}"/>
        <input type="submit" class="btn btn-success my-4 btn-block" value="${client_search_button}">
    </form>
</div>

<c:choose>
    <c:when test="${fn:length(all_clients) eq 0}">
        <h3><c:out value="${no_registered_clients}"/></h3>
    </c:when>
    <c:otherwise>
        <c:forEach items="${all_clients}" var="client">
            <div class="media border p-3">
                <c:choose>
                    <c:when test="${not empty client.image}">
                        <img src="data:image/jpg;base64,${client.image}" alt="${client_no_image}" class="mr-3 mt-3 rounded-circle" style="width:60px;">
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-user-circle mr-3 mt-3" style="font-size: 55px"></i>
                    </c:otherwise>
                </c:choose>
                <div class="media-body">
                    <h4><c:out value="${client.name} ${client.surname} (login : ${client.login})"/></h4>
                        <div class="row">
                            <div class="col">
                                <form action="${pageContext.request.contextPath}/controller?command=change_client_active" method="post">
                                    <input type="hidden" id="admin_client_id" name="admin_client_id" value="${client.id}"/>
                                    <c:if test="${client.active eq true}">
                                        <input type="submit" class="btn btn-danger" value="${block_user}"/>
                                    </c:if>
                                    <c:if test="${client.active eq false}">
                                        <input type="submit" class="btn btn-success" value="${unblock_user}"/>
                                    </c:if>
                                </form>
                            </div>
                            <div class="col">
                                <form action="${pageContext.request.contextPath}/controller?command=client_profile" method="post">
                                    <input type="hidden" name="admin_client_id" value="${client.id}" />
                                    <input type="submit" class="btn btn-info"  value="${show_profile}">
                                </form>
                            </div>
                        </div>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

<footer class="footer mt-auto py-3">
    <div class="container text-center">
        <span class="text-muted">${footer}</span>
    </div>
</footer>
</body>
</html>
