<!DOCTYPE html>

<jsp:include page="/includes/header.jsp" />
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

   <div class="fluid-container" id="registerationForm">
      <form action="Registered.html" method="get">      
         <label class="pad_top">Email:</label>
         <input type="email" name="email" value="${email}"><br>
         <label class="pad_top">First Name:</label>
         <input type="text" name="firstName" value="${firstName}"><br>
         <label class="pad_top">Last Name:</label>
         <input type="text" name="lastName" value="${lastName}"><br>       
         <label>&nbsp;</label>
         <button class="btn btn-primary" type="submit" class="margin_left">Register/login</button>
      </form>
   </div>

<jsp:include page="/includes/footer.jsp" />