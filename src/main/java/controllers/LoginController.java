package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cmbUserType;

    BaseDatos mBD = new BaseDatos();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbUserType.setItems(FXCollections.observableArrayList("Admin", "User"));
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String userType = cmbUserType.getValue();

        if (userType == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user type.");
            alert.showAndWait();
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields.");
            alert.showAndWait();
            return;
        }

        validarCredenciales(username, password, userType);

    }

    @FXML
    void handleRegister(ActionEvent event) {
        openRegistroWindow();
    }

    private void openAltaPlantasWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/altaPlantas.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Alta de Plantas");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openRegistroWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/registro.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro de Cuenta");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void validarCredenciales(String username, String password, String userType) {
        Boolean esAdmin = userType.equals("Admin");
        System.out.println(esAdmin);
        Boolean exito = mBD.validarUsuario(username, password, esAdmin);
        if (exito) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Bienvenido " + userType + "!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Alerta de Login");
            alert.setHeaderText(null);
            alert.setContentText("usuario o contraseña, invalido");
            alert.showAndWait();
        }
    }
}