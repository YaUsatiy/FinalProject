<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.local}" scope="session"/>
<fmt:setBundle basename="locale.pagecontent" var="locale"/>

<fmt:message bundle="${locale}" key="order.cost" var="cost"/>
<fmt:message bundle="${locale}" key="order.credit_card" var="credit_card"/>
<fmt:message bundle="${locale}" key="order.payment_data" var="payment_data"/>
<fmt:message bundle="${locale}" key="no_orders" var="no_orders"/>
<fmt:message bundle="${locale}" key="end_date" var="end_date"/>

<html>
<head>
    <title>My orders</title>
</head>
<body>
<jsp:include page="../menu.jsp">
    <jsp:param name="pageTopic" value="orders"/>
    <jsp:param name="currentPage" value="clientOrders"/>
</jsp:include>
    <c:choose>
        <c:when test="${fn:length(orders) eq 0}">
            <h3><c:out value="${no_orders}"/></h3>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>${cost}</th>
                    <th>${payment_data}</th>
                    <th>${end_date}</th>
                    <th>${credit_card}</th>
                </tr>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <td>${order.cost}</td>
                        <td >
                            <c:choose>
                                <c:when test="${sessionScope.local eq 'en_US'}">
                                    <div class="col-2">
                                        <fmt:formatDate value="${order.paymentData}" pattern="dd-MM-YYYY HH:mm:ss" />
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-2">
                                        <fmt:formatDate value="${order.paymentData}" pattern="dd.MM.YYYY HH:mm:ss" />
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td >
                            <c:choose>
                                <c:when test="${sessionScope.local eq 'en_US'}">
                                    <div class="col-2">
                                        <fmt:formatDate value="${order.membershipEndDate}" pattern="dd-MM-YYYY HH:mm:ss" />
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-2">
                                        <fmt:formatDate value="${order.membershipEndDate}" pattern="dd.MM.YYYY HH:mm:ss" />
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${fn:substring(order.cardNumber, 0, 4)}  ${fn:substring(order.cardNumber, 4, 6)}**  ****  ${fn:substring(order.cardNumber, 12, 16)}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>
