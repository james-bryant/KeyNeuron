package net.uberfoo.keyboard.neuron;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class KeyNeuronApplication extends Application {

    private final Preferences preferences = Preferences.userNodeForPackage(KeyNeuronApplication.class);

    private Scene scene;
    private MainController mainController;

    @Override
    public void start(Stage stage) throws IOException {

        System.out.println("Starting KeyNeuronApplication");

        FXMLLoader fxmlLoader = new FXMLLoader(KeyNeuronApplication.class.getResource("main-view.fxml"));

        scene = new Scene(fxmlLoader.load());

        mainController = fxmlLoader.getController();

        stage.setX(preferences.getDouble("WINDOW_X", 200));
        stage.setY(preferences.getDouble("WINDOW_Y", 200));

        stage.setTitle("Key Neuron");
        stage.setScene(scene);
        stage.show();
        stage.sizeToScene();
    }

    @Override
    public void stop() {
        preferences.putDouble("WINDOW_X", scene.getWindow().getX());
        preferences.putDouble("WINDOW_Y", scene.getWindow().getY());
        mainController.close();
    }

    public static void main(String[] args) {
        System.out.println("Launching KeyNeuronApplication");
        launch();
    }

}