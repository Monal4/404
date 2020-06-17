<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />
	<section>
		<h1>
		    Welcome ${firstName}
			Verification Successful!
		</h1>
		<br>

		<form action="initializeDB.html" method="POST">
			<input type="submit" value="Initialize DataBase">
		</form>
	    <br>
	
		<ul>
			<li><a href="processInvoice" >Process Invoice's</a></li>
			<li><a href="ShowReport.html">Show Report</a></li>
			<li><a href="../logout.html">Logout</a></li>
	    </ul>
	</section>

<a href="AdminPage.html">Home</a>
</section>
