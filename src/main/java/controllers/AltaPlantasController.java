package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class AltaPlantasController {

    @FXML
    private TextField txtNombrePlanta;

    @FXML
    private TableView<Planta> tablePlantas;

    @FXML
    private TableColumn<Planta, String> colNombrePlanta;

    private ObservableList<Planta> plantasList;

    @FXML
    public void initialize() {
        plantasList = FXCollections.observableArrayList();
        colNombrePlanta.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablePlantas.setItems(plantasList);
    }

    @FXML
    void handleAltaPlanta(ActionEvent event) {
        String nombrePlanta = txtNombrePlanta.getText();

        if (nombrePlanta.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("El nombre de la planta no puede estar vacío.");
            alert.showAndWait();
        } else {
            Planta planta = new Planta(nombrePlanta);
            plantasList.add(planta);
            txtNombrePlanta.clear();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Planta " + nombrePlanta + " dada de alta exitosamente.");
            alert.showAndWait();
        }
    }

    public static class Planta {
        private final String nombre;

        public Planta(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }
}