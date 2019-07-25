<!DOCTYPE html>
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="styles/main.css" type="text/css"/>
</head>
<section>
<p>
<a href="download?action=showProduct&amp;productCode=8601">
    86 (the band) - <em>True Life Songs and Pictures</em> - (mp3 sample played using HTML5)
</a><br>

<a href="download?action=showProduct&amp;productCode=pf01">
    Paddlefoot - <em>The First CD</em> - (mp3 sample played using HTML5, with servlet URL for mp3 data)
</a><br>

<a href="download?action=showProduct&amp;productCode=pf02">
    Paddlefoot - <em>The Second CD</em> - (mp3 samples played using Murach's page)
</a><br>

<a href="download?action=showProduct&amp;productCode=jr01">
    Joe Rut - <em>Genuine Wood Grained Finish</em> - (mp3 samples played using Murach's page)
</a>
</p>
<li>
	<a href="cart.html">view Cart</a>
</li>

</section>

<jsp:include page="/includes/footer.jsp" />