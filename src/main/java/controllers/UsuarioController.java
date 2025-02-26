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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class UsuarioController {

    BaseDatos baseDatos = new BaseDatos();

    @FXML
    private TableView<AltaPlantasController.Planta> tablePlantas;

    @FXML
    private TableColumn<AltaPlantasController.Planta, String> colNombrePlanta;


    @FXML
    private ObservableList<AltaPlantasController.Planta> plantasList;

    @FXML
    private TextField txtNombrePlanta;

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

    @FXML
    public void initialize() {
        plantasList = FXCollections.observableArrayList();
        colNombrePlanta.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablePlantas.setItems(plantasList);
        obtenerPlantas();
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

        ArrayList<AltaPlantasController.Planta> plantas = baseDatos.obtenerPlantas();
        if (plantas != null){
            plantasList.addAll(plantas);
        }
    }

    public void llenarSeleccionado(){
        tablePlantas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtNombrePlanta.setText(newValue.getNombre());
                txtDescripcion.setText(newValue.getDescripcion());
                txtNombreCientifico.setText(newValue.getNombreCientifico());
                txtPropiedades.setText(newValue.getPropiedades());
                txtEfecSecundarios.setText(newValue.getEfectosSecundarios());

                imageView.setImage(newValue.getImagen());

                nombrePlanta.setText(newValue.getNombre());
            } else {
                imageView.setImage(null);
                nombrePlanta.setText("");
            }
        });
    }
}