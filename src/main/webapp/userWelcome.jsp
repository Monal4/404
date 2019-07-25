<!DOCTYPE html>

<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
	Register Now!
</head>
<p align = center>To register for our website, enter your name and email
   address below. Then, click on the Submit button.</p>
<section align=center>
	<form action="Registered.html" method="get">
   <!-- <input type="hidden" name="action" value="registerUser">  -->       
    <label class="pad_top">Email:</label>
    <input type="email" name="email" value="${email}"><br>
    <label class="pad_top">First Name:</label>
    <input type="text" name="firstName" value="${firstName}"><br>
    <label class="pad_top">Last Name:</label>
    <input type="text" name="lastName" value="${lastName}"><br>       
    <label>&nbsp;</label>
    <input type="submit" value="Register" class="margin_left">
</form>

</section>
<jsp:include page="/includes/footer.jsp" />