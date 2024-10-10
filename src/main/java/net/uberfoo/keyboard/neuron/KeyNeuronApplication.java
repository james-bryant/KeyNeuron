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

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KeyNeuronApplication.class.getResource("main-view.fxml"));

        scene = new Scene(fxmlLoader.load(),
                preferences.getDouble("WIDTH", 640),
                preferences.getDouble("HEIGHT", 480));

        stage.setX(preferences.getDouble("WINDOW_X", 200));
        stage.setY(preferences.getDouble("WINDOW_Y", 200));

        stage.setTitle("Key Neuron");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        preferences.putDouble("WIDTH", scene.getWidth());
        preferences.putDouble("HEIGHT", scene.getHeight());
        preferences.putDouble("WINDOW_X", scene.getWindow().getX());
        preferences.putDouble("WINDOW_Y", scene.getWindow().getY());
    }

    public static void main(String[] args) {
        launch();
    }

}