package controllers;

import BaseDatos.BaseDatos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private ObservableList<Planta> plantasList;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    public void initialize() {
        plantasList = FXCollections.observableArrayList();
        colNombrePlanta.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionPlanta.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tablePlantas.setItems(plantasList);
        obtenerPlantas();
    }

    @FXML
    void handleAltaPlanta(ActionEvent event) {
        String nombrePlanta = txtNombrePlanta.getText();
        String descripcion = txtDescripcion.getText();

        if (nombrePlanta.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("El nombre de la planta no puede estar vacío.");
            alert.showAndWait();
        } else if (descripcion.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("La descripción de la planta no puede estar vacía.");
            alert.showAndWait();
        } else {
            Planta planta = new Planta(nombrePlanta, descripcion);
            plantasList.add(planta);
            agregarPlanta(nombrePlanta, descripcion);
            txtNombrePlanta.clear();
            txtDescripcion.clear();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Planta " + nombrePlanta + " dada de alta exitosamente.");
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

        public Planta(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDescripcion() {
            return descripcion;
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

    public void agregarPlanta(String nombre, String descripcion) {
        baseDatos.agregarPlanta(nombre, descripcion);
    }

    public void obtenerPlantas() {
        plantasList.clear();
        ArrayList<Planta> plantas = baseDatos.obtenerPlantas();
        if (plantas != null){
            plantasList.addAll(plantas);
        }
    }
}