<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Resultados SQL</title>
<style>
body {
    background-color: #ffff9d;
}

h1 {
    color: #00007e;
    text-align: center;
}

hr {
    color: #00007e;
}

p {
    color: #00007e;
}

strong {
    color: #FF0000;
}

table {
    border-collapse: collapse;
    width: 100%;
}

th, td {
    border: 1px solid black;
    padding: 8px;
    text-align: left;
}

th {
    background-color: #f2f2f2;
}

</style>
</head>
<body>

	<h2>Resultados de la consulta</h2>

	<c:choose>
		<c:when test="${empty data}">
			<p style="color: blue;">No se encontraron resultados.</p>
		</c:when>
		<c:otherwise>
			<table>
				<tr>
					<c:forEach var="column" items="${data[0].keySet()}">
						<th>${column}</th>
					</c:forEach>
				</tr>
				<c:forEach var="row" items="${data}">
					<tr>
						<c:forEach var="value" items="${row.values()}">
							<td>${value}</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>

</body>
</html>
