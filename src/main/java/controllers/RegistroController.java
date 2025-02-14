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

    @FXML
    public void initialize() {
        cmbUserType.setItems(FXCollections.observableArrayList("Admin", "User"));
    }

    @FXML
    void handleRegister(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String userType = cmbUserType.getValue();

        if (username.isEmpty() || password.isEmpty() || userType == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de Registro");
            alert.setHeaderText(null);
            alert.setContentText("Todos los campos son obligatorios.");
            alert.showAndWait();
        } else {
            // Aquí puedes agregar la lógica para guardar la cuenta en una base de datos o en memoria
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Registro Exitoso");
            alert.setHeaderText(null);
            alert.setContentText("Cuenta registrada exitosamente.");
            alert.showAndWait();
        }
    }
}