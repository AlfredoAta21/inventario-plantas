package controllers;

import java.sql.*;

public class BaseDatos {
    private static Connection con;

    private static Statement consulta;
    private static ResultSet resultado;

    private final String AGREGAR_USUARIO = "INSERT INTO Usuario (Nombre, Contraseña, Imagen, Adminis) VALUES (?, ?, ?, ?)";


    public BaseDatos() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Catalogo", "Hiram", "coco123");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public boolean conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Catalogo", "Hiram", "coco123");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM Usuario");
            while (rs.next())
                System.out.println("Si hay algo en la base de datos");
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public void agregarUsuario(String username, String password, Boolean userType) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(AGREGAR_USUARIO);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setNull(3, java.sql.Types.NULL);
            ps.setBoolean(4, userType);
            ps.executeUpdate();
            System.out.println("Usuario agregado");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean validarUsuario(String username, String password, Boolean userType){
        try {
            consulta = con.createStatement();
            resultado = consulta.executeQuery("CALL validacion_usuario('" + username + "', '" + password + "', " + userType + ")");
            if (resultado.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
