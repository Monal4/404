<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />
<html>

<h1>Welcome to the admin page: Login Here</h1>
<section>

	<form action="loginAdmin.html" method="POST">
    <input type="hidden" name="action" value="loginAdmin.html">        
    <label class="pad_top">First Name:</label>
    <input type="text" name="firstName" value="${firstName}"><br>
    <label class="pad_top">Password:</label>
    <input type="text" name="password" value="${password}"><br>        
    <label>&nbsp;</label>
    <input type="submit" value="login" class="margin_left">
    </form>

</section>
