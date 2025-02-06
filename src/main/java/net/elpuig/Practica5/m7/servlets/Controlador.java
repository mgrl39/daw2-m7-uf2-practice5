package net.elpuig.Practica5.m7.servlets;

import net.elpuig.Practica5.m7.enums.Protocol;
import net.elpuig.Practica5.m7.beans.Alumno;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


// 25 de novembre de 2024
@WebServlet("/AlumnoServlet")
public class Controlador extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String order = request.getParameter("order");
	    if (!"Ejecutar".equals(order)) {
	        out.println("INVALID VALUE");
	        return;
	    }
	    
	    String sql = request.getParameter("sql");
	    if (isNullOrEmpty(sql)) {
	        out.println("<p style=\"color: red\">Error: No se proporcionó ninguna consulta SQL</p>");
	        return;
	    }
	    
	    try {
	        List<Map<String, String>> data = Alumno.executeQuery(sql);
	        
	        if ("true".equals(request.getParameter("jstl"))) {
	            request.setAttribute("data", data);
	            request.getRequestDispatcher("index.jsp").forward(request, response);
	        } else  printSql(request, response);
	    } catch (RuntimeException e) {
	        out.println("<p style=\"color: red\">Error al ejecutar la consulta: " + e.getMessage() + "</p>");
	    }
	}
	
	
	private void printSql(HttpServletRequest request, HttpServletResponse response) throws IOException {
		out = response.getWriter();
		String sql = request.getParameter("sql");
		if (isNullOrEmpty(sql)) {
			out.println(webFormatter("<p style=\"color: #FF0000\">Error: <strong style=\"color: #FF0000\">No se proporcionó ninguna consulta SQL</strong></p>", Protocol.GET));
			return;
		}
		try {
			List<Map<String, String>> data = Alumno.executeQuery(sql);
			String htmlTable = "";
			if (data.isEmpty()) {
				htmlTable += "<p style=\"color:#00007e\">No se encontraron resultados.</p>\n";
				out.println(webFormatter(htmlTable, Protocol.GET));
				return ;
			}
			htmlTable += "<table style=\"color:#00007e\" border='1'><tr>";
			for (String columnName : data.get(0).keySet()) {
				htmlTable += "<th>" + columnName + "</th>";
			}
			System.out.printf(htmlTable + '\n');
			htmlTable += "</tr>";
			for (Map<String, String> row : data) {
				htmlTable += "<tr>";
				for (String value : row.values())  {
					htmlTable += "<td>" + value + "</td>";
					System.out.printf("testing " + value + "\n");
				}
				htmlTable += "</tr>";
			}

			htmlTable += "</table>";
			System.out.printf(htmlTable + '\n');
			out.println(webFormatter(htmlTable, Protocol.GET));
		} catch (RuntimeException e) {
			out.println(webFormatter("<p style=\"color:#00007e\">Error al ejecutar la consulta: <strong style=\"color: #FF0000\">" + e.getMessage() + "</strong></p>", Protocol.GET));
		}
	}

	private static String webFormatter(String msg, Protocol p) {
		String style;
		style = "<html><body style=\"background-color:#ffff9d\"><center><h1 style=\"color:#00007e\">";
		style += p == Protocol.GET ? "Usa JDBC para recuperar registros de una tabla" : "Usa JDBC para grabar un registro en una tabla";
		style += "</h1></center><hr style=\"color:#00007e\">";
		style += "<p style=\"color:#00007e\">" + msg + "</p></body></html>";
		return (style);
	}

	private boolean isNullOrEmpty(String value) {
		return (value == null || value.trim().isEmpty());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		out = response.getWriter();
		try {
			String idStr = request.getParameter("id");
			String curso = request.getParameter("curso");
			String nombre = request.getParameter("nombre");
			if (( isNullOrEmpty(idStr) || isNullOrEmpty(curso)) || isNullOrEmpty(nombre)) {
				out.println(webFormatter("Todos los campos son obligatorios", Protocol.POST));
				return ;
			}
			Alumno nuevoAlumno = new Alumno(Integer.parseInt(idStr), curso, nombre);
			out.println(webFormatter(nuevoAlumno.save() ? "Alumno añadido con éxito" : "Error al añadir el alumno", Protocol.POST));
		} catch (NumberFormatException e) {
			out.println(webFormatter("Error: El ID debe ser un número válido", Protocol.POST));
		}
	}
	
	
	
}
