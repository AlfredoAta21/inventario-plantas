package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class RegistroController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cmbUserType;

    BaseDatos mBD = new BaseDatos();

    @FXML
    public void initialize() {
    }

    @FXML
    void handleRegister(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de Registro");
            alert.setHeaderText(null);
            alert.setContentText("Todos los campos son obligatorios.");
            alert.showAndWait();
        } else {
            agregarUsuario(username, password);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Registro Exitoso");
            alert.setHeaderText(null);
            alert.setContentText("Cuenta registrada exitosamente.");
            alert.showAndWait();
        }
    }

    public void agregarUsuario(String Nombre, String contraseña) {
        mBD.agregarUsuario(Nombre, contraseña, false);
    }
}