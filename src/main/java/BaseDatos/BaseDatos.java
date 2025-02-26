package BaseDatos;

import controllers.AltaPlantasController;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;

public class BaseDatos {
    private static Connection con;

    private static Statement consulta;
    private static ResultSet resultado;

    private final String AGREGAR_USUARIO = "INSERT INTO Usuario (Nombre, Contraseña, Imagen, esAdmin) VALUES (?, ?, ?, ?)";
    private final String AGREGAR_PLANTA = "INSERT INTO Planta (nombre, descripcion, nombreCientifico, propiedades, efectosSecundarios, imagen) VALUES (?, ?, ?, ?, ?, ?)";

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

    public boolean agregarPlanta(String nombre, String descripcion, String nombreCientifico, String propiedades, String efectosSecundarios, byte[] imagenBytes) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(AGREGAR_PLANTA);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, nombreCientifico);
            ps.setString(4, propiedades);
            ps.setString(5, efectosSecundarios);

            // Si hay una imagen, la guardamos en la base de datos
            if (imagenBytes != null) {
                ps.setBytes(6, imagenBytes);
            } else {
                ps.setNull(6, java.sql.Types.BLOB);
            }

            ps.executeUpdate();
            System.out.println("Planta agregada");
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public ArrayList<AltaPlantasController.Planta> obtenerPlantas() {
        ArrayList<AltaPlantasController.Planta> plantas = new ArrayList<>();
        try {
            consulta = con.createStatement();
            resultado = consulta.executeQuery("SELECT * FROM Planta");

            while (resultado.next()) {
                // Recuperar los datos de la planta
                String nombre = resultado.getString("Nombre");
                String descripcion = resultado.getString("Descripcion");
                String nombreCientifico = resultado.getString("NombreCientifico");
                String propiedades = resultado.getString("Propiedades");
                String efectosSecundarios = resultado.getString("EfectosSecundarios");

                // Recuperar la imagen como byte[]
                byte[] imagenBytes = resultado.getBytes("Imagen");
                Image imagen = null;

                if (imagenBytes != null && imagenBytes.length > 0) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(imagenBytes);
                    imagen = new Image(bis);
                }

                // Crear la instancia de la planta con la imagen
                AltaPlantasController.Planta planta = new AltaPlantasController.Planta(
                        nombre, descripcion, nombreCientifico, propiedades, efectosSecundarios, imagen
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

    public boolean modificarPlanta(String oldNombre, String nombre, String descripcion, String nombreCientifico, String propiedades, String efectosSecundarios) {
    PreparedStatement ps = null;
    try {
        String query = "UPDATE Planta SET Nombre = ?, Descripcion = ?, NombreCientifico = ?, Propiedades = ?, EfectosSecundarios = ? WHERE Nombre = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setString(3, nombreCientifico);
        ps.setString(4, propiedades);
        ps.setString(5, efectosSecundarios);
        ps.setString(6, oldNombre);
        ps.executeUpdate();
        System.out.println("Planta modificada");
        return true;
    } catch (Exception e) {
        System.out.println(e);
        return false;
    }
}
}