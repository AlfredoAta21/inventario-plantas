package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class holaMundoController {



    @FXML
    private Label lblMensaje;

    @FXML
    void click(ActionEvent event) {
        lblMensaje.setText("Hola Mundo");

    }
}
