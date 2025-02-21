package BaseDatos;

import controllers.AltaPlantasController;

import java.sql.*;
import java.util.ArrayList;

public class BaseDatos {
    private static Connection con;

    private static Statement consulta;
    private static ResultSet resultado;

    private final String AGREGAR_USUARIO = "INSERT INTO Usuario (Nombre, Contraseña, Imagen, esAdmin) VALUES (?, ?, ?, ?)";
    private final String AGREGAR_PLANTA = "INSERT INTO Planta (Nombre, Descripcion, NombreCientifico, Propiedades, EfectosSecundarios) VALUES (?, ?, ?, ?, ?)";

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

    public boolean validarUsuario(String username, String password){
        try {
            consulta = con.createStatement();
            resultado = consulta.executeQuery("CALL validacion_usuario('" + username + "', '" + password + "')");
            if (resultado.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean esAdmin(String username, String password) {
        try {
            consulta = con.createStatement();
            resultado = consulta.executeQuery("CALL obtener_admin('" + username + "', '" + password + "')");
            if (resultado.next() && resultado.getInt("esAdmin") == 1) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean agregarPlanta(String nombre, String descripcion, String nombreCientifico, String propiedades, String efectosSecundarios) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(AGREGAR_PLANTA);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, nombreCientifico);
            ps.setString(4, propiedades);
            ps.setString(5, efectosSecundarios);
            ps.executeUpdate();
            System.out.println("Planta agregada");
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public ArrayList<AltaPlantasController.Planta> obtenerPlantas() {
        ArrayList<AltaPlantasController.Planta> plantas = new ArrayList<>();
        try {
            consulta = con.createStatement();
            resultado = consulta.executeQuery("SELECT * FROM Planta");
            while (resultado.next()) {
                AltaPlantasController.Planta planta = new AltaPlantasController.Planta(
                    resultado.getString("Nombre"),
                    resultado.getString("Descripcion"),
                    resultado.getString("NombreCientifico"),
                    resultado.getString("Propiedades"),
                    resultado.getString("EfectosSecundarios")
                );
                plantas.add(planta);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return plantas;
    }

    public boolean eliminarPlanta(String nombre) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Planta WHERE Nombre = ?");
            ps.setString(1, nombre);
            ps.executeUpdate();
            System.out.println("Planta eliminada");
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}