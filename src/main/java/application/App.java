package application;

import controllers.BaseDatos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    BaseDatos mBD = new BaseDatos();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        AnchorPane load = loader.load();

        Scene scene = new Scene(load);
        primaryStage.setScene(scene);
        primaryStage.show();

        mBD.conectar();         // Esto es para probar la conexion jaja

    }
}