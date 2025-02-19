package controllers;

import BaseDatos.BaseDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    
    Boolean esAdmin = false;
    
    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    BaseDatos mBD = new BaseDatos();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        validarCredenciales(username, password);
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
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/flor.png")));
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();
            stage.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUsuarioWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/usuario.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Usuario");
            stage.setScene(new Scene(pane));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/flor.png")));
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();
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
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/flor.png")));
            stage.show();
            stage = (Stage) txtUsername.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void validarCredenciales(String username, String password) {
        Boolean exito = mBD.validarUsuario(username, password);

        if (!exito) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Alerta de Login");
            alert.setHeaderText(null);
            alert.setContentText("usuario o contraseña, invalido");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        esAdmin = mBD.esAdmin(username, password);
        if(esAdmin){
            alert.setContentText("Bienvenido Administrador!");
            openAltaPlantasWindow();
        }
        else{
            alert.setContentText("Bienvenido Usuario!");
            openUsuarioWindow();
        }
            alert.showAndWait();
    }
}