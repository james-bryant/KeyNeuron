package net.uberfoo.keyboard.neuron;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.uberfoo.keyboard.neuron.model.ProcessInfo;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class KeyNeuronApplication extends Application {

    private final Preferences preferences = Preferences.userNodeForPackage(KeyNeuronApplication.class);

    private Scene scene;
    private ProcessService processService;

    @Override
    public void start(Stage stage) throws IOException {

        System.out.println("Starting KeyNeuronApplication");

        processService = new WindowsProcessService();
        processService.start();

        var consumer = new Consumer<ProcessInfo>() {
            @Override
            public void accept(ProcessInfo processInfo) {
                System.out.println(processInfo);
            }
        };

        processService.setConsumer(consumer);

        FXMLLoader fxmlLoader = new FXMLLoader(KeyNeuronApplication.class.getResource("main-view.fxml"));

        scene = new Scene(fxmlLoader.load());

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
        processService.stop();
    }

    public static void main(String[] args) {
        System.out.println("Launching KeyNeuronApplication");
        launch();
    }

}