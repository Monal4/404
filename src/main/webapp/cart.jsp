<jsp:include page="/includes/header.jsp" />

    <table class="table">
      	<thead>
			<tr class="tr tr-success">
				<th>Id</th>
				<th>Product Description</th>
				<th>ProductQuantity</th> 
			</tr>
	  	</thead>
	  
	  	<c:forEach items="${products}" var="p">
        	<tr>
				<td scope="col">${p.productId}</td>
				<td scope="col">${p.description}</td>
				<td scope="col">${p.quantity}</td>
			</tr>
		</c:forEach>
	</table>

<a href="catalog.html"> continue shopping!</a>

<jsp:include page="/includes/footer.jsp" />
