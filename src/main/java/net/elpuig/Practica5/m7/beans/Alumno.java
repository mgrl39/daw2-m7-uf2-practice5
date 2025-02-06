package net.elpuig.Practica5.m7.beans;
import net.elpuig.Practica5.m7.jdbc.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Alumno {
    private int id;
    private String curso;
    private String nombre;
    private static String sql;
    private static Connection conn;
    private static List<Alumno> alumnos = new ArrayList<>();

    // 22 de novembre de 2024
    public Alumno(int id, String curso, String nombre) {
        this.id = id;
        this.curso = curso;
        this.nombre = nombre;
    }

    // Método estático para inicializar la conexión (se ejecuta una vez al inicio)
    static {
        Conexion.setURL("jdbc:mysql://172.16.0.25/dbalumnos?user=mp7&password=secreto");
        conn = Conexion.getConexion();
    }

    public int getId() {
        return id;
    }	

    public void setId(int id) {
        this.id = id;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public static List<Alumno> load() {
        sql = "SELECT * FROM alumnos";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String curso = rs.getString("curso");
                String nombre = rs.getString("nombre");
                alumnos.add(new Alumno(id, curso, nombre));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar alumnos: " + e.getMessage());
        }
        return (alumnos);
    }
        
    public static List<Map<String, String>> executeQuery(String sql) {
        List<Map<String, String>> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                result.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }
    
    public boolean save() {
        sql = "INSERT INTO alumnos (id, curso, nombre) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, this.id);
            ps.setString(2, this.curso);
            ps.setString(3, this.nombre);
            return (ps.executeUpdate() > 0);
        } catch (SQLException e) {
            System.err.println("Error al guardar alumno: " + e.getMessage());
            return (false);
        }
    }

}
