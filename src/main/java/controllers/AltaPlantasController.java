package controllers;

import BaseDatos.BaseDatos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AltaPlantasController {
    BaseDatos baseDatos = new BaseDatos();

    @FXML
    private TextField txtNombrePlanta;

    @FXML
    private TableView<Planta> tablePlantas;

    @FXML
    private TableColumn<Planta, String> colNombrePlanta;

    @FXML
    private TableColumn<Planta, String> colDescripcionPlanta;

    @FXML
    private TableColumn<Planta, String> colNombreCientifico;

    @FXML
    private TableColumn<Planta, String> colPropiedades;

    @FXML
    private TableColumn<Planta, String> colEfectosSecundarios;

    @FXML
    private ObservableList<Planta> plantasList;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtNombreCientifico;

    @FXML
    private TextArea txtPropiedades;

    @FXML
    private TextArea txtEfecSecundarios;

    @FXML
    private ImageView imageView;

    @FXML
    private Label nombrePlanta;

    public void initialize() {
        plantasList = FXCollections.observableArrayList();
        colNombrePlanta.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionPlanta.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colNombreCientifico.setCellValueFactory(new PropertyValueFactory<>("nombreCientifico"));
        colPropiedades.setCellValueFactory(new PropertyValueFactory<>("propiedades"));
        colEfectosSecundarios.setCellValueFactory(new PropertyValueFactory<>("efectosSecundarios"));
        tablePlantas.setItems(plantasList);
        obtenerPlantas();

        tablePlantas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Mostrar la información de la planta seleccionada
                txtNombrePlanta.setText(newValue.getNombre());
                txtDescripcion.setText(newValue.getDescripcion());
                txtNombreCientifico.setText(newValue.getNombreCientifico());
                txtPropiedades.setText(newValue.getPropiedades());
                txtEfecSecundarios.setText(newValue.getEfectosSecundarios());

                // Mostrar la imagen en el ImageView
                imageView.setImage(newValue.getImagen());

                // Actualizar el texto del Label con el nombre de la planta seleccionada
                nombrePlanta.setText(newValue.getNombre());
            } else {
                // Limpiar los campos si no hay planta seleccionada
                imageView.setImage(null);  // Limpiar la imagen
                nombrePlanta.setText("");  // Limpiar el nombre de la planta
            }
        });
    }

    @FXML
    void handleAltaPlanta(ActionEvent event) {
        String nombre = txtNombrePlanta.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String nombreCientifico = txtNombreCientifico.getText().trim();
        String propiedades = txtPropiedades.getText().trim();
        String efectosSecundarios = txtEfecSecundarios.getText().trim();

        // Verificar que los campos obligatorios no estén vacíos
        if (nombre.isEmpty() || descripcion.isEmpty() || nombreCientifico.isEmpty() || propiedades.isEmpty() || efectosSecundarios.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos vacíos", "Por favor, llena todos los campos antes de continuar.");
            return;
        }

        // Verificar si imageView tiene una imagen
        if (imageView.getImage() == null) {
            mostrarAlerta(AlertType.WARNING, "Imagen requerida", "Debes agregar una imagen antes de registrar la planta.");
            return;
        }

        // Convertir la imagen en un arreglo de bytes (byte[])
        byte[] imagenBytes = convertirImagenABytes(imageView.getImage());

        // Intentar agregar la planta a la base de datos
        if (baseDatos.agregarPlanta(nombre, descripcion, nombreCientifico, propiedades, efectosSecundarios, imagenBytes)) {
            obtenerPlantas(); // Actualizar la lista de plantas
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Planta " + nombre + " agregada exitosamente.");

            // Limpiar los campos de entrada
            txtNombrePlanta.clear();
            txtDescripcion.clear();
            txtNombreCientifico.clear();
            txtPropiedades.clear();
            txtEfecSecundarios.clear();
            imageView.setImage(null); // También limpiar la imagen
        } else {
            mostrarAlerta(AlertType.ERROR, "Error", "No se pudo agregar la planta " + nombre + ".");
        }
    }


    private byte[] convertirImagenABytes(Image image) {
        try {
            // Convertir Image a BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream); // Puedes cambiar "png" por "jpg" si lo necesitas
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void handleModificarPlanta(ActionEvent event) {
        Planta selectedPlanta = tablePlantas.getSelectionModel().getSelectedItem();
        if (selectedPlanta != null) {
            String nombre = txtNombrePlanta.getText();
            String descripcion = txtDescripcion.getText();
            String nombreCientifico = txtNombreCientifico.getText();
            String propiedades = txtPropiedades.getText();
            String efectosSecundarios = txtEfecSecundarios.getText();

            if (baseDatos.modificarPlanta(selectedPlanta.getNombre(), nombre, descripcion, nombreCientifico, propiedades, efectosSecundarios)) {
                obtenerPlantas();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("Planta " + nombre + " modificada exitosamente.");
                alert.showAndWait();

                // limpiar los campos de texto y áreas de texto
                txtNombrePlanta.clear();
                txtDescripcion.clear();
                txtNombreCientifico.clear();
                txtPropiedades.clear();
                txtEfecSecundarios.clear();

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo modificar la planta " + nombre + ".");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione una planta para modificar.");
            alert.showAndWait();
        }
    }

    @FXML
    void handleEliminarPlanta(ActionEvent event) {
        Planta selectedPlanta = tablePlantas.getSelectionModel().getSelectedItem();
        if (selectedPlanta != null) {
            String nombrePlanta = selectedPlanta.getNombre();
            if (baseDatos.eliminarPlanta(nombrePlanta)) {
                plantasList.remove(selectedPlanta);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("Planta " + nombrePlanta + " eliminada exitosamente.");
                alert.showAndWait();

                // limpiar los campos de texto y áreas de texto
                txtNombrePlanta.clear();
                txtDescripcion.clear();
                txtNombreCientifico.clear();
                txtPropiedades.clear();
                txtEfecSecundarios.clear();

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo eliminar la planta " + nombrePlanta + ".");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione una planta para eliminar.");
            alert.showAndWait();
        }
    }

    public static class Planta {
        private final String nombre;
        private String descripcion;
        private String nombreCientifico;
        private String propiedades;
        private String efectosSecundarios;
        private Image imagen;

        public Planta(String nombre, String descripcion, String nombreCientifico, String propiedades, String efectosSecundarios, Image imagen) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.nombreCientifico = nombreCientifico;
            this.propiedades = propiedades;
            this.efectosSecundarios = efectosSecundarios;
            this.imagen = imagen;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getNombreCientifico() {
            return nombreCientifico;
        }

        public String getPropiedades() {
            return propiedades;
        }

        public String getEfectosSecundarios() {
            return efectosSecundarios;
        }

        public Image getImagen() {
            return imagen;
        }
    }

    @FXML
    void handleCerrarSesion(ActionEvent event){
        openLoginWindow();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(pane));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/flor.png")));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerPlantas() {
        plantasList.clear();
        ArrayList<Planta> plantas = baseDatos.obtenerPlantas();
        if (plantas != null) {
            plantasList.addAll(plantas);
        }
    }

    public void agregarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
    }

}